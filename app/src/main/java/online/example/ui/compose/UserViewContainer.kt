package online.example.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import online.example.handler.UserHandler
import online.example.viewModel.MainViewModel.UserViewState

@Composable
fun UserViewContainer(
    state: UserViewState.UserView,
    userHandler: UserHandler,
) {
    if (state.isLoading) {
        LoadingContainer()
    } else if (state.errorState.isErrorState) {
        ErrorContainer(userHandler = userHandler)
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = state.header.label,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                state.userInfo?.let { userInfo ->
                    items(items = userInfo.userDetailImmutableList) { user ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = user.name)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}