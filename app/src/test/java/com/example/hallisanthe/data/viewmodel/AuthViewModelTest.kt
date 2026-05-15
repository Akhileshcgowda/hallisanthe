package com.example.hallisanthe.data.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.hallisanthe.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AuthViewModel
    private lateinit var authRepository: AuthRepository
    private val mockUser = mockk<FirebaseUser>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk(relaxed = true)
        every { authRepository.currentUser } returns MutableStateFlow(null)
        every { authRepository.isLoggedIn } returns false

        viewModel = AuthViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `isLoggedIn should return repository login state`() {
        every { authRepository.isLoggedIn } returns true

        assertTrue(viewModel.isLoggedIn)
    }

    @Test
    fun `signInWithEmailPassword should call repository and return success`() = runTest {
        coEvery { authRepository.signInWithEmailPassword(any(), any()) } returns Result.success(mockk())
        var successCalled = false

        viewModel.signInWithEmailPassword("test@example.com", "password") { success, _ ->
            successCalled = success
        }

        coVerify { authRepository.signInWithEmailPassword("test@example.com", "password") }
        assertTrue(successCalled)
    }

    @Test
    fun `signInWithEmailPassword should handle failure`() = runTest {
        coEvery { authRepository.signInWithEmailPassword(any(), any()) } returns Result.failure(Exception("Invalid credentials"))
        var errorMessage: String? = null

        viewModel.signInWithEmailPassword("test@example.com", "wrong") { success, error ->
            if (!success) errorMessage = error
        }

        assertNotNull(errorMessage)
        assertTrue(errorMessage!!.contains("Invalid"))
    }

    @Test
    fun `signUpWithEmailPassword should call repository`() = runTest {
        coEvery { authRepository.signUpWithEmailPassword(any(), any(), any()) } returns Result.success(mockk())

        viewModel.signUpWithEmailPassword("new@example.com", "password", "Test User") { _, _ -> }

        coVerify { authRepository.signUpWithEmailPassword("new@example.com", "password", "Test User") }
    }

    @Test
    fun `signInWithGoogle should call repository with idToken`() = runTest {
        coEvery { authRepository.signInWithGoogle(any()) } returns Result.success(mockk())

        viewModel.signInWithGoogle("google_id_token") { _, _ -> }

        coVerify { authRepository.signInWithGoogle("google_id_token") }
    }

    @Test
    fun `signOut should call repository signOut`() = runTest {
        viewModel.signOut()

        coVerify { authRepository.signOut() }
    }

    @Test
    fun `sendPasswordResetEmail should call repository`() = runTest {
        coEvery { authRepository.sendPasswordResetEmail(any()) } returns Result.success(Unit)

        viewModel.sendPasswordResetEmail("test@example.com") { _, _ -> }

        coVerify { authRepository.sendPasswordResetEmail("test@example.com") }
    }

    @Test
    fun `sendPasswordResetEmail should handle success`() = runTest {
        coEvery { authRepository.sendPasswordResetEmail(any()) } returns Result.success(Unit)
        var successCalled = false

        viewModel.sendPasswordResetEmail("test@example.com") { success, _ ->
            successCalled = success
        }

        assertTrue(successCalled)
    }
}
