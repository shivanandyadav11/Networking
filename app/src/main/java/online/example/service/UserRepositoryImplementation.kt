package online.example.service

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of [UserRepository] that uses [UserService] to fetch user data.
 *
 * @property userService The service used to fetch user data.
 */
class UserRepositoryImplementation @Inject constructor(private val userService: UserService) :
    UserRepository {
    /**
     * Retrieves a list of users from the [UserService].
     *
     * @return A Flow emitting a [UsersResponse] which can be either a success with user data or a failure with an error message.
     */
    override suspend fun getUsers(): Flow<UsersResponse> {
        return userService.getUsers()
    }
}