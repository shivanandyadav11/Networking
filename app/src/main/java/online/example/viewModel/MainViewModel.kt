package online.example.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import online.example.model.User
import online.example.networking.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _apiResult = MutableLiveData<Result>()
    val apiResult: LiveData<Result> = _apiResult

    fun makeApiCall() {
        val api = RetrofitInstance.api
        api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (response.isSuccessful) {
                    response.body()?.let { users ->
                        for (user in users) {
                            _apiResult.value = Result.Success(user)
                        }
                    }
                } else {
                    _apiResult.value = Result.Error("Failed to retrieve users")
//                    Toast.makeText(
//                        this@MainActivity,
//                        "User: ${user.name}, Email: ${user.email}",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Failed to retrieve users: ${response.errorBody()?.string()}",
//                        Toast.LENGTH_LONG
//                    ).show()
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _apiResult.value = Result.Error("Failed to retrieve users")
//                Toast.makeText(
//                    this@MainActivity,
//                    "Error fetching users: ${t.message}",
//                    Toast.LENGTH_LONG
//                ).show()
            }
        })
    }

    sealed class Result {
        data class Success(val user: User) : Result()
        data class Error(val message: String) : Result()
    }
}