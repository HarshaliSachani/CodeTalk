package com.example.chatapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentForgetPasswordBinding
import com.example.chatapp.utils.AppPrefs
import com.example.chatapp.utils.CommonDialog
import com.example.chatapp.utils.PrefConstant
import com.example.chatapp.viewmodels.ForgetPasswordViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ForgetPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var binding: FragmentForgetPasswordBinding
    private val model: ForgetPasswordViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentForgetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        binding.btnNext.setOnClickListener(this)
    }

    private fun setObserver() {
        model.getOtp.observe(viewLifecycleOwner) {
            if (it != null) {
                if (!it.success)
                    CommonDialog.Builder()
                        .setTitle(it.title)
                        .setMessage(it.message)
                        .setButton2(resources.getString(R.string.ok)) {}
                        .create(requireActivity())

            }
        }
    }

    override fun onClick(v: View?) {
        if (v == binding.btnNext) {
            val emailId = binding.edtEmailId.text?.trim().toString()
            if (emailId.isEmpty()) {
                CommonDialog.Builder()
                    .setTitle(resources.getString(R.string.alert))
                    .setMessage(resources.getString(R.string.enter_user_email_id))
                    .setButton2(resources.getString(R.string.ok)) {}
                    .create(requireActivity())
            } else {
                AppPrefs.saveValue(PrefConstant.temp_user_name, emailId)
                model.generateOtp(emailId)
            }
        }
    }

}