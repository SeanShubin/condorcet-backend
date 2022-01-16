package com.seanshubin.condorcet.backend.domain

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import kotlin.test.Test
import com.seanshubin.condorcet.backend.domain.Role.OWNER
import com.seanshubin.condorcet.backend.domain.Role.ADMIN
import com.seanshubin.condorcet.backend.domain.Role.AUDITOR
import com.seanshubin.condorcet.backend.domain.Role.USER
import com.seanshubin.condorcet.backend.domain.Role.OBSERVER
import com.seanshubin.condorcet.backend.domain.Role.UNASSIGNED
import kotlin.test.assertEquals

class UserRolePermissionsTest {
    val allPermissions = Permission.values().toList()
    val allPermissionsExceptManageUsers = allPermissions.filterNot { it == Permission.MANAGE_USERS }
    @Test
    fun typicalCase() {
        val someOwner = UserRolePermissions("owner", OWNER, allPermissions)
        val someAuditor = UserRolePermissions("auditor", AUDITOR, allPermissions)
        val someAdmin = UserRolePermissions("admin", ADMIN, allPermissions)
        val someUser = UserRole("user", USER)
        val someObserver = UserRole("observer", OBSERVER)
        val someUnassigned= UserRole("unassigned", UNASSIGNED)
        verifyCanChangeRoleTo(someOwner, someUser, AUDITOR, Right(Unit))
        verifyCanChangeRoleTo(someAuditor, someObserver, UNASSIGNED, Right(Unit))
        verifyCanChangeRoleTo(someAdmin, someUnassigned, USER, Right(Unit))
    }

    @Test
    fun transferOwnership() {
        val someOwner = UserRolePermissions("owner", OWNER, allPermissions)
        val someUser = UserRole("user", USER)
        verifyCanChangeRoleTo(someOwner, someUser, OWNER, Right(Unit))
    }

    @Test
    fun canNotChangeSelfRole() {
        val someOwner = UserRolePermissions("owner", OWNER, allPermissions)
        val someAdmin= UserRolePermissions("admin", ADMIN, allPermissions)
        verifyCanChangeRoleTo(someOwner, someOwner.toUserRole(), OWNER, Left("may not change role of self"))
        verifyCanChangeRoleTo(someOwner, someOwner.toUserRole(), ADMIN, Left("may not change role of self"))
        verifyCanChangeRoleTo(someAdmin, someAdmin.toUserRole(), OWNER, Left("may not change role of self"))
        verifyCanChangeRoleTo(someAdmin, someAdmin.toUserRole(), ADMIN, Left("may not change role of self"))
    }

    @Test
    fun mayOnlyAssignLessorRoles() {
        val someAdmin= UserRolePermissions("admin", ADMIN, allPermissions)
        val someUser = UserRole("user", USER)
        verifyCanChangeRoleTo(someAdmin, someUser, ADMIN, Left("may only assign lessor roles"))
        verifyCanChangeRoleTo(someAdmin, someUser, AUDITOR, Left("may only assign lessor roles"))
    }

    @Test
    fun mayOnlyAssignToUsersWithLessorRoles() {
        val someAdmin= UserRolePermissions("admin", ADMIN, allPermissions)
        val someAuditor = UserRole("auditor", AUDITOR)
        val otherAdmin = UserRole("other admin", ADMIN)
        verifyCanChangeRoleTo(someAdmin, someAuditor, USER, Left("may only modify users with lessor roles"))
        verifyCanChangeRoleTo(someAdmin, otherAdmin, OBSERVER, Left("may only modify users with lessor roles"))
    }

    @Test
    fun requiresManageUsersPermission() {
        val someOwner = UserRolePermissions("owner", OWNER, allPermissionsExceptManageUsers)
        val someAuditor = UserRolePermissions("auditor", AUDITOR, allPermissionsExceptManageUsers)
        val someAdmin = UserRolePermissions("admin", ADMIN, allPermissionsExceptManageUsers)
        val someUser = UserRole("user", USER)
        val someObserver = UserRole("observer", OBSERVER)
        val someUnassigned= UserRole("unassigned", UNASSIGNED)
        verifyCanChangeRoleTo(someOwner, someUser, AUDITOR, Left("must have MANAGE_USERS permission"))
        verifyCanChangeRoleTo(someAuditor, someObserver, UNASSIGNED, Left("must have MANAGE_USERS permission"))
        verifyCanChangeRoleTo(someAdmin, someUnassigned, USER, Left("must have MANAGE_USERS permission"))
    }

    @Test
    fun listedRolesFor() {
        val someOwner = UserRolePermissions("owner", OWNER, allPermissions)
        val someAuditor = UserRolePermissions("auditor", AUDITOR, allPermissions)
        val someAdmin = UserRolePermissions("admin", ADMIN, allPermissions)
        val someUser = UserRole("user", USER)
        val someObserver = UserRole("observer", OBSERVER)
        val someUnassigned= UserRole("unassigned", UNASSIGNED)
        verifyListedRolesFor(someOwner, someUser, listOf(UNASSIGNED, OBSERVER, USER, ADMIN, AUDITOR, OWNER))
        verifyListedRolesFor(someAuditor, someObserver,  listOf(UNASSIGNED, OBSERVER, USER, ADMIN))
        verifyListedRolesFor(someAdmin, someUnassigned, listOf(UNASSIGNED, OBSERVER, USER))
    }

    fun verifyListedRolesFor(self:UserRolePermissions, target:UserRole, expected: List<Role>){
        val actual = self.listedRolesFor(target)
        assertEquals(expected, actual, "$self $target")
    }

    fun verifyCanChangeRoleTo(self:UserRolePermissions, target:UserRole, newRole:Role, expected:Either<String, Unit>){
        val actual = self.canChangeRole(target, newRole)
        assertEquals(expected, actual,"$self $target $newRole")
    }
}