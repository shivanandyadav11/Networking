package online.example.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import online.example.handler.UserHandler
import online.example.viewModel.MainViewModel
import online.example.viewModel.MainViewModel.UserViewState

@Composable
internal fun UserScreen(viewModel: MainViewModel = hiltViewModel()) {
    val dataFetched = rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        if (!dataFetched.value) {
            viewModel.fetchUserData()
            dataFetched.value = true
        }
    }

    val state = viewModel.userUiState.collectAsStateWithLifecycle().value

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        UserScreenImpl(
            state = state,
            userHandler = UserHandler(
                retry = {
                    viewModel.retryFetchingUserData()
                }
            ),
            modifier = Modifier.padding(innerPadding))
    }
}

@Composable
fun UserScreenImpl(
    state: UserViewState,
    userHandler: UserHandler,
    modifier: Modifier = Modifier
) {
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
                UserViewContainer(
                    state = state,
                    userHandler = userHandler,
                )
            }
        }
    }
}
