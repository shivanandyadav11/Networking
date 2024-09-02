package online.example.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.example.viewModel.MainViewModel
import online.example.viewModel.MainViewModel.Result
import online.example.viewModel.MainViewModel.UserViewState

@Composable
internal fun UserScreen(viewModel: MainViewModel = hiltViewModel()) {

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchUserData()
    }

    val state = viewModel.userUiState.collectAsStateWithLifecycle().value

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        UserScreenImpl(state = state, modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun UserScreenImpl(state: UserViewState, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        when (state) {
            is UserViewState.Loading -> {
                LoadingContainer()
            }

            is UserViewState.UserView -> {
                SuccessContainer(
                    state = state,
                )
            }
            is UserViewState.Error -> {
                ErrorContainer(
                    onRetry = { /*TODO*/ }
                )
            }
        }
    }
}
