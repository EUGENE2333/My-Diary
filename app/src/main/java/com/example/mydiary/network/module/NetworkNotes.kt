package com.example.mydiary.network.module

import com.google.firebase.Timestamp


data class NetworkNotes(
    val userId: String ,
    val title: String,
    val description: String,
    val timestamp: Timestamp,
    val colorIndex: Int,
    val documentId: String
)
