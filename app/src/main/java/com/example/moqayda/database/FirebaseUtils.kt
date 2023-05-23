package com.example.moqayda.database

import android.net.Uri
import com.example.moqayda.models.AppUser
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


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
fun updateFirebaseUser(userId:String, onCompleteListener: OnCompleteListener<Void>,user: AppUser){
    val doc = getCollectionReference(AppUser.COLLECTION_NAME).document(userId)
    doc.update(mapOf("firstName" to user.firstName,"lastName" to user.lastName,"city" to user.city,
        "phoneNumber" to user.phoneNumber)).addOnCompleteListener(onCompleteListener) }

fun updateFirebaseUserToken(userId:String, onCompleteListener: OnCompleteListener<Void>,token: String){
    val doc = getCollectionReference(AppUser.COLLECTION_NAME).document(userId)
    doc.update(mapOf("token" to token)).addOnCompleteListener(onCompleteListener) }


fun storeImageInFirebaseStore(filePath:Uri,userId: String,onSuccessListener: OnSuccessListener<UploadTask.TaskSnapshot>,onFailureListener: OnFailureListener) {
    getStorageReference().child(
        "images/$userId").putFile(filePath).addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener)
}

fun getFirebaseImageUri(onSuccessListener: OnSuccessListener<Uri>, onFailureListener: OnFailureListener, userId: String){
    getStorageReference().child("images/$userId").downloadUrl.addOnSuccessListener(onSuccessListener).addOnFailureListener(onFailureListener)
}

fun getUerImageFromFirebase(valueEventListener: ValueEventListener, userId: String){
    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.reference
    val getImage = databaseReference.child("images/$userId")
    getImage.addListenerForSingleValueEvent(valueEventListener)

}
fun getStorageReference(): StorageReference {
    val storageReference: StorageReference
    val storage: FirebaseStorage = FirebaseStorage.getInstance();
    storageReference = storage.reference
    return storageReference
}
fun deleteImageFromFirebaseFirestore(userId:String,onSuccessListener: OnSuccessListener<Void>,onFailureListener: OnFailureListener){
    getStorageReference().child("images/$userId").delete().addOnSuccessListener(onSuccessListener)
        .addOnFailureListener(onFailureListener)
}




