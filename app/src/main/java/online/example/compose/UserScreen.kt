package online.example.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import online.example.viewModel.MainViewModel

@Composable
internal fun UserScreen(userResult: MainViewModel.Result) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (userResult) {
            is MainViewModel.Result.Loading -> {
                LoadingContainer()
            }

            is MainViewModel.Result.Error -> {
                ErrorContainer(message = userResult.message, onRetry = { /*TODO*/ })
            }

            is MainViewModel.Result.Success -> {
                SuccessContainer(users = userResult.users, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}