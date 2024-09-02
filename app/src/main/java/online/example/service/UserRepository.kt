package online.example.service

import kotlinx.coroutines.flow.Flow

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
}