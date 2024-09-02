package online.example.handler

class UserHandler(
    val retry: () -> Unit = {}
)