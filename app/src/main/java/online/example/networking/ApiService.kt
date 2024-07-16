package online.example.networking

import online.example.model.User
import retrofit2.Response
import retrofit2.http.GET

// Code separation
//Retrofit uses Java's dynamic proxy mechanism to implement the interface
interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<User>>
}