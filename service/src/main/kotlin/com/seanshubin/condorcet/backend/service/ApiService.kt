package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.database.StateDbCommands
import com.seanshubin.condorcet.backend.database.StateDbQueries
import com.seanshubin.condorcet.backend.database.UserRow
import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role

class ApiService(
    private val passwordUtil: PasswordUtil,
    private val stateDbCommands: StateDbCommands,
    private val stateDbQueries: StateDbQueries
) : Service {
    override fun addUser(name: String, email: String, password: String): ServiceResponse {
        if (nameExists(name)) {
            return ServiceResponse.Conflict("User with name '$name' already exists")
        }
        if (emailExists(email)) {
            return ServiceResponse.Conflict("User with email '$email' already exists")
        }
        val role = if (stateDbQueries.countUsers() == 0) {
            Role.OWNER
        } else {
            Role.UNASSIGNED
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        stateDbCommands.createUser(name, email, salt, hash, role)
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

    override fun setRole(authority: String, name: String, role: Role): ServiceResponse {
        val authorityRole = stateDbQueries.findUserByName(authority).role
        val canSetRole = stateDbQueries.roleHasPermission(authorityRole, Permission.MANAGE_USERS)
        if (!canSetRole) {
            return ServiceResponse.Unauthorized("User $authority with role $authorityRole is not allowed to set roles")
        }
        if (authorityRole.ordinal >= role.ordinal) {
            return ServiceResponse.Unauthorized("User $authority with role $authorityRole can not assign role $role, as that role is not lower in rank")
        }
        stateDbCommands.setRole(name, role)
        return ServiceResponse.GenericOk
    }

    override fun health(): ServiceResponse {
        return ServiceResponse.Health("ok")
    }

    override fun unsupported(name: String, text: String): ServiceResponse {
        val userSafeMessage = "Unsupported command '$name'"
        return ServiceResponse.Unsupported(userSafeMessage, name, text)
    }

    private fun nameExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun emailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
    private fun getRole(name: String): Role = TODO()
}
