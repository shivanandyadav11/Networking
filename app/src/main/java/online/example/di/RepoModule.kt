package online.example.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import online.example.service.UserRepository
import online.example.service.UserRepositoryImplementation

@Module
@InstallIn(SingletonComponent::class)
interface RepoModule {

    @Binds
    fun bindWeatherRepo(userRepositoryImplementation: UserRepositoryImplementation): UserRepository
}
