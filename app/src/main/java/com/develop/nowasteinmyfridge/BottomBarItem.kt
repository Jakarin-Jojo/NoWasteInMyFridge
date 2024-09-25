package com.develop.nowasteinmyfridge

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
) {
    data object Home : BottomBarItem(
        route = "main/home",
        title = "Home",
        icon = Icons.Default.Home,
    )

    data object Inventory : BottomBarItem(
        route = "main/inventory",
        title = "Inventory",
        icon = Icons.AutoMirrored.Filled.List,
    )

    data object Adding : BottomBarItem(
        route = "main/adding",
        title = "Adding",
        icon = Icons.Default.Add,
    )

    data object Setting : BottomBarItem(
        route = "main/setting",
        title = "Setting",
        icon = Icons.Default.AccountCircle,
    )
}