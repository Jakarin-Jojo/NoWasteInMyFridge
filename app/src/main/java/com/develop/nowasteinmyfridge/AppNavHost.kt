package com.develop.nowasteinmyfridge

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

const val APP_GRAPH_ROUTE = "app"

@Composable
fun AppNavHost(
    startDestination: String = AUTH_GRAPH_ROUTE,
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        route = APP_GRAPH_ROUTE,
    ) {
        authNavGraph(navController)
        composable(route = MAIN_GRAPH_ROUTE) {
            MainScreen()
        }
    }
}


