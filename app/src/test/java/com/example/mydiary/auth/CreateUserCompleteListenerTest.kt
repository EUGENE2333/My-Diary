package com.example.mydiary.auth

import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.domain.repository.CreateUserCompleteListener
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class CreateUserCompleteListenerTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        firebaseAuth = mock(FirebaseAuth::class.java)
        authRepository = AuthRepository(firebaseAuth)
    }

    @Test
    fun `onComplete invokes true when create user is successful`() {
        // Given
        val onCompleteCalled = TaskCompletionSource<Boolean>()
        val onComplete = { success: Boolean -> onCompleteCalled.setResult(success) }
        val createUserCompleteListener = CreateUserCompleteListener(onComplete)

        // Simulate a successful task
        val mockAuthResult = mock(AuthResult::class.java)
        val mockTask = Tasks.forResult(mockAuthResult)

        // When
        createUserCompleteListener.onComplete(mockTask)

        // Then
        assertEquals(true, onCompleteCalled.task.result)
    }

    @Test
    fun `onComplete invokes false when create user is not successful`() {
        // Given
        val onCompleteCalled = TaskCompletionSource<Boolean>()
        val onComplete = { success: Boolean -> onCompleteCalled.setResult(success) }
        val createUserCompleteListener = CreateUserCompleteListener(onComplete)

        // Simulate a failed task
        val mockTask = Tasks.forException<AuthResult>(Exception("Create user failed"))

        // When
        createUserCompleteListener.onComplete(mockTask)

        // Then
        assertEquals(false, onCompleteCalled.task.result)
    }
}