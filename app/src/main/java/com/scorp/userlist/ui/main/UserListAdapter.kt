package com.scorp.userlist.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.scorp.userlist.R
import com.scorp.userlist.data.Person
import com.scorp.userlist.databinding.UserListItemBinding

class UserListAdapter: PagedListAdapter<Person, UserListAdapter.UserViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemBinding: UserListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.user_list_item,
            parent,
            false
        )
        return UserViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { person -> holder.bind(person) }
    }

    inner class UserViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(person: Person) {
            binding.person = person
        }
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Person> =
            object : DiffUtil.ItemCallback<Person>() {
                override fun areItemsTheSame(
                    oldItem: Person,
                    newItem: Person
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: Person,
                    newItem: Person
                ): Boolean = oldItem.fullName == newItem.fullName
            }
    }
}

