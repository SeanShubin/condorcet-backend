package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.algorithms.Algorithm

interface AlgorithmFactory {
    fun create():Algorithm
}
