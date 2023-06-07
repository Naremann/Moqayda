package com.example.moqayda.ui.blockedUsers

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.databinding.BlockedUserItemBinding
import com.example.moqayda.models.AppUser

class BlockedUsersAdapter(
    private val blockedUsers: List<AppUser>,
    private val mContext: Context,
    private val viewModel: BlockedUsersViewModel,
) : RecyclerView.Adapter<BlockedUsersAdapter.BlockedUsersViewHolder>() {
    private lateinit var  builder: AlertDialog.Builder
    private lateinit var  dialog: AlertDialog
    inner class BlockedUsersViewHolder(val binding: BlockedUserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(blockedUser:AppUser){
            binding.blockedUser = blockedUser
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlockedUsersViewHolder {
        val binding:BlockedUserItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.blocked_user_item,
            parent,
            false
        )
        return BlockedUsersViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return blockedUsers.size
    }

    override fun onBindViewHolder(holder: BlockedUsersViewHolder, position: Int) {
        val blockedUser = blockedUsers[position]
        holder.bind(blockedUser)
        holder.binding.unBlockUser.setOnClickListener {
            val animation: Animation = AnimationUtils.loadAnimation(mContext, R.anim.click_animation)
            it.startAnimation(animation)

            builder = AlertDialog.Builder(mContext)
            builder.setTitle(mContext.getString(R.string.confirmation))
            builder.setMessage(R.string.un_block_confirmation)

            builder.setPositiveButton(mContext.getString(R.string.ok)) { dialog, which ->
                viewModel.unBlockUser(blockedUser.id!!)
            }

            builder.setNegativeButton(mContext.getString(R.string.cancel)) { _, _ ->
                // Cancel button clicked

            }
            dialog = builder.create()
            dialog.show()
        }
    }
}