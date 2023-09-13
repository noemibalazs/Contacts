package com.example.contacts.userdetails

import android.Manifest.permission.CALL_PHONE
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.contacts.R
import com.example.contacts.base.BaseFragment
import com.example.contacts.databinding.FragmentUserDetailsBinding
import com.example.contacts.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailsFragment :
    BaseFragment<FragmentUserDetailsBinding>(R.layout.fragment_user_details),
    UserDetailsClickListener {

    private val args: UserDetailsFragmentArgs by navArgs()
    private val detailsViewModel: UserDetailsViewModel by viewModels()

    private val callPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[CALL_PHONE] == true -> placeCall()
                else -> toast(getString(R.string.label_permission_call))
            }
        }

    override fun bind(view: View) {
        super.bind(view)
        viewBinding = FragmentUserDetailsBinding.bind(view)

        viewBinding.apply {
            lifecycleOwner = this@UserDetailsFragment
            viewModel = detailsViewModel
            listener = this@UserDetailsFragment
        }

        viewBinding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        detailsViewModel.loadUserDetails(args.seedValue)

        observeData()
    }

    private fun observeData() {
        detailsViewModel.observeViewState().observe(viewLifecycleOwner) {
            if (it == UserDetailsViewModel.ViewState.UserDetailsFailed) toast(getString(R.string.error_message_label))
        }
    }

    override fun onPhoneClicked() {
        checkCallPermission()
    }

    override fun onEmailClicked(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:$email")
        startActivity(intent)
    }

    private fun checkCallPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), CALL_PHONE))
                AlertDialog.Builder(requireContext())
                    .setTitle(getString(R.string.label_permission_header))
                    .setMessage(getString(R.string.label_permission_call))
                    .setPositiveButton(android.R.string.ok) { _, _ -> requestPermission() }
                    .show()
            else
                requestPermission()
        } else
            placeCall()
    }

    private fun requestPermission() = callPermissionRequest.launch(arrayOf(CALL_PHONE))

    private fun placeCall() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:${detailsViewModel.user.value?.phoneNumber}")
        startActivity(intent)
    }
}