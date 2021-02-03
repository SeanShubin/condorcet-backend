package com.seanshubin.condorcet.backend.server

import com.seanshubin.condorcet.backend.genericdb.ConnectionWrapper
import com.seanshubin.condorcet.backend.genericdb.Lifecycle
import com.seanshubin.condorcet.backend.genericdb.Schema

class Database(val schema: Schema, val lifecycle: Lifecycle<ConnectionWrapper>)
