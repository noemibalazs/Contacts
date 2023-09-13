package com.example.contacts.users

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.contacts.R
import com.example.contacts.base.BaseFragment
import com.example.contacts.databinding.FragmentUsersBinding
import com.example.contacts.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentUsersBinding>(R.layout.fragment_users), UserListener {

    private val viewModel: UsersViewModel by viewModels()
    private val adapter: UsersAdapter by lazy { UsersAdapter(this) }

    override fun bind(view: View) {
        super.bind(view)
        viewBinding = FragmentUsersBinding.bind(view)

        viewBinding.apply {
            listener = this@UsersFragment
            userViewModel = viewModel
            lifecycleOwner = this@UsersFragment
            usersRecycleView.adapter = adapter
        }

        viewModel.loadUsers()
    }

    override fun onUserClicked(user: User) =
        findNavController().navigate(UsersFragmentDirections.fromUsersToDetails(user.id.value))

}