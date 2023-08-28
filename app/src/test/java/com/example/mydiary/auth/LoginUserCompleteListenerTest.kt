package com.example.mydiary.auth

import com.example.mydiary.domain.repository.AuthRepository
import com.example.mydiary.domain.repository.LoginUserCompleteListener
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class LoginUserCompleteListenerTest {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp(){
        firebaseAuth = mock(FirebaseAuth::class.java)
        authRepository = AuthRepository(firebaseAuth)
    }

    @Test
    fun `onComplete invokes true when login is successful` (){
      //Given
        val onCompleteCalled = TaskCompletionSource<Boolean>()
        val onComplete = {success: Boolean-> onCompleteCalled.setResult(success) }
        val loginUserCompleteListener =   LoginUserCompleteListener(onComplete)

        //simulate a successful task
        val  mockAuthResult = mock(AuthResult::class.java)
        val mockTask = Tasks.forResult(mockAuthResult)

        //when
        loginUserCompleteListener.onComplete(mockTask)

        //then
        assertEquals(true, onCompleteCalled.task.result)


    }


    @Test
    fun `onComplete invokes false when login is not successful`(){

        // Given
        val onCompleteCalled = TaskCompletionSource<Boolean>()
        val onComplete = { success: Boolean -> onCompleteCalled.setResult(success) }
        val loginUserCompleteListener =   LoginUserCompleteListener(onComplete)

        //simulate a failed task
        val mockTask = Tasks.forException<AuthResult>(Exception("Loin user failed"))

        //when
        loginUserCompleteListener.onComplete(mockTask)

        //then
        assertEquals(false, onCompleteCalled.task.result)


    }
}

















