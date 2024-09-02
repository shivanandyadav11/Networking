package online.example.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import online.example.model.User
import online.example.networking.ApiService
import retrofit2.Response
import javax.inject.Inject

class UserRepositoryImplementation @Inject constructor(val api: ApiService) : UserRepository {
    override suspend fun getUsers(): Response<List<User>> {
        return withContext(Dispatchers.IO) {
            api.getUsers()
        }
    }
}