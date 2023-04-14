package com.example.moqayda.ui.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.ItemChatUserBinding
import com.example.moqayda.models.AppUser

class UserAdapter(private val context: Context, private val userList: ArrayList<AppUser>) :
    RecyclerView.Adapter<UserAdapter.ChatViewHolder>() {
    class ChatViewHolder(val binding: ItemChatUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AppUser){
            binding.user = user
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val viewBinding:ItemChatUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat_user,
            parent,
            false
        )
        return ChatViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}