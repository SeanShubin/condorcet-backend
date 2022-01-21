package com.seanshubin.condorcet.backend.domain

enum class Role(val description: String) {
    NO_ACCESS("Waiting for ADMIN to promote"),
    OBSERVER("Can navigate the application"),
    USER("Can use the application, can do anything an OBSERVER can do"),
    ADMIN("Can manage users, can do anything a USER can do"),
    AUDITOR("Can see secrets, can do anything a ADMIN can do"),
    OWNER("Only 1 owner, can transfer OWNER to another user, can do anything AUDITOR can do");
    companion object{
        val PRIMARY_ROLE = OWNER
        val SECONDARY_ROLE = AUDITOR
        val DEFAULT_ROLE = OBSERVER
    }
}
