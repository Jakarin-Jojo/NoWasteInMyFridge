package com.develop.nowasteinmyfridge

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.develop.nowasteinmyfridge.feature.adding.AddingScreen
import com.develop.nowasteinmyfridge.feature.home.HomeScreen
import com.develop.nowasteinmyfridge.feature.home.MenuScreen
import com.develop.nowasteinmyfridge.feature.inventory.InventoryScreen
import com.develop.nowasteinmyfridge.feature.setting.navigation.settingNavGraph

const val MAIN_GRAPH_ROUTE = "main"
const val MENU_SCREEN_ROUTE = "menu/{name}/{image}?ingredients={ingredients}"
const val BACK_TO_APP_ROUTE = "back_to_app"

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        route = MAIN_GRAPH_ROUTE,
        startDestination = BottomBarScreen.Home.route,
    ) {
        composable(route = BottomBarScreen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = BottomBarScreen.Inventory.route) {
            InventoryScreen()
        }
        composable(route = BottomBarScreen.Adding.route) {
            AddingScreen(navController)
        }
        settingNavGraph(navController)
        composable(
            route = MENU_SCREEN_ROUTE,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("ingredients") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val image = backStackEntry.arguments?.getString("image")
            val ingredientsString = backStackEntry.arguments?.getString("ingredients")
            val ingredients = ingredientsString?.split(";") ?: emptyList()
            Log.d("koko", "$ingredientsString")

            if (name != null && image != null) {
                MenuScreen(name = name, image = image, ingredientList = ingredients)
            }
        }
        composable(route = BACK_TO_APP_ROUTE) {
            AppNavHost()
        }
    }
}
