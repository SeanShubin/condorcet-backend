package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.genericdb.StateDbCommands
import com.seanshubin.condorcet.backend.genericdb.StateDbQueries
import com.seanshubin.condorcet.backend.genericdb.UserRow

class ApiService(
    private val passwordUtil: PasswordUtil,
    private val stateDbCommands: StateDbCommands,
    private val stateDbQueries: StateDbQueries
) : Service {
    override fun health(): ServiceResponse {
        return ServiceResponse.Health("ok")
    }

    override fun unsupported(name: String, text: String): ServiceResponse {
        val userSafeMessage = "Unsupported command '$name'"
        return ServiceResponse.Unsupported(userSafeMessage, name, text)
    }

    override fun addUser(name: String, email: String, password: String): ServiceResponse {
        if (nameExists(name)) {
            return ServiceResponse.Conflict("User with name '$name' already exists")
        }
        if (emailExists(email)) {
            return ServiceResponse.Conflict("User with email '$email' already exists")
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        stateDbCommands.createUser(name, email, salt, hash)
        val user = stateDbQueries.findUserByName(name)
        return ServiceResponse.UserName(user.name)
    }

    override fun authenticate(nameOrEmail: String, password: String): ServiceResponse {
        var user: UserRow? = null
        if (user == null) {
            user = stateDbQueries.searchUserByName(nameOrEmail)
        }
        if (user == null) {
            user = stateDbQueries.searchUserByEmail(nameOrEmail)
        }
        if (user == null) {
            return ServiceResponse.NotFound("User with name or email '$nameOrEmail' does not exist")
        }
        if (passwordUtil.validatePassword(password, user.salt, user.hash)) {
            return ServiceResponse.UserName(user.name)
        } else {
            return ServiceResponse.Unauthorized("Authentication failed for user with name or email '$nameOrEmail'")
        }
    }

    private fun nameExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun emailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
}
