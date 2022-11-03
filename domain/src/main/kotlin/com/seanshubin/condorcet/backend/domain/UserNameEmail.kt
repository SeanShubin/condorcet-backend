package com.seanshubin.condorcet.backend.domain

data class UserNameEmail(val name: String, val email: String) {
    companion object{
        fun User.toUserNameEmail():UserNameEmail = UserNameEmail(name, email)
    }
}
