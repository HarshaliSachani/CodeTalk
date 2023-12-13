package com.example.chatapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentUpdatePasswordBinding
import com.example.chatapp.fragments.BaseFragment
import com.example.chatapp.utils.CommonDialog
import com.example.chatapp.viewmodels.ForgetPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class UpdatePasswordActivity : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentUpdatePasswordBinding
    private val model: ForgetPasswordViewModel by sharedViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdatePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        binding.btnUpdate.setOnClickListener(this)
    }

    private fun setObserver() {
        model.updatePwd.observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.success) {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                    model.isFinishActivity.value = true
                } else
                    CommonDialog.Builder()
                        .setTitle(it.title)
                        .setMessage(it.message)
                        .setButton2(resources.getString(R.string.ok)) {}
                        .create(requireActivity())
            }
        }
    }

    override fun onClick(v: View?) {
        if (v == binding.btnUpdate) {
            val pwd = binding.edtPassWord.text?.trim().toString()
            val cPwd = binding.edtReEnterPassWord.text?.trim().toString()

            if (pwd.isEmpty()) CommonDialog.Builder()
                .setTitle(resources.getString(R.string.alert))
                .setMessage(resources.getString(R.string.enter_password))
                .setButton2(resources.getString(R.string.ok)) {}
                .create(requireActivity())
            else if (cPwd.isEmpty())
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_confirm_password))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(requireActivity())
            else if (pwd != cPwd)
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.password_confirm_password_not_match))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(requireActivity())
            else model.updatePassword(pwd)


        }
    }

}