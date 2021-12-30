package com.seanshubin.condorcet.backend.domain

data class Place(val rank: Int, val candidate: String) {
    override fun toString(): String = "$rank $candidate"
}