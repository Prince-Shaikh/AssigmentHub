package dev.sagar.assigmenthub.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.hellosagar.assigmenthub.R
import dev.hellosagar.assigmenthub.databinding.FragmentVerifyOtpBinding
import dev.sagar.assigmenthub.HomeActivity
import dev.sagar.assigmenthub.ui.viewmodel.VerifyOtpViewModel
import dev.sagar.assigmenthub.utils.ResponseModel
import dev.sagar.assigmenthub.utils.toast
import timber.log.Timber

@AndroidEntryPoint
class VerifyOtpFragment : Fragment(R.layout.fragment_verify_otp) {

    private val binding by viewBinding(FragmentVerifyOtpBinding::bind)
    private val args by navArgs<VerifyOtpFragmentArgs>()
    private val viewModel: VerifyOtpViewModel by viewModels()
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var name: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnVerify.setOnClickListener {
            val otp = binding.tietOtp.text.toString()
            email = args.email
            password = args.password
            name = args.name
            viewModel.verifyOtp(email, otp)
        }

        viewModel.verifyTeacher.observe(
            viewLifecycleOwner,
            Observer {
                it.getContentIfNotHandled()?.let { result ->
                    when (result) {
                        is ResponseModel.Loading -> {
                            Timber.i("Loading")
                        }
                        is ResponseModel.Success -> {
                            viewModel.loginTeacher(email, password)
                        }
                        is ResponseModel.Error -> {
                            toast(result.message)
                            Timber.i(result.error)
                        }
                    }
                }
            }
        )

        viewModel.loginTeacher.observe(
            viewLifecycleOwner,
            Observer {
                it.getContentIfNotHandled()?.let { result ->
                    when (result) {
                        is ResponseModel.Loading -> {
                            Timber.i("Loading")
                        }
                        is ResponseModel.Success -> {
                            Timber.i(result.response)
                            viewModel.createTeacher(name, email)
                        }
                        is ResponseModel.Error -> {
                            toast(result.message)
                            Timber.i(result.error)
                        }
                    }
                }
            }
        )

        viewModel.createTeacher.observe(
            viewLifecycleOwner,
            Observer {
                it.getContentIfNotHandled()?.let { result ->
                    when (result) {
                        is ResponseModel.Loading -> {
                            Timber.i("Loading")
                        }
                        is ResponseModel.Success -> {
                            Timber.i(result.message)
                            requireActivity().startActivity(
                                Intent(
                                    requireActivity(),
                                    HomeActivity::class.java
                                )
                            )
                            requireActivity().finish()
                        }
                        is ResponseModel.Error -> {
                            toast(result.message)
                            Timber.i(result.error)
                        }
                    }
                }
            }
        )
    }
}
