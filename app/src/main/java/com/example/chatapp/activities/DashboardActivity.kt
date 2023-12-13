package com.example.chatapp.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapters.GroupAdapter
import com.example.chatapp.adapters.GroupIconAdapter
import com.example.chatapp.databinding.ActivityDashboardBinding
import com.example.chatapp.databinding.DialogCreateGroupBinding
import com.example.chatapp.extensions.startChatActivity
import com.example.chatapp.extensions.startIntroActivity
import com.example.chatapp.models.Group
import com.example.chatapp.socket_service.Connection
import com.example.chatapp.utils.AppData
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.CommonDialog
import com.example.chatapp.utils.PrefConstant
import com.example.chatapp.viewmodels.DashboardViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private val model: DashboardViewModel by viewModel()
    private var groupAdapter: GroupAdapter? = null
    private var currentGroup: Group? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)
        Log.e(TAG, "onCreate: open.......................")
        showLoader()

        setObserver()
        bindViews()

        if (AppPrefs.getBoolean(PrefConstant.isAdmin)) model.fetchAdminAllGroup()
        else model.fetchUserAllGroup()

    }

    private fun bindViews() {
        binding.btnCreateNewGroup.isVisible = AppPrefs.getBoolean(PrefConstant.isAdmin)
        binding.txtUserName.text = AppPrefs.getString(PrefConstant.display_name)

        binding.btnCreateNewGroup.setOnClickListener {
            createGroupDialog()
        }

        binding.imgLogout.setOnClickListener {
            CommonDialog.Builder()
                .setTitle(resources.getString(R.string.logout))
                .setMessage(resources.getString(R.string.would_you_like_to_logout))
                .setButton1(resources.getString(R.string.yes)) {
                    AppPrefs.clearPref()
                    startIntroActivity()
                    finish()
                }
                .setButton2(resources.getString(R.string.no)) {}
                .create(this@DashboardActivity)

        }

        Glide.with(binding.imgUserProfile)
            .asDrawable()
            .load(if (AppPrefs.getBoolean(PrefConstant.isAdmin)) R.drawable.admin_profile else resources.getIdentifier("profile_" + AppPrefs.getString(PrefConstant.user_profile), "drawable", packageName))
            .into(binding.imgUserProfile)

        setupGroupAdapter()

    }

    override fun onResume() {
        super.onResume()
        if (AppData.getInstance().isRefreshGroup) {
            AppData.getInstance().isRefreshGroup = false
            model.fetchAdminAllGroup()
        }
    }

    private fun setObserver() {
        model.createGroupData.observe(this) {
            hideLoader()
            if (it != null) {
                CommonDialog.Builder()
                    .setTitle(resources.getString(if (it.success) R.string.success else R.string.alert))
                    .setMessage(it.message)
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(this@DashboardActivity)
                if (it.success) model.fetchAdminAllGroup()
            }
        }

        model.fetchGroupData.observe(this) {
            hideLoader()
            Log.e(TAG, "setObserver:   dismiss loader")
            if (it != null) {
                if (it.success) groupAdapter?.submitList(it.data.group)
                else
                    CommonDialog.Builder()
                        .setTitle(resources.getString(R.string.alert))
                        .setMessage(it.message)
                        .setButton2(resources.getString(R.string.ok)) {}
                        .create(this@DashboardActivity)
            }
        }

    }

    private fun setSocketEvent() {
        Connection.getInstance().receiveJoinEvent {
            currentGroup?.let {
                AppData.getInstance().groupId = it._id
                Log.e(TAG, "onCreate: start chat")
                hideLoader()
                startChatActivity(it.group_name, it.group_icon)
            }

        }
    }

    private fun createGroupDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        val dialogBinding = DialogCreateGroupBinding.inflate(LayoutInflater.from(this), null, false)
        dialog.setContentView(dialogBinding.root)

        val groupIconList = listOf(
            R.drawable.group_1, R.drawable.group_2, R.drawable.group_3, R.drawable.group_4, R.drawable.group_5,
            R.drawable.group_6, R.drawable.group_7, R.drawable.group_8, R.drawable.group_9, R.drawable.group_10
        )

        var groupIconAdapter: GroupIconAdapter? = null
        groupIconAdapter = GroupIconAdapter(this, onGroupClick = {
            groupIconAdapter?.setGroupIconSelection(it)

        })

        dialogBinding.recyclerGroupIcon.layoutManager = GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false)
        dialogBinding.recyclerGroupIcon.adapter = groupIconAdapter
        groupIconAdapter.submitList(groupIconList)

        dialogBinding.btnCreateGroup.setOnClickListener {
            val groupName = dialogBinding.edtGroupName.text?.trim().toString()
            if (groupName.isEmpty())
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_group_name_msg))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(this@DashboardActivity)
            else if (groupIconAdapter.selectedIndex == -1)
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_group_name_msg))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(this@DashboardActivity)
            else {
                model.createNewGroup(AppPrefs.getString(PrefConstant.id), groupName, (groupIconAdapter.selectedIndex + 1).toString())
                dialog.dismiss()
            }
        }

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }
        dialog.show()

    }

    private fun redirectToChatScreen() {
        currentGroup?.let {
            Log.e(TAG, "open chat screen:   sendJoinGroupEvent")
            Connection.getInstance().sendJoinGroupEvent(it._id, it.group_name)
        }

    }

    private fun connectSocket() {
        Connection.getInstance().connect(onConnected = {
            Log.e(TAG, "open chat screen:   onConnected")
            setSocketEvent()
            Handler(Looper.getMainLooper()).postDelayed({
                redirectToChatScreen()
            }, 500)
        })
    }

    private fun setupGroupAdapter() {
        groupAdapter = GroupAdapter(this, onGroupClick = { group ->
            Log.e(TAG, "setupGroupAdapter: $group")
            showLoader()
            currentGroup = group
            if (Connection.getInstance().isConnected()) {
                Log.e(TAG, "open chat screen:   relaunch")
                redirectToChatScreen()
            } else {
                Log.e(TAG, "open chat screen:  ")
                connectSocket()
            }


        })
        binding.recyclerGroupList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerGroupList.adapter = groupAdapter

    }

}