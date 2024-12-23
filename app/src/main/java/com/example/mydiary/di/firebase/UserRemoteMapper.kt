package com.example.mydiary.di.firebase

import com.example.mydiary.data.model.User
import com.example.mydiary.data.model.UserId
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import java.util.Date

fun FirebaseUser.toUser(): User = User(
    id = UserId(uid),
    email = email,
    photoUrl = photoUrl?.toString(),
    createdAt = metadata?.creationTimestamp?.let { Date(it) } ?: Date(),
)

fun User.toFireStoreUserData() =
    mapOf(
        FirestoreDatabase.Users.Fields.UID to id.value,
        FirestoreDatabase.Users.Fields.EMAIL to email,
        FirestoreDatabase.Users.Fields.PHOTO_URL to photoUrl,
        // We use the server timestamp to avoid issues with the device time
        FirestoreDatabase.Users.Fields.CREATED_AT to FieldValue.serverTimestamp(),
        FirestoreDatabase.Users.Fields.SUBSCRIBED_AT to subscribedAt?.let { Timestamp(it) }
    )

fun FirestoreData.toUser(uid: String) =
    User(
        id = UserId(uid),
        email = this[FirestoreDatabase.Users.Fields.EMAIL].toString(),
        photoUrl = this[FirestoreDatabase.Users.Fields.PHOTO_URL].toString(),
        createdAt = (this[FirestoreDatabase.Users.Fields.CREATED_AT] as? Timestamp)?.toDate(),
        subscribedAt = (this[FirestoreDatabase.Users.Fields.SUBSCRIBED_AT] as? Timestamp)?.toDate()
    )