package com.seanshubin.condorcet.backend.domain

import com.seanshubin.condorcet.backend.crypto.PasswordUtil

class ApiService(private val passwordUtil: PasswordUtil) : Service {
    private var list: List<User> = emptyList()

    override fun health(): Response {
        return Response.Health("ok")
    }

    override fun unsupported(name: String, text: String): Response {
        val userSafeMessage = "Unsupported command '$name'"
        return Response.Unsupported(userSafeMessage, name, text)
    }

    override fun addUser(name: String, email: String, password: String): Response {
        if (nameExists(name)) {
            return Response.Conflict("User with name '$name' already exists")
        }
        if (emailExists(email)) {
            return Response.Conflict("User with email '$email' already exists")
        }
        val (salt, hash) = passwordUtil.createSaltAndHash(password)
        val user = User(name, email, salt, hash)
        list = list + user
        return Response.UserName(user.name)
    }

    override fun authenticate(nameOrEmail: String, password: String): Response {
        var user: User? = null
        if (user == null) {
            user = list.find { it.name == nameOrEmail }
        }
        if (user == null) {
            user = list.find { it.email == nameOrEmail }
        }
        if (user == null) {
            return Response.NotFound("User with name or email '$nameOrEmail' does not exist")
        }
        if (passwordUtil.validatePassword(password, user.salt, user.hash)) {
            return Response.UserName(user.name)
        } else {
            return Response.Unauthorized("Authentication failed for user with name or email '$nameOrEmail'")
        }
    }

    private fun nameExists(name: String): Boolean = list.find { it.name == name } != null
    private fun emailExists(email: String): Boolean = list.find { it.email == email } != null
}
/*
import * as R from 'ramda'
import util from 'util'

const statusCode = {
    ok: 200,
    unauthorized: 401,
    notFound: 404,
    conflict: 409
}

const composeResponse = ({status, bodyJson}) => {
    const json = () => Promise.resolve(bodyJson)
    const ok = status >= 200 && status <= 299
    const result = Promise.resolve({status, ok, json})
    return result
}

const users = JSON.parse(sessionStorage.getItem('users')) || []
const addUser = ({name, email, password}) => {
    users.push({name, email, password})
    sessionStorage.setItem('users', JSON.stringify(users))
}
const findByName = name => R.find(R.propEq('name', name), users)
const findByEmail = email => R.find(R.propEq('email', email), users)
const nameExists = name => !!findByName(name)
const emailExists = email => !!findByEmail(email)
const loginRequest = ({nameOrEmail, password}) => {
    let found
    if (!found) found = findByName(nameOrEmail)
    if (!found) found = findByEmail(nameOrEmail)
    if (found) {
        if (found.password === password) {
            return {
                status: statusCode.ok,
                bodyJson: {name: found.name}
            }
        } else {
            return {
                status: statusCode.unauthorized,
                bodyJson: {userMessage: `Wrong password for user '${nameOrEmail}'`}
            }
        }
    } else {
        return {
            status: statusCode.notFound,
            bodyJson: {userMessage: `User '${nameOrEmail}' not found`}
        }
    }
}
const registerRequest = ({name, email, password}) => {
    if (nameExists(name)) {
        return {
            status: statusCode.conflict,
            bodyJson: {userMessage: `User name '${name}' is unavailable`}
        }
    } else if (emailExists(email)) {
        return {
            status: statusCode.conflict,
            bodyJson: {userMessage: `User with email '${email}' is already registered`}
        }
    } else {
        addUser({name, email, password})
        return {
            status: statusCode.ok,
            bodyJson: {name}
        }
    }
}

if (R.isEmpty(users)) {
    addUser({name: 'foo', email: 'foo@email.com', password: 'bar'})
}

const fetchSimulator = (resource, init) => {
    let result
    if (resource === '/proxy/login-request' && init.method === 'POST') {
        const {nameOrEmail, password} = JSON.parse(init.body)
        result = loginRequest({nameOrEmail, password})
    } else if (resource === '/proxy/register-request' && init.method === 'POST') {
        const {name, email, password} = JSON.parse(init.body)
        result = registerRequest({name, email, password})
    } else {
        throw Error(`No response defined for ${util.inspect({resource, init})}`)
    }
    return composeResponse(result)
}

export default fetchSimulator

 */