package com.example.moqayda.ui.chat

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moqayda.R
import com.example.moqayda.base.BaseFragment
import com.example.moqayda.databinding.FragmentChatBinding
import com.example.moqayda.initToolbar
import com.example.moqayda.models.Message
import com.example.moqayda.ui.chatRequests.RequestViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.BuildConfig
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class ChatFragment : BaseFragment<FragmentChatBinding,RequestViewModel>() {
    private lateinit var manager: LinearLayoutManager
    private val requestViewModel by activityViewModels<RequestViewModel>()


    // Firebase instance variables
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private lateinit var adapter: MessageAdapter
    val user = Firebase.auth.currentUser

    private val openDocument = registerForActivityResult(MyOpenDocumentContract()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        hideBottomAppBar()
        viewDataBinding.toolbar.initToolbar(viewDataBinding.toolbar,"Chat",this)
        if (BuildConfig.DEBUG) {
            Firebase.database.useEmulator("10.0.2.2", 9000)
            Firebase.auth.useEmulator("10.0.2.2", 9099)
            Firebase.storage.useEmulator("10.0.2.2", 9199)
        }


        auth = Firebase.auth

        db = Firebase.database


        requestViewModel.message.observe(viewLifecycleOwner) {
            adapter = MessageAdapter(it, getUserName())
            viewDataBinding.progressBar.visibility = ProgressBar.INVISIBLE
            manager = LinearLayoutManager(requireContext())
            manager.stackFromEnd = true
            viewDataBinding.messageRecyclerView.layoutManager = manager
            viewDataBinding.messageRecyclerView.adapter = adapter
            adapter.registerAdapterDataObserver(
                MyScrollToBottomObserver(viewDataBinding.messageRecyclerView, adapter, manager)
            )
        }

        // The FirebaseRecyclerAdapter class and options come from the FirebaseUI library
        // See: https://github.com/firebase/FirebaseUI-Android

        // Scroll down when a new message arrives
        // See MyScrollToBottomObserver for details


        // Disable the send button when there's no text in the input field
        // See MyButtonObserver for details
        viewDataBinding.messageEditText.addTextChangedListener(MyButtonObserver(viewDataBinding.sendButton))

        // When the send button is clicked, send a text message
        viewDataBinding.sendButton.setOnClickListener {
            //TODO call viewModel
            val message = Message("",
                viewDataBinding.messageEditText.text.toString(),
                getUserName(),
                getPhotoUrl(),
                null
            )
            viewDataBinding.messageEditText.setText("")
            requestViewModel.navigateToSelectedChat.observe(viewLifecycleOwner) {
                requestViewModel.setMessage(message, it)
            }
        }

        // When the image button is clicked, launch the image picker
        viewDataBinding.addMessageImageView.setOnClickListener {
            openDocument.launch(arrayOf("image/*"))
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        const val ANONYMOUS = "anonymous"
    }

    private fun onImageSelected(uri: Uri) {
        Log.d(TAG, "Uri: $uri")
        val tempMessage = Message(user!!.uid, null, getUserName(), getPhotoUrl(), "")
        requestViewModel.navigateToSelectedChat.observe(viewLifecycleOwner) {
            db.getReference("messages//${it}").push().setValue(tempMessage,
                DatabaseReference.CompletionListener { databaseError, databaseReference ->
                    if (databaseError != null) {
                        Log.w(
                            TAG, "Unable to write message to database.",
                            databaseError.toException()
                        )
                        return@CompletionListener
                    }

                    // Build a StorageReference and then upload the file
                    val key = databaseReference.key
                    val storageReference = Firebase.storage
                        .getReference(user!!.uid)
                        .child(key!!)
                        .child(uri.lastPathSegment!!)
                    putImageInStorage(storageReference, uri, key)
                })
        }
    }

    private fun putImageInStorage(storageReference: StorageReference, uri: Uri, key: String?) {
        // First upload the image to Cloud Storage
        storageReference.putFile(uri)
            .addOnSuccessListener { taskSnapshot -> // After the image loads, get a public downloadUrl for the image
                // and add it to the message.
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        val friendlyMessage =
                            Message(user!!.uid, null, getUserName(), getPhotoUrl(), uri.toString())
                        requestViewModel.navigateToSelectedChat.observe(viewLifecycleOwner) {
                            db.getReference("messages//${it}")
                                .child(key!!)
                                .setValue(friendlyMessage)
                        }
                    }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Image upload task was unsuccessful.", e)
            }
    }

    private fun getUserName(): String? {
        val user = auth.currentUser
        return if (user != null) {
            user.email
        } else ANONYMOUS
    }

    private fun getPhotoUrl(): String? {
        val user = auth.currentUser
        return user?.photoUrl?.toString()
    }

    override fun getViews(): View {
        return viewDataBinding.root
    }

    override fun initViewModeL(): RequestViewModel {
        return ViewModelProvider(this)[RequestViewModel::class.java]
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_chat
    }

}