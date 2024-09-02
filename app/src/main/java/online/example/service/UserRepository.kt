package online.example.service

import online.example.model.User
import retrofit2.Response

interface UserRepository {
    suspend fun getUsers(): Response<List<User>>
}