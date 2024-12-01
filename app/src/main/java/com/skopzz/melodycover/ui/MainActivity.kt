package com.skopzz.melodycover.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.skopzz.melodycover.preference.PreferenceManager
import com.skopzz.melodycover.ui.theme.MelodyCoverTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PreferenceManager.init(this)

        enableEdgeToEdge()
        setContent {
            MelodyCoverTheme {
                App(this)
            }
        }
    }
}

