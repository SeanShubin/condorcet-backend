package com.seanshubin.condorcet.backend.service.http

import com.seanshubin.condorcet.backend.jwt.Cipher
import com.seanshubin.condorcet.backend.service.Service

class ServiceEnvironment(val service: Service, val cipher: Cipher)
