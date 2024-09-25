package com.develop.nowasteinmyfridge

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
const val MENU_SCREEN_ROUTE =
    "menu/{name}/{image}?ingredients={ingredients}&calories={calories}&totalTime={totalTime}&mealType={mealType}&dishType={dishType}"
const val BACK_TO_APP_ROUTE = "back_to_app"

@Composable
fun BottomNavGraph(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        route = MAIN_GRAPH_ROUTE,
        startDestination = BottomBarItem.Home.route,
    ) {
        composable(route = BottomBarItem.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(route = BottomBarItem.Inventory.route) {
            InventoryScreen()
        }
        composable(route = BottomBarItem.Adding.route) {
            AddingScreen(navController)
        }
        settingNavGraph(navController)
        composable(
            route = MENU_SCREEN_ROUTE,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("image") { type = NavType.StringType },
                navArgument("ingredients") { type = NavType.StringType },
                navArgument("calories") { type = NavType.FloatType },
                navArgument("totalTime") { type = NavType.FloatType },
                navArgument("mealType") { type = NavType.StringType },
                navArgument("dishType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name")
            val image = backStackEntry.arguments?.getString("image")
            val ingredientsString = backStackEntry.arguments?.getString("ingredients")
            val ingredients = ingredientsString?.split(";") ?: emptyList()
            val time = backStackEntry.arguments?.getFloat("totalTime") ?: 0f
            val calories = backStackEntry.arguments?.getFloat("calories") ?: 0f
            val mealType = backStackEntry.arguments?.getString("mealType")
            val dishType = backStackEntry.arguments?.getString("dishType")

            if (name != null && image != null) {
                MenuScreen(
                    name = name,
                    image = image,
                    ingredientList = ingredients,
                    time = time,
                    calories = calories,
                    mealType = mealType ?: "",
                    dishType = dishType ?: "",
                )
            }
        }
        composable(route = BACK_TO_APP_ROUTE) {
            AppNavHost()
        }

    }
}
