package com.example.chatapp.activities

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.adapters.ChatAdapter
import com.example.chatapp.adapters.GroupUserAdapter
import com.example.chatapp.databinding.ActivityChatBinding
import com.example.chatapp.databinding.DialogAddUserBinding
import com.example.chatapp.databinding.DialogRemoveUserBinding
import com.example.chatapp.models.ChatMsg
import com.example.chatapp.models.ChatMsgResponse
import com.example.chatapp.models.GroupUser
import com.example.chatapp.socket_service.Connection
import com.example.chatapp.utils.*
import com.example.chatapp.viewmodels.ChatViewModel
import com.google.gson.Gson
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel


class ChatActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityChatBinding
    private val model: ChatViewModel by viewModel()
    private var chatAdapter: ChatAdapter? = null
    private var msgList = ArrayList<ChatMsg>()
    private var id = 0
    private var groupName = ""
    private var isDeleteGroup = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat)

        binding.btnMore.isVisible = AppPrefs.getBoolean(PrefConstant.isAdmin)


        showLoader(true)
        setClickListener()
        setObserver()

        intent?.extras?.let { bundle ->
            if (bundle.containsKey(Constants.GROUP_NAME) && bundle.getString(Constants.GROUP_NAME) != null) {
                groupName = bundle.getString(Constants.GROUP_NAME)!!
                binding.txtUserName.text = groupName
            }
            if (bundle.containsKey(Constants.GROUP_ICON) && bundle.getString(Constants.GROUP_ICON) != null)
                Glide.with(binding.imgUserProfile)
                    .asDrawable()
                    .load(resources.getIdentifier("group_" + bundle.getString(Constants.GROUP_ICON), "drawable", packageName))
                    .into(binding.imgUserProfile)
        }

        setSocketEvents()
        Connection.getInstance().sendFetchAllMessageEvent(groupName)

    }

    override fun onBackPressed() {
//        super.onBackPressed()
        Log.e(TAG, "onBackPressed:  finish screen")
        finish()
    }

    private fun setObserver() {
        model.deleteGroup.observe(this) {
            if (it != null) {
                CommonDialog.Builder()
                    .setTitle(it.title)
                    .setMessage(it.message)
                    .setButton2(resources.getString(R.string.ok)) {
                        if (it.success && isDeleteGroup) {
                            AppData.getInstance().isRefreshGroup = true
                            finish()
                        }
                    }
                    .create(this@ChatActivity)
            }
        }

        model.removeUser.observe(this) {
            if (it != null) {
                CommonDialog.Builder()
                    .setTitle(it.title)
                    .setMessage(it.message)
                    .setButton2(resources.getString(R.string.ok)) { }
                    .create(this@ChatActivity)
            }
        }

        model.addUser.observe(this) {
            if (it != null) {
                CommonDialog.Builder()
                    .setTitle(it.title)
                    .setMessage(it.message)
                    .setButton2(resources.getString(R.string.ok)) { }
                    .create(this@ChatActivity)
            }
        }

        model.groupUserData.observe(this) {
            Log.e(TAG, "setObserver: observer it=$it")
            if (it != null) {
                Log.e(TAG, "setObserver: it=$it")
                if (it.success) {
                    if (it.data.isNotEmpty()) removeUser(it.data)
                    else CommonDialog.Builder()
                        .setTitle(resources.getString(R.string.alert))
                        .setMessage(resources.getString(R.string.no_user_found))
                        .setButton2(resources.getString(R.string.ok)) { }
                        .create(this@ChatActivity)
                } else CommonDialog.Builder()
                    .setTitle(resources.getString(if (it.success) R.string.success else R.string.alert))
                    .setMessage(it.message)
                    .setButton2(resources.getString(R.string.ok)) { }
                    .create(this@ChatActivity)

            }
        }

        model.clearChatHistory.observe(this) { chatMsgResponse ->
            chatMsgResponse?.let {
                runOnUiThread {
                    Log.e(TAG, "setObserver:  it.message=${it.message}")
                    chatAdapter = ChatAdapter(this@ChatActivity, AppPrefs.getString(PrefConstant.id), it.message)
                    binding.recyclerChatMsg.adapter = chatAdapter
                    binding.recyclerChatMsg.post {
                        binding.recyclerChatMsg.smoothScrollToPosition(it.message.size - 1)
                    }
                }
            }
        }

    }


    private fun setSocketEvents() {
        Connection.getInstance().receiveGroupAllMessages {
            Log.e(TAG, "onCreate: get All msg for groups=$it")
            val chatMsgResponse = Gson().fromJson(it, ChatMsgResponse::class.java)
            Log.e(TAG, "onCreate: get All msg for groups model=$chatMsgResponse")
            chatMsgResponse?.let {

                runOnUiThread {
                    chatAdapter = ChatAdapter(this@ChatActivity, AppPrefs.getString(PrefConstant.id), chatMsgResponse.message)
                    binding.recyclerChatMsg.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                    binding.recyclerChatMsg.adapter = chatAdapter
                    binding.recyclerChatMsg.post {
                        binding.recyclerChatMsg.smoothScrollToPosition(chatMsgResponse.message.size - 1)
                    }
                }


                /*if (chatMsgResponse.message.size > 0) chatAdapter?.oldUserId = chatMsgResponse.message[0].userId
                runOnUiThread {
                    msgList = ArrayList(chatMsgResponse.message)
                    chatAdapter?.submitList(chatMsgResponse.message)
                    binding.recyclerChatMsg.post {
                        binding.recyclerChatMsg.smoothScrollToPosition(chatMsgResponse.message.size - 1)
                    }
                }*/

            }
            Handler(Looper.getMainLooper()).postDelayed({
                binding.recyclerChatMsg.visibility = View.VISIBLE
                hideLoader(true)
            }, Constants.LOADER_DELAY)

        }

        Connection.getInstance().receiveMessageEvent {
            Log.e(TAG, "onCreate: get msg response=$it")
            val jObj = JSONObject(it)
            val chatMsg = ChatMsg(
                id,
                jObj.getString(Constants.ID),
                jObj.getString(Constants.USER_NAME),
                jObj.getString(Constants.ROOM),
                jObj.getString(Constants.MESSAGE),
                jObj.getBoolean(Constants.IS_ADMIN),
                jObj.getString(Constants.PROFILE_INDEX),
                jObj.getLong(Constants.CREATED_AT),
            )
            id++
            msgList.add(chatMsg)
            runOnUiThread {

                chatAdapter?.addNewMessage(chatMsg)
                binding.recyclerChatMsg.post {
                    chatAdapter?.msgList?.size?.let { index ->
                        binding.recyclerChatMsg.smoothScrollToPosition(index)
                    }
                }
            }


        }
    }

    private fun setClickListener() {
        binding.btnSendMsg.setOnClickListener(this)
        binding.btnMore.setOnClickListener(this)

        binding.edtMsg.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.btnSendMsg.isVisible = count > 0
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSendMsg -> {
                val msg = binding.edtMsg.text?.trim().toString()
                Log.e(TAG, "send message=$msg")
                if (msg.isNotEmpty()) {

                    binding.edtMsg.text?.clear()

                    val jObj = JSONObject()
                    jObj.put(Constants.ID, AppPrefs.getString(PrefConstant.id))
                    jObj.put(Constants.IS_ADMIN, AppPrefs.getBoolean(PrefConstant.isAdmin))
                    jObj.put(Constants.USER_NAME, AppPrefs.getString(PrefConstant.display_name))
                    jObj.put(Constants.MESSAGE, msg)
                    jObj.put(Constants.ROOM, AppData.getInstance().groupId)
                    jObj.put(Constants.GROUP, groupName)
                    Log.e(TAG, "send message send Obj=$jObj")

                    Connection.getInstance().sendMessageEvent(jObj)
                }
            }
            binding.btnMore -> {
                val popupMenu = PopupMenu(this@ChatActivity, binding.btnMore)

                // Inflating popup menu from popup_menu.xml file
                popupMenu.menuInflater.inflate(R.menu.menu_chat, popupMenu.menu)
                popupMenu.setOnMenuItemClickListener { item ->
                    when (item.title) {
                        resources.getString(R.string.add_user) -> addUserDialog()
                        resources.getString(R.string.remove_user) -> model.fetchGroupUser(groupName)
                        resources.getString(R.string.delete_group) ->
                            CommonDialog.Builder()
                                .setTitle(resources.getString(R.string.are_you_sure))
                                .setMessage(resources.getString(R.string.are_you_sure_to_delete_group))
                                .setButton1(resources.getString(R.string.no)) {}
                                .setButton2(resources.getString(R.string.yes)) {
                                    isDeleteGroup = true
                                    model.deleteGroup(groupName)
                                }
                                .create(this@ChatActivity)
                        resources.getString(R.string.clear_history) ->
                            CommonDialog.Builder()
                                .setTitle(resources.getString(R.string.clear_this_chat))
                                .setMessage(resources.getString(R.string.are_you_sure_to_clear_history))
                                .setButton1(resources.getString(R.string.no)) {}
                                .setButton2(resources.getString(R.string.yes)) {
                                    model.clearChatHistory(groupName)
                                }
                                .create(this@ChatActivity)
                    }
                    true
                }
                popupMenu.show()
            }
        }
    }


    private fun addUserDialog() {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        val dialogBinding = DialogAddUserBinding.inflate(LayoutInflater.from(this), null, false)
        dialog.setContentView(dialogBinding.root)

        dialogBinding.btnAddUser.setOnClickListener {
            val emailId = dialogBinding.edtUserId.text?.trim().toString()
            val userName = dialogBinding.edtUserName.text?.trim().toString()
            if (emailId.isEmpty())
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_user_email_id))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(this@ChatActivity)
            else if (userName.isEmpty())
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_user_name))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(this@ChatActivity)
            else {
                model.addUser(groupName, emailId, userName)
                dialog.dismiss()
            }
        }
        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()


    }

    private fun removeUser(groupUserList: List<GroupUser>) {
        val dialog = Dialog(this, R.style.Theme_Dialog)
        val dialogBinding = DialogRemoveUserBinding.inflate(LayoutInflater.from(this), null, false)
        dialog.setContentView(dialogBinding.root)

        var groupUserAdapter: GroupUserAdapter? = null
        groupUserAdapter = GroupUserAdapter(this, onGroupClick = {
            CommonDialog.Builder()
                .setTitle(resources.getString(R.string.are_you_sure))
                .setMessage(resources.getString(R.string.are_you_sure_to_remove_user))
                .setButton1(resources.getString(R.string.no)) {}
                .setButton2(resources.getString(R.string.yes)) {
                    model.removeUser(groupName, it.user_name)
                    dialog.dismiss()
                }
                .create(this@ChatActivity)

        })

        dialogBinding.recyclerGroupIcon.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        dialogBinding.recyclerGroupIcon.adapter = groupUserAdapter
        groupUserAdapter.submitList(groupUserList)

        dialogBinding.btnCancel.setOnClickListener { dialog.dismiss() }

        dialog.show()

    }

}