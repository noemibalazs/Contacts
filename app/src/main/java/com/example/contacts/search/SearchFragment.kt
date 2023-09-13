package com.example.contacts.search

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.contacts.R
import com.example.contacts.base.BaseFragment
import com.example.contacts.databinding.FragmentSearchBinding
import com.example.contacts.model.User
import com.example.contacts.users.UserListener
import com.example.contacts.users.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search), UserListener {

    private val viewModel: SearchViewModel by viewModels()
    private val adapter: UsersAdapter by lazy { UsersAdapter(this) }

    override fun bind(view: View) {
        super.bind(view)
        viewBinding = FragmentSearchBinding.bind(view)

        viewBinding.apply {
            lifecycleOwner = this@SearchFragment
            model = viewModel
            listener = this@SearchFragment
            usersRecycleView.adapter = adapter
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.setEmailAddressState()
                adapter.submitList(null)
                viewBinding.errorMessage.isVisible = false
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit
            override fun afterTextChanged(p0: Editable?) = Unit
        }

        viewBinding.editText.addTextChangedListener(textWatcher)

        viewBinding.editText.setOnEditorActionListener { textView, actionId, _ ->
            when (actionId == EditorInfo.IME_ACTION_DONE) {
                true -> {
                    viewModel.searchedTermUpdated(textView.text.toString())
                    viewModel.initField()
                    true
                }

                else -> false
            }
        }

        observeData()
    }

    private fun observeData() {
        with(viewModel) {
            isValidEmailAddress.observe(viewLifecycleOwner) { isValid -> setEmailValidationState(isValid) }

            users.observe(viewLifecycleOwner) { users ->
                viewBinding.errorMessage.isVisible = users.isEmpty() && viewBinding.editText.text?.isNotBlank() == true
                if (viewBinding.editText.text?.isNotBlank() == true) adapter.submitList(users)
            }
        }
    }

    private fun setEmailValidationState(isValid: Boolean) {
        val (errorMessage, errorEnabled) = when (isValid) {
            true -> null to false
            else -> getString(R.string.error_invalid_email_address) to true
        }

        viewBinding.inputLayout.isErrorEnabled = errorEnabled
        viewBinding.inputLayout.error = errorMessage
    }

    override fun onUserClicked(user: User) =
        findNavController().navigate(SearchFragmentDirections.fromSearchToDetails(user.id.value))
}