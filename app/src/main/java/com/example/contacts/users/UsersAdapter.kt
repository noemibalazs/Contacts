package com.example.contacts.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.contacts.databinding.ListItemUserBinding
import com.example.contacts.model.User

class UsersAdapter(private val userListener: UserListener) :
    ListAdapter<User, UsersAdapter.UserViewHolder>(UserDifUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding: ListItemUserBinding =
            ListItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(private val binding: ListItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                data = user
                listener = userListener
                executePendingBindings()
            }
        }
    }

    class UserDifUtil : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean = oldItem == newItem

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
            oldItem.email == newItem.email && oldItem.name == newItem.name

    }
}