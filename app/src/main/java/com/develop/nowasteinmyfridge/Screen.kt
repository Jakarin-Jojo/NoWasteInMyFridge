package com.develop.nowasteinmyfridge

sealed class Screen(val route: String) {
    data object LoginScreenRoute : Screen("login_screen_route")
    data object SignUpScreenRoute : Screen("signup_screen_route")
}