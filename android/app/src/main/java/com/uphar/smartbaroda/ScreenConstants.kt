package com.uphar.smartbaroda

import androidx.annotation.DrawableRes

sealed class ScreenConstants(val route: String, @DrawableRes val iconResId: Int, val title: String) {
    data object Dashboard : ScreenConstants("dashboard", R.drawable.graph_svgrepo_com, "Dashboard")
    data object AIPrompt : ScreenConstants("ai_prompt", R.drawable.light_bulb_svgrepo_com, "AI Prompt")
    data object Profile : ScreenConstants("profile", R.drawable.profile_svgrepo_com, "Profile")
    data object Login : ScreenConstants("login", 0, "Login")
}

