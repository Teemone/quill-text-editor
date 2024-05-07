package com.yourandroidguy.quil

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.yourandroidguy.quil.ui.theme.QuilTheme
import com.yourandroidguy.quil.viewmodel.QuilViewModel
import com.yourandroidguy.quil.viewmodel.QuilViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: QuilViewModel by viewModels {
        QuilViewModelFactory((application as QuilApplication).database.noteDao())
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {

            App(viewModel = viewModel)
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AppPreview() {
    QuilTheme {
    }
}