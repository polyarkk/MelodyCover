package com.skopzz.melodycover.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.ui.screen.ConfigScreen
import com.skopzz.melodycover.ui.screen.MainScreen
import com.skopzz.melodycover.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object Main

@Serializable
object Config

@Serializable
object Settings

@Composable
fun App(ctx: Context) {
    val navController = rememberNavController()

    NavHost(navController, startDestination = Main) {
        composable<Main> { entry ->
            MainScreen(McContext(ctx, navController))
        }

        composable<Config> { entry ->
            ConfigScreen(McContext(ctx, navController))
        }

        composable<Settings> { entry ->
            SettingsScreen(McContext(ctx, navController))
        }
    }
}

