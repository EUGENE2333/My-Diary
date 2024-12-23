package com.example.mydiary.di.firebase


typealias FirestoreData = Map<String, Any?>

object FirestoreDatabase {
    object Users {
        // Collection Reference
        const val COLLECTION_NAME = "users"

        // Fields
        object Fields {
            const val UID = "uid"
            const val EMAIL = "email"
            const val PHOTO_URL = "photoUrl"
            const val CREATED_AT = "createdAt"
            const val SUBSCRIBED_AT = "subscribedAt"
        }
    }
}