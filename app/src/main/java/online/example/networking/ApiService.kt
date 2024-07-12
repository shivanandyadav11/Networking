package online.example.networking

import retrofit2.Call
import retrofit2.http.GET

// Code separation
//Retrofit uses Java's dynamic proxy mechanism to implement the interface
interface ApiService {
    @GET("users")
    fun getUsers(): Call<List<User>>
}