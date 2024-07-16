package online.example.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import online.example.model.User
import online.example.networking.RetrofitInstance

class MainViewModel : ViewModel() {

    private val _apiResult = MutableLiveData<Result>()
    val apiResult: LiveData<Result> = _apiResult

    private val _userResult = MutableStateFlow<Result>(Result.NoState)
    val userResult: Flow<Result> get() = _userResult

    fun makeApiCall() = viewModelScope.launch(Dispatchers.IO) {
        makeAPICall()
    }

    private suspend fun makeAPICall() {
        val api = RetrofitInstance.api
        try {
            val result = api.getUsers()
            if (result.isSuccessful) {
                result.body()?.let { users ->
                    _userResult.emit(Result.Success(users))
                    _apiResult.postValue(Result.Success(users))
                } ?: run {
                    _userResult.emit(Result.Error("body is null"))
                    _apiResult.postValue(Result.Error("body is null"))
                }
            } else {
                _userResult.emit(Result.Error("Failed to retrieve users"))
                _apiResult.postValue(Result.Error("Failed to retrieve users"))
            }
        } catch (ex: Exception) {
            _userResult.emit(Result.Error(ex.message.toString()))
            _apiResult.postValue(Result.Error(ex.message.toString()))
        }
    }

    sealed class Result {
        data object NoState : Result()
        data class Success(val users: List<User>) : Result()
        data class Error(val message: String) : Result()
    }
}