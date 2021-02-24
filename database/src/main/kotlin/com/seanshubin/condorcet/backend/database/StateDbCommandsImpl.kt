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
        if(updates.restrictedToVoterList != null){
            update("set-election-restricted-to-voter-list", updates.restrictedToVoterList, name)
        }
        if(updates.shouldSetStartTime == true){
            update("set-election-start", updates.startTime, name)
        }
        if(updates.shouldSetEndTime == true){
            update("set-election-end", updates.endTime, name)
        }
        if(updates.secretBallot != null){
            update("set-election-secret-ballot", updates.secretBallot, name)
        }
        if(updates.shouldSetWhenDoneConfiguring == true){
            update("set-election-done-configuring", updates.whenDoneConfiguring, name)
        }
        if(updates.isTemplate != null){
            update("set-election-template", updates.isTemplate, name)
        }
        if(updates.isStarted != null){
            update("set-election-started", updates.isStarted, name)
        }
        if(updates.isFinished != null){
            update("set-election-finished", updates.isFinished, name)
        }
        if(updates.canChangeCandidatesAfterDoneConfiguring != null){
            update("set-election-can-change-candidates-after-done-configuring", updates.canChangeCandidatesAfterDoneConfiguring, name)
        }
        if(updates.ownerCanDeleteBallots != null){
            update("set-election-owner-can-delete-ballots", updates.ownerCanDeleteBallots, name)
        }
        if(updates.auditorCanDeleteBallots != null){
            update("set-election-auditor-can-delete-ballots", updates.auditorCanDeleteBallots, name)
        }
    }
}
