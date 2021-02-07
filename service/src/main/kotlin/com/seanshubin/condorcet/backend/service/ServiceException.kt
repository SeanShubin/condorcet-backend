package com.seanshubin.condorcet.backend.service

class ServiceException(val serviceResponse: ServiceResponse) : RuntimeException()
