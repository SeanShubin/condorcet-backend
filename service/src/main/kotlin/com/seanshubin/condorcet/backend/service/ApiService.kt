package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.database.StateDbCommands
import com.seanshubin.condorcet.backend.database.StateDbQueries
import com.seanshubin.condorcet.backend.database.UserRow
import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.UserNameRole

class ApiService(
    private val passwordUtil: PasswordUtil,
    private val stateDbCommands: StateDbCommands,
    private val stateDbQueries: StateDbQueries
) : Service {
    override fun refresh(refreshToken: RefreshToken): Tokens {
        val userRow = stateDbQueries.findUserByName(refreshToken.userName)
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }

    override fun register(name: String, email: String, password: String): Tokens {
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
        return createTokens(user)
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val userRow = mustMatchExistingNameOrEmail(nameOrEmail)
        mustMatchPassword(nameOrEmail, userRow, password)
        return createTokens(userRow)
    }

    override fun setRole(accessToken: AccessToken, target: String, role: Role) {
        val targetUserRow = mustMatchExistingName(target)
        val permissionNeeded = Permission.MANAGE_USERS
        mustHavePermission(accessToken, permissionNeeded)
        mustBeAllowedToChangeRoleTo(accessToken, targetUserRow, role)
        stateDbCommands.setRole(accessToken.userName, target, role)
    }

    override fun removeUser(accessToken: AccessToken, target: String) {
        val targetUserRow = mustMatchExistingName(target)
        mustHavePermission(accessToken, Permission.MANAGE_USERS)
        mustBeAllowedToRemoveUser(accessToken, targetUserRow)
        stateDbCommands.removeUser(accessToken.userName, target)
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        mustHavePermission(accessToken, Permission.MANAGE_USERS)
        val userRows = stateDbQueries.listUsers()
        val list = userRows.map { row ->
            val allowedRoles = Role.values().filter {
                allowedToChangeRoleTo(accessToken, row, it)
            }
            UserNameRole(row.name, row.role, allowedRoles)
        }
        return list
    }

    private fun allowedToChangeRoleTo(accessToken: AccessToken, userRow: UserRow, targetRole: Role): Boolean =
        if (accessToken.userName == userRow.name) {
            accessToken.role == targetRole
        } else {
            accessToken.role.ordinal < targetRole.ordinal
        }

    private fun allowedToRemoveUser(accessToken: AccessToken, userRow: UserRow): Boolean =
        if (accessToken.userName == userRow.name) {
            false
        } else {
            accessToken.role.ordinal < userRow.role.ordinal
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
            throw ServiceException.Conflict("User with name '$name' already exists")
        }
    }

    private fun mustNotConflictWithExistingEmail(email: String) {
        if (emailExists(email)) {
            throw ServiceException.Conflict("User with email '$email' already exists")
        }
    }

    private fun mustMatchExistingNameOrEmail(nameOrEmail: String): UserRow {
        val userRow = searchByNameOrEmail(nameOrEmail)
        if (userRow == null) {
            throw ServiceException.NotFound("User with name or email '$nameOrEmail' does not exist")
        } else {
            return userRow
        }
    }

    private fun mustMatchExistingName(name: String): UserRow {
        val userRow = searchByName(name)
        if (userRow == null) {
            throw ServiceException.NotFound("User with name '$name' does not exist")
        } else {
            return userRow
        }
    }

    private fun mustMatchPassword(nameOrEmail: String, userRow: UserRow, password: String) {
        if (!passwordUtil.validatePassword(password, userRow.salt, userRow.hash)) {
            throw ServiceException.Unauthorized("Authentication failed for user with name or email '$nameOrEmail'")
        }

    }

    private fun mustHavePermission(accessToken: AccessToken, permission: Permission) {
        if (!stateDbQueries.roleHasPermission(accessToken.role, permission)) {
            throw ServiceException.Unauthorized(
                "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permission"
            )
        }
    }

    private fun mustBeAllowedToChangeRoleTo(accessToken: AccessToken, userRow: UserRow, role: Role) {
        if (!allowedToChangeRoleTo(accessToken, userRow, role)) {
            throw ServiceException.Unauthorized(
                "User ${accessToken.userName} with role ${accessToken.role}" +
                        " is not allowed to change role of user ${userRow.name} from ${userRow.role} to $role"
            )
        }
    }

    private fun mustBeAllowedToRemoveUser(accessToken: AccessToken, userRow: UserRow) {
        if (!allowedToRemoveUser(accessToken, userRow)) {
            throw ServiceException.Unauthorized(
                "User ${accessToken.userName} with role ${accessToken.role}" +
                        " is not allowed to remove user ${userRow.name} with role ${userRow.role}"
            )
        }
    }

    private fun createTokens(userRow: UserRow): Tokens {
        val refreshToken = RefreshToken(userRow.name)
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }
}
