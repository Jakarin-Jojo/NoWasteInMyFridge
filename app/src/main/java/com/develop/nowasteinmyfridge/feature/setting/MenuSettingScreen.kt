package com.develop.nowasteinmyfridge.feature.setting

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.develop.nowasteinmyfridge.R

sealed class MenuSettingScreen(
    val route: String,
    @StringRes
    val nameResID: Int,
    val icon: ImageVector,
) {
    data object Account : MenuSettingScreen(
        route = "account",
        nameResID = R.string.menu_account,
        icon = Icons.Default.AccountCircle,
    )

    data object Setting : MenuSettingScreen(
        route = "setting",
        nameResID = R.string.menu_setting,
        icon = Icons.Default.Settings,
    )

    data object Notification : MenuSettingScreen(
        route = "notification",
        nameResID = R.string.menu_notification,
        icon = Icons.Default.Notifications,
    )

    data object Password : MenuSettingScreen(
        route = "password",
        nameResID = R.string.menu_password,
        icon = Icons.Default.Password,
    )

    data object Grocery : MenuSettingScreen(
        route = "grocery",
        nameResID = R.string.menu_grocery,
        icon = Icons.Default.Notifications,
    )

    data object WasteReport : MenuSettingScreen(
        route = "waste_report",
        nameResID = R.string.menu_waste_report,
        icon = Icons.Default.Notifications,
    )

    data object TestNotification:MenuSettingScreen(
        route = "test notification",
        nameResID = R.string.test_noti,
        icon = Icons.Default.Notifications,
    )
}
