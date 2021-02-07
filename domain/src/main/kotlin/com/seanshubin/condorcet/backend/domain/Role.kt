package com.seanshubin.condorcet.backend.domain

enum class Role(val description: String) {
    OWNER("Only 1 owner, can transfer OWNER to another user, can do anything AUDITOR can do"),
    AUDITOR("Can see secrets, can do anything a ADMIN can do"),
    ADMIN("Can manage users, can do anything a USER can do"),
    USER("Can use the application, can do anything an OBSERVER can do"),
    OBSERVER("Can navigate the application"),
    UNASSIGNED("Waiting for ADMIN to promote")
}
