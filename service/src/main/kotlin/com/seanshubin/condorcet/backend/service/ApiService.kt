package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.database.StateDbCommands
import com.seanshubin.condorcet.backend.database.StateDbQueries
import com.seanshubin.condorcet.backend.database.UserRow
import com.seanshubin.condorcet.backend.domain.Permission
import com.seanshubin.condorcet.backend.domain.Permission.MANAGE_USERS
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.domain.Role.OWNER
import com.seanshubin.condorcet.backend.domain.Role.UNASSIGNED
import com.seanshubin.condorcet.backend.domain.UserNameRole
import com.seanshubin.condorcet.backend.service.ServiceException.Category.*

class ApiService(
    private val passwordUtil: PasswordUtil,
    private val stateDbCommands: StateDbCommands,
    private val stateDbQueries: StateDbQueries
) : Service {
    override fun refresh(refreshToken: RefreshToken): Tokens {
        val userRow = searchUserByName(refreshToken.userName)
        failIf(userRow == null, NOT_FOUND, "User with name '${refreshToken.userName}' not found")
        userRow!!
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }

    override fun register(rawName: String, email: String, password: String): Tokens {
        val name = collapseWhitespace(rawName)
        failIf(name.isBlank(), UNSUPPORTED, "User name must not be blank")
        failIf(nameExists(name), CONFLICT, "User with name '$name' already exists")
        failIf(emailExists(email), CONFLICT, "User with email '$email' already exists")
        val role = if (stateDbQueries.countUsers() == 0) {
            OWNER
        } else {
            UNASSIGNED
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        val authority = name
        stateDbCommands.createUser(authority, name, email, salt, hash, role)
        val user = stateDbQueries.findUserByName(name)
        return createTokens(user)
    }

    override fun authenticate(nameOrEmail: String, password: String): Tokens {
        val userRow = searchUserByNameOrEmail(nameOrEmail)
        failIf(userRow == null, NOT_FOUND, "User with name or email '$nameOrEmail' does not exist")
        userRow!!
        val passwordMatches = passwordUtil.passwordMatches(password, userRow.salt, userRow.hash)
        failUnless(passwordMatches, UNAUTHORIZED, "Authentication failed for user with name or email '$nameOrEmail'")
        return createTokens(userRow)
    }

    override fun setRole(accessToken: AccessToken, name: String, role: Role) {
        val userRow = searchUserByName(name)
        failIf(userRow == null, NOT_FOUND, "User with name '$name' does not exist")
        userRow!!
        val permissionNeeded = MANAGE_USERS
        failUnless(
            hasPermission(accessToken, permissionNeeded), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permissionNeeded"
        )
        val isChangingSelfRole = isSelf(accessToken, userRow) && accessToken.role != role
        failIf(isChangingSelfRole, UNAUTHORIZED, "Not allowed to change role for self")
        failUnless(roleIsGreater(accessToken, userRow), UNAUTHORIZED, "Must have greater role than target")
        failUnless(roleIsGreater(accessToken, role), UNAUTHORIZED, "Not allowed to set roles greater or equal to self")
        stateDbCommands.setRole(accessToken.userName, name, role)
    }

    override fun removeUser(accessToken: AccessToken, name: String) {
        val userRow = searchUserByName(name)
        failIf(userRow == null, NOT_FOUND, "User with name '$name' does not exist")
        userRow!!
        val permissionNeeded = MANAGE_USERS
        failUnless(
            hasPermission(accessToken, permissionNeeded), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permissionNeeded"
        )
        failIf(
            isSelf(accessToken, userRow) && stateDbQueries.countUsers() > 1, UNAUTHORIZED,
            "Not allowed to remove self unless you are the only user"
        )
        failUnless(roleIsGreater(accessToken, userRow), UNAUTHORIZED, "Must have greater role than target")
        stateDbCommands.removeUser(accessToken.userName, name)
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        val permissionNeeded = MANAGE_USERS
        failUnless(
            hasPermission(accessToken, permissionNeeded), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permissionNeeded"
        )
        val userRows = stateDbQueries.listUsers()
        val list = userRows.map { row ->
            val allowedRoles = Role.values().filter {
                allowedToChangeRoleTo(accessToken, row, it)
            }
            UserNameRole(row.name, row.role, allowedRoles)
        }
        return list
    }

    private fun nameExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun emailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
    private fun roleIsGreater(accessToken: AccessToken, userRow: UserRow): Boolean =
        roleIsGreater(accessToken.role, userRow.role)

    private fun roleIsGreater(accessToken: AccessToken, role: Role): Boolean = roleIsGreater(accessToken.role, role)
    private fun roleIsGreater(first: Role, second: Role): Boolean = first.ordinal < second.ordinal
    private fun hasPermission(role: Role, permission: Permission): Boolean =
        stateDbQueries.roleHasPermission(role, permission)

    private fun hasPermission(accessToken: AccessToken, permission: Permission): Boolean =
        hasPermission(accessToken.role, permission)

    private fun isSelf(accessToken: AccessToken, userRow: UserRow): Boolean = accessToken.userName == userRow.name

    private fun failIf(shouldFail: Boolean, category: ServiceException.Category, message: String) {
        if (shouldFail) throw ServiceException(category, message)
    }

    private fun failUnless(shouldNotFail: Boolean, category: ServiceException.Category, message: String) {
        failIf(!shouldNotFail, category, message)
    }

    private fun allowedToChangeRoleTo(accessToken: AccessToken, userRow: UserRow, role: Role): Boolean {
        if (userRow.role == role) return true
        if (!hasPermission(accessToken, MANAGE_USERS)) return false
        if (isSelf(accessToken, userRow)) return false
        if (!roleIsGreater(accessToken, userRow)) return false
        if (!roleIsGreater(accessToken, role)) return false
        return true
    }

    private fun searchUserByName(name: String): UserRow? = stateDbQueries.searchUserByName(name)
    private fun searchUserByEmail(email: String): UserRow? = stateDbQueries.searchUserByEmail(email)
    private fun searchUserByNameOrEmail(nameOrEmail: String): UserRow? {
        val byName = searchUserByName(nameOrEmail)
        if (byName != null) return byName
        val byEmail = searchUserByEmail(nameOrEmail)
        return byEmail
    }

    private fun createTokens(userRow: UserRow): Tokens {
        val refreshToken = RefreshToken(userRow.name)
        val accessToken = AccessToken(userRow.name, userRow.role)
        return Tokens(refreshToken, accessToken)
    }

    private fun collapseWhitespace(s: String): String = s.trim().replace(whitespaceBlock, " ")

    companion object {
        val whitespaceBlock = Regex("""\s+""")
    }
}
