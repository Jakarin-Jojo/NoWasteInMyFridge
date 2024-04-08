package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.UserProfile
import com.develop.nowasteinmyfridge.domain.GetUserInfoUseCase
import com.develop.nowasteinmyfridge.domain.LogoutUseCase
import com.develop.nowasteinmyfridge.feature.account.AccountViewModel
import com.develop.nowasteinmyfridge.feature.setting.SettingViewModel
import com.develop.nowasteinmyfridge.util.Result
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
class AccountViewModelTest {

    // Mock dependencies
    private val getUserInfoUseCase: GetUserInfoUseCase = mockk()
    private val logoutUseCase: LogoutUseCase = mockk()

    // Subject under test
    private lateinit var viewModel: AccountViewModel

    @After
    fun tearDown() {
        // Cleanup
        unmockkAll()
    }

    @Test
    fun testGetUserInfoSuccess() = runTest {
        // Mock the behavior of the use case to return a flow with success result
        MockKAnnotations.init(this)
        val userInfoFlow = flowOf(Result.Success(UserProfile()))
        coEvery { getUserInfoUseCase.invoke() } returns userInfoFlow
        viewModel = AccountViewModel(getUserInfoUseCase,logoutUseCase)

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Verify that the correct value is set
        coVerify(exactly = 1) { getUserInfoUseCase.invoke() }
        assertEquals(Result.Success(UserProfile()), viewModel.userProfileInfoState.value)
    }

    @Test
    fun testLogoutSuccess() = runTest {
        // Mock the behavior of the logout use case
        MockKAnnotations.init(this)
        val userInfoFlow = flowOf(Result.Success(UserProfile()))
        coEvery { getUserInfoUseCase.invoke() } returns userInfoFlow
        coEvery { logoutUseCase.invoke() } just runs
        viewModel = AccountViewModel(getUserInfoUseCase,logoutUseCase)

        // Perform action
        viewModel.logout()

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Verify that the logout use case is invoked
        coVerify(exactly = 1) { logoutUseCase.invoke() }
    }
}