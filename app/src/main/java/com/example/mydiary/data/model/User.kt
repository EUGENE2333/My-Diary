package com.example.mydiary.data.model

import java.util.Date


@JvmInline
value class UserId(val value: String)

data class User(
    val id: UserId,
    val email: String? = null,
    val photoUrl: String?,
    val createdAt: Date?,
    val subscribedAt: Date? = null

)
