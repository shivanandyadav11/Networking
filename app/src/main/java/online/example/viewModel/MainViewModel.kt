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

/**
 * ViewModel responsible for managing the UI state and business logic for the main screen.
 *
 * @property userRepository The repository used to fetch user data.
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val userMutableUiState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    internal val userUiState: StateFlow<UserViewState> = userMutableUiState.asStateFlow()

    /**
     * Fetches user data and updates the UI state accordingly.
     */
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

    /**
     * Retries fetching user data in case of a previous failure.
     */
    fun retryFetchingUserData() = viewModelScope.launch {
        val currentState = userMutableUiState.value
        if (currentState is UserViewState.UserView) {
            updateUiState(
                currentState.copy(
                    isLoading = true
                )
            )
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

    /**
     * Updates the UI state with a new state.
     *
     * @param newState The new state to be set.
     */
    private fun updateUiState(newState: UserViewState) {
        userMutableUiState.update { newState }
    }

    /**
     * Sealed class representing the possible UI states for the main screen.
     */
    @Stable
    sealed class UserViewState {
        /**
         * Represents the loading state when fetching user data.
         */
        data object Loading : UserViewState()

        /**
         * Represents the state when user data is available or an error has occurred.
         *
         * @property header The header information for the screen.
         * @property userInfo The user information, if available.
         * @property errorState The error state, if an error has occurred.
         * @property isLoading Indicates whether the view is currently loading data.
         */
        data class UserView(
            val header: Header,
            val userInfo: UserInfo? = null,
            val errorState: UserErrorState,
            val isLoading: Boolean = false
        ) : UserViewState()
    }
}