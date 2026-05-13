package com.example.hallisanthe.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseFirestoreService {

    val db: FirebaseFirestore = Firebase.firestore

    val productsCollection = db.collection("products")
    val artisansCollection = db.collection("artisans")
    val categoriesCollection = db.collection("categories")
    val enquiriesCollection = db.collection("enquiries")
}
