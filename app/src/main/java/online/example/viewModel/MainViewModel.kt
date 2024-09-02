package online.example.viewModel

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import online.example.model.Header
import online.example.model.User
import online.example.model.UserInfo
import online.example.service.UserRepository
import online.example.service.UsersResponse
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val userMutableUiState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    internal val userUiState: StateFlow<UserViewState> = userMutableUiState.asStateFlow()

    suspend fun fetchUserData() = viewModelScope.launch {
        val result = userRepository.getUsers().first()
        updateUiState(
            when (result) {
                is UsersResponse.Success -> {
                        UserViewState.UserView(
                            header = Header(
                                label = "Networking",
                                description = "Networking desc"
                            ),
                            userInfo = result.userInfo
                        )
                }

                is UsersResponse.Failure -> {
                    UserViewState.Error
                }
            }
        )
    }

    private fun updateUiState(newState: UserViewState) {
        userMutableUiState.update { newState }
    }

    sealed class Result {
        data object Loading : Result()
        data class Success(val users: List<User>) : Result()
        data class Error(val message: String) : Result()
    }

    @Stable
    sealed class UserViewState {
        data object Loading : UserViewState()
        data class UserView(
            val header: Header,
            val userInfo: UserInfo,
        ) : UserViewState()

        data object Error : UserViewState()
    }
}