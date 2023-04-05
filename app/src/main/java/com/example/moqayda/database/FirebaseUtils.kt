package com.example.moqayda.database

import com.example.moqayda.models.AppUser
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

fun getCollectionReference(collectionName: String): CollectionReference {
    val db = Firebase.firestore
    return db.collection(collectionName)
}

fun addUserToFirestore(
    user: AppUser,
    onSuccessListener: OnSuccessListener<Void>,
    onFailureListener: OnFailureListener
) {
    val doc = getCollectionReference(AppUser.COLLECTION_NAME).document(user.id!!)
    doc.set(user).addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}

fun getUserFromFirestore(
    userId: String,
    onSuccessListener: OnSuccessListener<DocumentSnapshot>,
    onFailureListener: OnFailureListener
) {
    val doc = getCollectionReference(AppUser.COLLECTION_NAME).document(userId).get()
    doc.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener)
}