package online.example.model

import kotlinx.collections.immutable.ImmutableList

data class UserInfo(
    val userDetailImmutableList: ImmutableList<UserDetail>
)

data class UserDetail(
    val name: String,
    val email: String,
)

data class UserAddress(
    val street: String,
    val city: String,
)