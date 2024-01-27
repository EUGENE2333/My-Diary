package com.example.mydiary.network.module

import com.google.firebase.Timestamp


data class NetworkNotes(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    val documentId: String = ""
)
