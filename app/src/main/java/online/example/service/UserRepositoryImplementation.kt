package online.example.service

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImplementation @Inject constructor(private val userService: UserService) : UserRepository {
    override suspend fun getUsers(): Flow<UsersResponse> {
        return userService.getUsers()
    }
}