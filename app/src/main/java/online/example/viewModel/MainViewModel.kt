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
import online.example.model.UserErrorState
import online.example.model.UserInfo
import online.example.service.UserRepository
import online.example.service.UsersResponse
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val userMutableUiState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    internal val userUiState: StateFlow<UserViewState> = userMutableUiState.asStateFlow()

    fun fetchUserData() = viewModelScope.launch {
        val result = userRepository.getUsers().first()
        updateUiState(
            when (result) {
                is UsersResponse.Success -> {
                    UserViewState.UserView(
                        header = Header(
                            label = "Networking",
                            description = "Networking desc"
                        ),
                        userInfo = result.userInfo,
                        errorState = UserErrorState(
                            isErrorState = false,
                            errorString = ""
                        )
                    )
                }

                is UsersResponse.Failure -> {
                    UserViewState.UserView(
                        header = Header(
                            label = "Networking - Error",
                            description = "Networking desc error"
                        ),
                        userInfo = null,
                        errorState = UserErrorState(
                            isErrorState = true,
                            errorString = result.message
                        )
                    )
                }
            }
        )
    }

    fun retryFetchingUserData() = viewModelScope.launch {
        val currentState = userMutableUiState.value
        if (currentState is UserViewState.UserView) {
            updateUiState(currentState.copy(
                isLoading = true
            ))
            when (val result = userRepository.getUsers().first()) {
                is UsersResponse.Success -> {
                    updateUiState(
                        currentState.copy(
                            header = Header(
                                label = "Networking",
                                description = "Networking desc"
                            ),
                            userInfo = result.userInfo,
                            errorState = UserErrorState(isErrorState = false, errorString = "")
                        )
                    )
                }

                is UsersResponse.Failure -> {
                    updateUiState(
                        currentState.copy(
                            errorState = UserErrorState(
                                isErrorState = true,
                                errorString = result.message
                            )
                        )
                    )
                }
            }
        }
    }

    private fun updateUiState(newState: UserViewState) {
        userMutableUiState.update { newState }
    }

    @Stable
    sealed class UserViewState {
        data object Loading : UserViewState()
        data class UserView(
            val header: Header,
            val userInfo: UserInfo? = null,
            val errorState: UserErrorState,
            val isLoading: Boolean = false
        ) : UserViewState()
    }
}