package com.ebc.muffinshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.ebc.muffinshop.ui.EmiDiomasApp
import com.ebc.muffinshop.ui.theme.MuffinShopTheme

// inicio de la app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MuffinShopTheme {
                EmiDiomasApp()
            }
        }
    }
}
