package com.develop.nowasteinmyfridge.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.develop.nowasteinmyfridge.BottomBarScreen
import com.develop.nowasteinmyfridge.Screen
import com.develop.nowasteinmyfridge.feature.account.AccountScreen
import com.develop.nowasteinmyfridge.feature.grocerylist.GroceryListScreen
import com.develop.nowasteinmyfridge.feature.login.LoginScreen
import com.develop.nowasteinmyfridge.feature.setting.MenuSettingScreen
import com.develop.nowasteinmyfridge.feature.setting.SettingScreen
import com.develop.nowasteinmyfridge.feature.wastereport.WasteReportScreen

const val SETTING_GRAPH_ROUTE = "setting_graph"

fun NavGraphBuilder.settingNavGraph(
    navHostController: NavHostController,
) {
    navigation(
        route = SETTING_GRAPH_ROUTE,
        startDestination = BottomBarScreen.Setting.route,
    ) {
        composable(route = BottomBarScreen.Setting.route) {
            SettingScreen(navHostController)
        }
        composable(route = MenuSettingScreen.Account.route){
            AccountScreen(navController = navHostController)
        }
        composable(route = MenuSettingScreen.Grocery.route){
            GroceryListScreen()
        }
        composable(route = MenuSettingScreen.WasteReport.route){
            WasteReportScreen()
        }
    }

}
