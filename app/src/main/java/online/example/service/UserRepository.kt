package online.example.service

import kotlinx.coroutines.flow.Flow
import online.example.model.User
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(): Flow<UsersResponse>
}