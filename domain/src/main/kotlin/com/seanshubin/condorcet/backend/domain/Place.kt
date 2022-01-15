package com.seanshubin.condorcet.backend.domain

data class Place(val rank: Int, val candidateName: String) {
    override fun toString(): String = "$rank $candidateName"
    fun toKotlinString(): String = """Place($rank, "$candidateName")"""

    companion object {
        fun List<Place>.toKotlinString(): String = this.joinToString(", ", "listOf(", ")") { it.toKotlinString() }
    }
}