package com.seanshubin.condorcet.backend.database

import com.seanshubin.condorcet.backend.domain.ElectionUpdates
import com.seanshubin.condorcet.backend.domain.Role
import com.seanshubin.condorcet.backend.genericdb.GenericDatabase

class StateDbCommandsImpl(genericDatabase: GenericDatabase) : StateDbCommands, GenericDatabase by genericDatabase {
    override fun setLastSynced(lastSynced: Int) {
        update("set-last-synced", lastSynced)
    }

    override fun initializeLastSynced(lastSynced: Int) {
        update("initialize-last-synced", lastSynced)
    }

    override fun createUser(authority: String, name: String, email: String, salt: String, hash: String, role: Role) {
        update("create-user", name, email, salt, hash, role.name)
    }

    override fun setRole(authority: String, name: String, role: Role) {
        update("set-role", role.name, name)
    }

    override fun removeUser(authority: String, name: String) {
        update("delete-user", name)
    }

    override fun addElection(authority: String, owner: String, name: String) {
        update("add-election", owner, name)
    }

    override fun updateElection(authority: String, name:String, updates: ElectionUpdates) {
        if (updates.newName != null) {
            update("set-election-name", updates.newName, name)
        }
        if (updates.secretBallot != null) {
            update("set-election-secret-ballot", updates.secretBallot, name)
        }
        if (updates.clearScheduledStart == true) {
            update("set-election-scheduled-start", null, name)
        } else {
            update("set-election-scheduled-start", updates.scheduledStart, name)
        }
        if (updates.clearScheduledEnd == true) {
            update("set-election-scheduled-end", null, name)
        } else {
            update("set-election-scheduled-end", updates.scheduledEnd, name)
        }
        if (updates.restrictWhoCanVote != null) {
            update("set-election-restrict-who-can-vote", updates.restrictWhoCanVote, name)
        }
        if (updates.ownerCanDeleteBallots != null) {
            update("set-election-owner-can-delete-ballots", updates.ownerCanDeleteBallots, name)
        }
        if (updates.auditorCanDeleteBallots != null) {
            update("set-election-auditor-can-delete-ballots", updates.auditorCanDeleteBallots, name)
        }
        if (updates.isTemplate != null) {
            update("set-election-is-template", updates.isTemplate, name)
        }
        if (updates.noChangesAfterVote != null) {
            update("set-election-no-changes-after-vote", updates.noChangesAfterVote, name)
        }
        if (updates.isOpen != null) {
            update("set-election-is-open", updates.isOpen, name)
        }
    }

    override fun deleteElection(authority: String, name: String) {
        update("delete-election", name)
    }
}
