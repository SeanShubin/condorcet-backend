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
    override fun register(name: String, email: String, password: String): ServiceResponse {
        mustNotConflictWithExistingName(name)
        mustNotConflictWithExistingEmail(email)
        val role = if (stateDbQueries.countUsers() == 0) {
            Role.OWNER
        } else {
            Role.UNASSIGNED
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        val authority = name
        stateDbCommands.createUser(authority, name, email, salt, hash, role)
        val user = stateDbQueries.findUserByName(name)
        return ServiceResponse.UserName(user.name)
    }

    override fun authenticate(nameOrEmail: String, password: String): ServiceResponse {
        val userRow = mustMatchExistingNameOrEmail(nameOrEmail)
        mustMatchPassword(nameOrEmail, userRow, password)
        return ServiceResponse.UserName(userRow.name)
    }

    override fun setRole(authority: String, target: String, role: Role): ServiceResponse {
        val authorityUserRow = mustMatchExistingName(authority)
        val targetUserRow = mustMatchExistingName(target)
        val permissionNeeded = Permission.MANAGE_USERS
        mustHavePermission(authorityUserRow, permissionNeeded)
        mustHaveGreaterRole(authorityUserRow, targetUserRow, "setRole")
        stateDbCommands.setRole(authority, target, role)
        return ServiceResponse.GenericOk
    }

    override fun removeUser(authority: String, target: String): ServiceResponse {
        val authorityUserRow = mustMatchExistingName(authority)
        val targetUserRow = mustMatchExistingName(target)
        mustHavePermission(authorityUserRow, Permission.MANAGE_USERS)
        mustHaveGreaterRole(authorityUserRow, targetUserRow, "removeUser")
        stateDbCommands.removeUser(authority, target)
        return ServiceResponse.GenericOk
    }

    override fun listUsers(authority: String): ServiceResponse {
        val authorityUserRow = mustMatchExistingName(authority)
        mustHavePermission(authorityUserRow, Permission.MANAGE_USERS)
        val userRows = stateDbQueries.listUsers()
        val list = userRows.map { row ->
            ServiceResponse.UserNameRole(row.name, row.role)
        }
        return ServiceResponse.UserList(list)
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
    private fun searchByName(name: String): UserRow? = stateDbQueries.searchUserByName(name)
    private fun searchByEmail(email: String): UserRow? = stateDbQueries.searchUserByEmail(email)
    private fun searchByNameOrEmail(nameOrEmail: String): UserRow? {
        val byName = searchByName(nameOrEmail)
        if (byName != null) return byName
        val byEmail = searchByEmail(nameOrEmail)
        return byEmail
    }

    private fun mustNotConflictWithExistingName(name: String) {
        if (nameExists(name)) {
            throw ServiceException(ServiceResponse.Conflict("User with name '$name' already exists"))
        }
    }

    private fun mustNotConflictWithExistingEmail(email: String) {
        if (emailExists(email)) {
            throw ServiceException(ServiceResponse.Conflict("User with email '$email' already exists"))
        }
    }

    private fun mustMatchExistingNameOrEmail(nameOrEmail: String): UserRow {
        val userRow = searchByNameOrEmail(nameOrEmail)
        if (userRow == null) {
            throw ServiceException(ServiceResponse.NotFound("User with name or email '$nameOrEmail' does not exist"))
        } else {
            return userRow
        }
    }

    private fun mustMatchExistingName(name: String): UserRow {
        val userRow = searchByName(name)
        if (userRow == null) {
            throw ServiceException(ServiceResponse.NotFound("User with name '$name' does not exist"))
        } else {
            return userRow
        }
    }

    private fun mustMatchPassword(nameOrEmail: String, userRow: UserRow, password: String) {
        if (!passwordUtil.validatePassword(password, userRow.salt, userRow.hash)) {
            throw ServiceException(
                ServiceResponse.Unauthorized(
                    "Authentication failed for user with name or email '$nameOrEmail'"
                )
            )
        }

    }

    private fun mustHavePermission(userRow: UserRow, permission: Permission) {
        if (!stateDbQueries.roleHasPermission(userRow.role, permission)) {
            throw ServiceException(
                ServiceResponse.Unauthorized(
                    "User ${userRow.name} with role ${userRow.role} does not have permission $permission"
                )
            )
        }
    }

    private fun mustHaveGreaterRole(first: UserRow, second: UserRow, operation: String) {
        if (first.role.ordinal >= second.role.ordinal) {
            throw ServiceException(
                ServiceResponse.Unauthorized(
                    "For operation $operation, user ${first.name} with role ${first.role}" +
                            " must have greater role than user ${second.name} with role ${second.role}"
                )
            )
        }
    }
}
