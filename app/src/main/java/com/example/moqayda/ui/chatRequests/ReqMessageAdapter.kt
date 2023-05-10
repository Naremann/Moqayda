package com.example.moqayda.ui.chatRequests

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.moqayda.R
import com.example.moqayda.api.RetrofitBuilder
import com.example.moqayda.bindImage
import com.example.moqayda.databinding.ItemChatUserBinding
import com.example.moqayda.models.AppUser
import com.example.moqayda.models.MessageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReqMessageAdapter(
    private val reqList: List<MessageRequest>,
    private val requestViewModel: RequestViewModel,
    private val mContext: Context,
) :
    RecyclerView.Adapter<ReqMessageAdapter.ChatViewHolder>() {
    lateinit var appUser: AppUser
    inner class ChatViewHolder(val binding: ItemChatUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(req: MessageRequest, requestViewModel: RequestViewModel) {
            binding.req = req
            when (req.isApproved) {
                true -> {
                    binding.accept.visibility = View.GONE
                    binding.cancel.visibility = View.GONE
                    binding.ConstraintLayout.setOnClickListener {
                        // TODO navigate to chat req.id
                        it.findNavController().navigate(
                            RequestsFragmentDirections.actionRequestFragmentToChatFragment(req,appUser)
                        )
                        requestViewModel.selectedChat(req.id!!)
                        requestViewModel.getMessage(req.id!!)
                    }
                }
                false -> {
                    binding.accept.visibility = View.VISIBLE
                    binding.cancel.visibility = View.VISIBLE
                }
                null -> {
                    Log.i("ChatViewHolder", "no Request")
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val viewBinding: ItemChatUserBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_chat_user,
            parent,
            false
        )
        return ChatViewHolder(viewBinding)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {

        val currentUser = Firebase.auth.currentUser
        val req = reqList[position]
        holder.bind(req, requestViewModel)
        val builder = AlertDialog.Builder(mContext)



        if (currentUser?.uid == req.senderId) {

            GlobalScope.launch {
                appUser = requestViewModel.getUser(req.receiverId!!)!!
                holder.binding.appUser = appUser
            }

        } else {
            GlobalScope.launch {
                appUser = requestViewModel.getUser(req.senderId!!)!!
                holder.binding.appUser = appUser
            }

        }

        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to cancel this Request ?")
        holder.binding.accept.setOnClickListener {
            requestViewModel.openReq(req)
        }
        holder.binding.cancel.setOnClickListener {
            builder.setPositiveButton("OK") { _, _ ->
                requestViewModel.deleteReq(req)
            }
            builder.setNegativeButton("Cancel") { _, _ ->
                // Cancel button clicked
                // Do something here
            }
            val dialog = builder.create()
            dialog.show()
        }


    }


    private fun showToastMessage(message: String) {
        Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()

    }

    override fun getItemCount(): Int {
        return reqList.size
    }

//    private fun loadUserImage(holder: ChatViewHolder, userId: String) {
//        getUerImageFromFirebase(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                getFirebaseImageUri({ uri ->
//                    Picasso.with(holder.binding.userImage.context).load(uri)
//                        .into(holder.binding.userImage)
//
//                }, { ex ->
//                    ex.localizedMessage?.let { error ->
//                        showToastMessage(error)
//                    }
//
//                }, userId)
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                showToastMessage("Error Loading Image")
//            }
//        }, userId)
//    }
}