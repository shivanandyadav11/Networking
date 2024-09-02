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

/**
 * Service class responsible for fetching user data from the API and mapping it to domain models.
 *
 * @property service The API service used to make network requests.
 */
class UserService @Inject constructor(private val service: ApiService) {

    /**
     * Fetches users from the API and transforms the response into a [UsersResponse].
     *
     * @return A Flow emitting a [UsersResponse] which can be either a success with user data or a failure with an error message.
     */
    suspend fun getUsers(): Flow<UsersResponse> {
        return flow {
            val result = service.getUsers()
            emit(handleResponse(result))
        }.catch { error ->
            emit(UsersResponse.Failure(error.message.orEmpty()))
        }
    }

    /**
     * Handles the API response and transforms it into a [UsersResponse].
     *
     * @param result The API response containing a list of users.
     * @return A [UsersResponse] based on the API response.
     */
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

    /**
     * Transforms a [User] object into a [UserDetail] object.
     *
     * @return A [UserDetail] object containing the user's name and email.
     */
    private fun User.toUserDetail(): UserDetail {
        return UserDetail(
            name = this.name,
            email = this.email
        )
    }
}

/**
 * Sealed class representing the possible responses when fetching users.
 */
sealed class UsersResponse {
    /**
     * Represents a successful response containing user information.
     *
     * @property userInfo The user information retrieved.
     */
    data class Success(val userInfo: UserInfo) : UsersResponse()

    /**
     * Represents a failure response containing an error message.
     *
     * @property message The error message describing the failure.
     */
    data class Failure(val message: String) : UsersResponse()
}