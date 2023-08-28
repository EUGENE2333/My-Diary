package com.example.mydiary.auth

import com.example.mydiary.domain.repository.AuthRepository
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

//@RunWith(RobolectricTestRunner::class)
class AuthRepositoryTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {

        firebaseAuth = mock(FirebaseAuth::class.java)
        authRepository = AuthRepository(firebaseAuth)
    }

    @Test
    fun test_hasUser_returns_true_when_currentUser_is_not_null() {
        val mockCurrentUser: FirebaseUser = mock(FirebaseUser::class.java)
        `when`(firebaseAuth.currentUser).thenReturn(mockCurrentUser)

        val result = authRepository.hasUser()

        assertThat(result).isTrue()
    }

    @Test
    fun test_hasUser_returns_false_when_currentUser_is_null() {
        `when`(firebaseAuth.currentUser).thenReturn(null)

        val result = authRepository.hasUser()

        assertThat(result).isFalse()
    }

    @Test
    fun test_getUserID_returns_UID_when_currentUser_is_not_null(){

        val mockCurrentUser: FirebaseUser = mock(FirebaseUser::class.java)
        `when`(mockCurrentUser.uid).thenReturn("test_uid")
        `when`(firebaseAuth.currentUser).thenReturn(mockCurrentUser)

        val result = authRepository.getUserId()

        assertEquals("test_uid", result)


    }
    @Test
    fun `test getUserId returns empty string when currentUser is null`() {
        `when`(firebaseAuth.currentUser).thenReturn(null)

        val result = authRepository.getUserId()

        assertEquals("", result)

    }

}






















