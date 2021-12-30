package com.seanshubin.condorcet.backend.domain

data class Place(val rank: Int, val candidate: String) {
    override fun toString(): String = "$rank $candidate"
    fun toKotlinString(): String = """Place($rank, "$candidate")"""

    companion object {
        fun List<Place>.toKotlinString(): String = this.joinToString(", ", "listOf(", ")") { it.toKotlinString() }
    }
}