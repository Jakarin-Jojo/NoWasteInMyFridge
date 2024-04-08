package com.develop.nowasteinmyfridge.integrationTest

import com.develop.nowasteinmyfridge.data.model.UserProfile
import com.develop.nowasteinmyfridge.domain.GetUserInfoUseCase
import com.develop.nowasteinmyfridge.feature.setting.SettingViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.develop.nowasteinmyfridge.util.Result
import io.mockk.coVerify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
class SettingViewModelTest {

    // Mock dependencies
    private val getUserInfoUseCase: GetUserInfoUseCase = mockk()

    // Subject under test
    private lateinit var viewModel: SettingViewModel

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun testGetUserInfoSuccess() = runTest {

        MockKAnnotations.init(this)
        val userInfoFlow = flowOf(Result.Success(UserProfile()))
        coEvery { getUserInfoUseCase.invoke() } returns userInfoFlow
        viewModel = SettingViewModel(getUserInfoUseCase)

        // Advance until all coroutines under test have completed
        advanceUntilIdle()

        // Verify that the user profile information state is updated correctly
        coVerify(exactly = 1) { getUserInfoUseCase.invoke() }
    }
}