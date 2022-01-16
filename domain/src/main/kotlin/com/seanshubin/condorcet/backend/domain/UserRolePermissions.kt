package com.seanshubin.condorcet.backend.domain

import arrow.core.Either
import arrow.core.Either.Right
import arrow.core.Either.Left
import com.seanshubin.condorcet.backend.domain.Role.OWNER
import com.seanshubin.condorcet.backend.domain.Permission.MANAGE_USERS
import com.seanshubin.condorcet.backend.domain.Permission.TRANSFER_OWNER

data class UserRolePermissions(val userName:String, val role:Role, val permissions:List<Permission>) {
    fun toUserRole():UserRole = UserRole(userName, role)
    fun canChangeRole(target:UserRole, newRole:Role): Either<String, Unit> {
        if(!permissions.contains(MANAGE_USERS)) return Left("must have $MANAGE_USERS permission")
        if(userName == target.userName) return Left("may not change role of self")
        if(permissions.contains(TRANSFER_OWNER) && role == OWNER && newRole == OWNER) return Right(Unit)
        if(role <= newRole) return Left("may only assign lessor roles")
        if(role <= target.role) return Left("may only modify users with lessor roles")
        return Right(Unit)
    }

    fun listedRolesFor(target:UserRole):List<Role> =
        Role.values().filter {
            val isCurrentRole = it == target.role
            val canChangeRole = canChangeRole(target, it).isRight()
            val accept = isCurrentRole || canChangeRole
            accept
        }
}
