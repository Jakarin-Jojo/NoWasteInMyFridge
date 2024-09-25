package com.develop.nowasteinmyfridge.feature.setting

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.ui.graphics.vector.ImageVector
import com.develop.nowasteinmyfridge.R

sealed class MenuSettingScreen(
    val route: String,
    @StringRes
    val nameResID: Int,
    val icon: ImageVector,
) {
    data object Account : MenuSettingScreen(
        route = "main/setting/account",
        nameResID = R.string.menu_account,
        icon = Icons.Default.AccountCircle,
    )


    data object Grocery : MenuSettingScreen(
        route = "main/setting/grocery",
        nameResID = R.string.menu_grocery,
        icon = Icons.Default.Notifications,
    )

    data object WasteReport : MenuSettingScreen(
        route = "main/setting/waste_report",
        nameResID = R.string.menu_waste_report,
        icon = Icons.Default.Notifications,
    )
}
