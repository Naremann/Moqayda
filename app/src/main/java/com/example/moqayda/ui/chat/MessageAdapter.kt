package com.example.moqayda.ui.chat


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.moqayda.R
import com.example.moqayda.bindImage
import com.example.moqayda.bindImageUri
import com.example.moqayda.databinding.ImageMessageBinding
import com.example.moqayda.databinding.MessageBinding
import com.example.moqayda.models.Message
import com.example.moqayda.ui.chat.ChatFragment.Companion.ANONYMOUS
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

// The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
// See: https://github.com/firebase/FirebaseUI-Android
class MessageAdapter(
    private val messageList: List<Message>,
    private val currentUserName: String?,
) : RecyclerView.Adapter<ViewHolder>() {

    private fun setSenderAndReceiver(
        userName: String?,
        textView: TextView?,
        messageLayout: ConstraintLayout
    ) {
        if (userName != ANONYMOUS && currentUserName == userName && userName != null) {
            messageLayout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            textView?.setBackgroundResource(R.drawable.rounded_message_blue)
            textView?.setTextColor(Color.WHITE)

        } else {
            textView?.setBackgroundResource(R.drawable.rounded_message_gray)
            textView?.setTextColor(Color.BLACK)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_TEXT) {
            val view = inflater.inflate(R.layout.message, parent, false)
            val binding = MessageBinding.bind(view)
            MessageViewHolder(binding)
        } else {
            val view = inflater.inflate(R.layout.image_message, parent, false)
            val binding = ImageMessageBinding.bind(view)
            ImageMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]
        if (messageList[position].text != null) {
            (holder as MessageViewHolder).bind(message)
        } else {
            (holder as ImageMessageViewHolder).bind(message)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].text != null) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    inner class MessageViewHolder(private val binding: MessageBinding) : ViewHolder(binding.root) {
        fun bind(item: Message) {
            binding.messageTextView.text = item.text
            setSenderAndReceiver(item.senderName, binding.messageTextView,binding.messageLayout)
            binding.messengerTextView.text = item.senderName ?: ANONYMOUS
            if (item.senderPhotoUrl != null) {
                loadImageIntoView(binding.messengerImageView, item.senderPhotoUrl)
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
            }
        }


    }

    inner class ImageMessageViewHolder(private val binding: ImageMessageBinding) :
        ViewHolder(binding.root) {
        fun bind(item: Message) {
            setSenderAndReceiver(item.senderName, null,binding.imageMessageLayout)
            loadImageIntoView(binding.messageImageView, item.imageUrl!!, false)
            Log.e("MessageAdapter",item.imageUrl)
            binding.messengerTextView.text = item.senderName ?: ANONYMOUS
            if (item.senderPhotoUrl != null) {
                loadImageIntoView(binding.messengerImageView, item.senderPhotoUrl)
            } else {
                binding.messengerImageView.setImageResource(R.drawable.ic_account_circle_black_36dp)
            }
        }
    }

    private fun loadImageIntoView(view: ImageView, url: String, isCircular: Boolean = true) {
        if (url.startsWith("gs://")) {
            Log.e("MessageAdapter","loadWithGlide url starts with gs")
            val storageReference = Firebase.storage.getReferenceFromUrl(url)
            storageReference.downloadUrl
                .addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    loadWithGlide(view, downloadUrl, isCircular)
                    Log.e("MessageAdapter","addOnSuccessListener")
                }
                .addOnFailureListener { e ->
                    Log.w(
                        TAG,
                        "Getting download url was not successful.",
                        e
                    )
                    Log.e("MessageAdapter","addOnFailureListener")
                }
        } else {
            loadWithGlide(view, url, isCircular)
            Log.e("MessageAdapter","loadWithGlide url is not starts with gs")
        }
    }

    private fun loadWithGlide(view: ImageView, url: String, isCircular: Boolean = true) {
        Glide.with(view.context).load(url).into(view)
        var requestBuilder = Glide.with(view.context).load(url)
        Log.e("MessageAdapter","loadWithGlide Method")
        if (isCircular) {
            requestBuilder = requestBuilder.transform(CircleCrop())
        }
        requestBuilder.into(view)
    }

    companion object {
        const val TAG = "MessageAdapter"
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
    }

    override fun getItemCount(): Int {
        return messageList.size
    }
}
