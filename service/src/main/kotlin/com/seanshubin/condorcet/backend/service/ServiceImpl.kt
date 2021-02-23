package com.seanshubin.condorcet.backend.service

import com.seanshubin.condorcet.backend.crypto.PasswordUtil
import com.seanshubin.condorcet.backend.database.*
import com.seanshubin.condorcet.backend.domain.*
import com.seanshubin.condorcet.backend.domain.Permission.*
import com.seanshubin.condorcet.backend.domain.Role.OWNER
import com.seanshubin.condorcet.backend.domain.Role.UNASSIGNED
import com.seanshubin.condorcet.backend.genericdb.GenericTable
import com.seanshubin.condorcet.backend.service.DataTransfer.toDomain
import com.seanshubin.condorcet.backend.service.ServiceException.Category.*

class ServiceImpl(
    private val passwordUtil: PasswordUtil,
    private val eventDbQueries: EventDbQueries,
    private val stateDbQueries: StateDbQueries,
    private val stateDbCommands: StateDbCommands,
    private val synchronizer: Synchronizer
) : Service {
    override fun synchronize() {
        synchronizer.synchronize()
    }

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
        failIf(userNameExists(name), CONFLICT, "User with name '$name' already exists")
        failIf(userEmailExists(email), CONFLICT, "User with email '$email' already exists")
        val role = if (stateDbQueries.userCount() == 0) {
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
        failUnlessPermission(accessToken, MANAGE_USERS)
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
        failUnlessPermission(accessToken, MANAGE_USERS)
        failIf(
            isSelf(accessToken, userRow) && stateDbQueries.tableCount() > 1, UNAUTHORIZED,
            "Not allowed to remove self unless you are the only user"
        )
        failUnless(roleIsGreater(accessToken, userRow), UNAUTHORIZED, "Must have greater role than target")
        stateDbCommands.removeUser(accessToken.userName, name)
    }

    override fun listUsers(accessToken: AccessToken): List<UserNameRole> {
        failUnlessPermission(accessToken, MANAGE_USERS)
        val userRows = stateDbQueries.listUsers()
        val list = userRows.map { row ->
            val allowedRoles = Role.values().filter {
                allowedToChangeRoleTo(accessToken, row, it)
            }
            UserNameRole(row.name, row.role, allowedRoles)
        }
        return list
    }

    override fun addElection(accessToken: AccessToken, name: String) {
        failUnlessPermission(accessToken, USE_APPLICATION)
        failIf(electionNameExists(name), CONFLICT, "Election with name '$name' already exists")
        stateDbCommands.addElection(accessToken.userName, accessToken.userName, name)
    }

    override fun listElections(accessToken: AccessToken): List<Election> {
        failUnlessPermission(accessToken, USE_APPLICATION)
        val rows = stateDbQueries.listElections()
        val list = rows.map { it.toDomain() }
        return list
    }

    override fun listTables(accessToken: AccessToken): List<String> {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        return stateDbQueries.tableNames(StateSchema)
    }

    override fun userCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.userCount()
    }

    override fun electionCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.electionCount()
    }

    override fun tableCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return stateDbQueries.tableCount()
    }

    override fun eventCount(accessToken: AccessToken): Int {
        failUnlessPermission(accessToken, VIEW_APPLICATION)
        return eventDbQueries.eventCount()
    }

    override fun tableData(accessToken: AccessToken, name: String): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.tableData(StateSchema, name)
        return genericTable.toTableData()
    }

    override fun debugTableData(accessToken: AccessToken, name: String): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = stateDbQueries.debugTableData(StateSchema, name)
        return genericTable.toTableData()
    }

    override fun eventData(accessToken: AccessToken): TableData {
        failUnlessPermission(accessToken, VIEW_SECRETS)
        val genericTable = eventDbQueries.tableData(EventSchema, "event")
        return genericTable.toTableData()
    }

    private fun userNameExists(name: String): Boolean = stateDbQueries.searchUserByName(name) != null
    private fun userEmailExists(email: String): Boolean = stateDbQueries.searchUserByEmail(email) != null
    private fun electionNameExists(name: String): Boolean = stateDbQueries.searchElectionByName(name) != null
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

    private fun failUnlessPermission(accessToken: AccessToken, permission: Permission) {
        failUnless(
            hasPermission(accessToken, permission), UNAUTHORIZED,
            "User ${accessToken.userName} with role ${accessToken.role} does not have permission $permission"
        )

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
    private fun GenericTable.toTableData(): TableData {
        val columnNames = this.columnNames
        val rows = this.rows
        return TableData(columnNames, rows)
    }

    companion object {
        val whitespaceBlock = Regex("""\s+""")
    }
}
