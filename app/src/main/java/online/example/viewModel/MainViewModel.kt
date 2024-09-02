package online.example.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import online.example.model.User
import online.example.service.UserRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private val _userResult = MutableStateFlow<Result>(Result.Loading)
    val userResult: Flow<Result> get() = _userResult

    fun makeApiCall() = viewModelScope.launch(Dispatchers.IO) {
        makeAPICall()
    }

    private suspend fun makeAPICall() {
        try {
            val result = userRepository.getUsers()
            if (result.isSuccessful) {
                result.body()?.let { users ->
                    _userResult.emit(Result.Success(users))
                } ?: run {
                    _userResult.emit(Result.Error("body is null"))
                }
            } else {
                _userResult.emit(Result.Error("Failed to retrieve users"))
            }
        } catch (ex: Exception) {
            _userResult.emit(Result.Error(ex.message.toString()))
        }
    }

    sealed class Result {
        data object Loading : Result()
        data class Success(val users: List<User>) : Result()
        data class Error(val message: String) : Result()
    }
}