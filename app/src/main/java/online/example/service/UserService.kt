package online.example.service

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import online.example.model.User
import online.example.model.UserDetail
import online.example.model.UserInfo
import online.example.networking.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserService @Inject constructor(private val service: ApiService) {

    suspend fun getUsers(): Flow<UsersResponse> {
        return flow {
            val result = service.getUsers()
            emit(handleResponse(result))
        }.catch { error ->
            emit(UsersResponse.Failure(error.message.orEmpty()))
        }
    }

    private fun handleResponse(result: Response<List<User>>): UsersResponse {
        return when {
            result.isSuccessful -> {
                val users = result.body()
                if (users != null) {
                    val userDetails = users.map { it.toUserDetail() }.toPersistentList()
                    UsersResponse.Success(UserInfo(userDetails))
                } else {
                    UsersResponse.Failure("User response body is null")
                }
            }

            else -> {
                val errorBody = result.errorBody()?.string()
                val errorMessage = if (!errorBody.isNullOrEmpty()) {
                    errorBody
                } else {
                    result.message() ?: "Unknown error occurred"
                }
                UsersResponse.Failure(errorMessage)
            }
        }
    }

    private fun User.toUserDetail(): UserDetail {
        return UserDetail(
            name = this.name,
            email = this.email
        )
    }
}

sealed class UsersResponse {
    data class Success(val userInfo: UserInfo) : UsersResponse()
    data class Failure(val message: String) : UsersResponse()
}
