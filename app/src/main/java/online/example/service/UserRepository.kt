package online.example.service

import kotlinx.coroutines.flow.Flow
import online.example.model.User

/**
 * Interface defining the contract for user-related data operations.
 */
interface UserRepository {
    /**
     * Retrieves a list of users from the data source.
     *
     * @return A Flow emitting a [UsersResponse] which can be either a success with user data or a failure with an error message.
     */
    suspend fun getUsers(): Flow<UsersResponse>

    /**
     * Searches for users based on the provided query.
     *
     * @param query The search query.
     * @return A Flow emitting a list of users matching the search query.
     */
    suspend fun searchUsers(query: String): Flow<List<User>>
}