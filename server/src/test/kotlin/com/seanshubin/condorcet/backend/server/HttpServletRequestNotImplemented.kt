package com.seanshubin.condorcet.backend.server

import jakarta.servlet.*
import jakarta.servlet.http.*
import java.io.BufferedReader
import java.security.Principal
import java.util.*

abstract class HttpServletRequestNotImplemented : HttpServletRequest {
    override fun getAttribute(name: String?): Any {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getAttributeNames(): Enumeration<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getCharacterEncoding(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCharacterEncoding(env: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getContentLength(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getContentLengthLong(): Long {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getContentType(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getInputStream(): ServletInputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParameter(name: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParameterNames(): Enumeration<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParameterValues(name: String?): Array<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParameterMap(): MutableMap<String, Array<String>> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getProtocol(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getScheme(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getServerName(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getServerPort(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getReader(): BufferedReader {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRemoteAddr(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRemoteHost(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setAttribute(name: String?, o: Any?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun removeAttribute(name: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocale(): Locale {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocales(): Enumeration<Locale> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isSecure(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRequestDispatcher(path: String?): RequestDispatcher {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRealPath(path: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRemotePort(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocalName(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocalAddr(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocalPort(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getServletContext(): ServletContext {
        throw UnsupportedOperationException("not implemented")
    }

    override fun startAsync(): AsyncContext {
        throw UnsupportedOperationException("not implemented")
    }

    override fun startAsync(servletRequest: ServletRequest?, servletResponse: ServletResponse?): AsyncContext {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isAsyncStarted(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isAsyncSupported(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getAsyncContext(): AsyncContext {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDispatcherType(): DispatcherType {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getAuthType(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getCookies(): Array<Cookie>? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getDateHeader(name: String?): Long {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeader(name: String?): String? {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeaders(name: String?): Enumeration<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeaderNames(): Enumeration<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getIntHeader(name: String?): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getMethod(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getPathInfo(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getPathTranslated(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getContextPath(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getQueryString(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRemoteUser(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isUserInRole(role: String?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getUserPrincipal(): Principal {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRequestedSessionId(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRequestURI(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getRequestURL(): StringBuffer {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getServletPath(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getSession(create: Boolean): HttpSession {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getSession(): HttpSession {
        throw UnsupportedOperationException("not implemented")
    }

    override fun changeSessionId(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isRequestedSessionIdValid(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isRequestedSessionIdFromCookie(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isRequestedSessionIdFromURL(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isRequestedSessionIdFromUrl(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun authenticate(response: HttpServletResponse?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun login(username: String?, password: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun logout() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getParts(): MutableCollection<Part> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getPart(name: String?): Part {
        throw UnsupportedOperationException("not implemented")
    }

    override fun <T : HttpUpgradeHandler?> upgrade(handlerClass: Class<T>?): T {
        throw UnsupportedOperationException("not implemented")
    }
}