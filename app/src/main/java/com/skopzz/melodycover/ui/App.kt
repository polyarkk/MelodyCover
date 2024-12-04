package com.skopzz.melodycover.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.skopzz.melodycover.McContext
import com.skopzz.melodycover.ui.screen.ConfigEditScreen
import com.skopzz.melodycover.ui.screen.ConfigScreen
import com.skopzz.melodycover.ui.screen.MainScreen
import com.skopzz.melodycover.ui.screen.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
object MainRoute

@Serializable
object ConfigRoute

@Serializable
data class ConfigEditRoute(val key: String)

@Serializable
object SettingsRoute

@Composable
fun App() {
  val navController = rememberNavController()
  val mcCtx = McContext(LocalContext.current, navController);

  NavHost(
    navController,
    startDestination = MainRoute,
    enterTransition = {
      slideIntoContainer(
        animationSpec = tween(550, easing = EaseInOut),
        towards = AnimatedContentTransitionScope.SlideDirection.Start
      )
    },
    exitTransition = {
      fadeOut(
        animationSpec = tween(
          300, easing = EaseIn
        )
      )
    }
  ) {
    composable<MainRoute> { entry ->
      MainScreen(mcCtx)
    }

    composable<ConfigRoute> { entry ->
      ConfigScreen(mcCtx)
    }

    composable<ConfigEditRoute>() { entry ->
      ConfigEditScreen(mcCtx, entry.toRoute<ConfigEditRoute>().key)
    }

    composable<SettingsRoute> { entry ->
      SettingsScreen(mcCtx)
    }
  }
}

