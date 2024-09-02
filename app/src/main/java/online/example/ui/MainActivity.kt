package online.example.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dagger.hilt.android.AndroidEntryPoint
import online.example.compose.UserScreen
import online.example.ui.theme.ComposeTheme
import online.example.viewModel.MainViewModel
import online.example.viewModel.MainViewModel.Result

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val userResult by viewModel.userResult.collectAsState(initial = Result.Loading)
            ComposeTheme {
                UserScreen(userResult)
            }
        }
        viewModel.makeApiCall()
    }
}