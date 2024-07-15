package online.example.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import online.example.model.User
import online.example.networking.RetrofitInstance

class MainViewModel : ViewModel() {

    private val _apiResult = MutableLiveData<Result>()
    val apiResult: LiveData<Result> = _apiResult

    fun makeApiCall() = viewModelScope.launch(Dispatchers.IO) {
        makeAPICall()
    }

    private suspend fun makeAPICall() {
        val api = RetrofitInstance.api
        try {
            val result = api.getUsers()
            if (result.isSuccessful) {
                result.body()?.let { users ->
                    for (user in users) {
                        _apiResult.postValue(Result.Success(user))
                    }
                } ?:  _apiResult.postValue(Result.Error("body is null"))
            } else {
                _apiResult.postValue(Result.Error("Failed to retrieve users"))
            }
        } catch (ex: Exception) {
            _apiResult.postValue(Result.Error(ex.message.toString()))
        }
    }

    sealed class Result {
        data class Success(val user: User) : Result()
        data class Error(val message: String) : Result()
    }
}