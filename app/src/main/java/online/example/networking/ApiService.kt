package online.example.networking

import online.example.model.User
import retrofit2.Response
import retrofit2.http.GET

/**
 * Interface defining the API endpoints for user-related operations.
 */
interface ApiService {
    /**
     * Fetches a list of users from the API.
     *
     * @return A [Response] object containing a list of [User] objects.
     */
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}