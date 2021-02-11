package com.seanshubin.condorcet.backend.server

import jakarta.servlet.ServletOutputStream
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import java.io.PrintWriter
import java.util.*

abstract class HttpServletResponseNotImplemented : HttpServletResponse {
    override fun getCharacterEncoding(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getContentType(): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getOutputStream(): ServletOutputStream {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getWriter(): PrintWriter {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setCharacterEncoding(charset: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setContentLength(len: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setContentLengthLong(len: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setContentType(type: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setBufferSize(size: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getBufferSize(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun flushBuffer() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun resetBuffer() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun isCommitted(): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun reset() {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setLocale(loc: Locale?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getLocale(): Locale {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addCookie(cookie: Cookie) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun containsHeader(name: String?): Boolean {
        throw UnsupportedOperationException("not implemented")
    }

    override fun encodeURL(url: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun encodeRedirectURL(url: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun encodeUrl(url: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun encodeRedirectUrl(url: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun sendError(sc: Int, msg: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun sendError(sc: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun sendRedirect(location: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setDateHeader(name: String?, date: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addDateHeader(name: String?, date: Long) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setHeader(name: String?, value: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addHeader(name: String, value: String) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setIntHeader(name: String?, value: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun addIntHeader(name: String?, value: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setStatus(sc: Int) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun setStatus(sc: Int, sm: String?) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getStatus(): Int {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeader(name: String?): String {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeaders(name: String?): MutableCollection<String> {
        throw UnsupportedOperationException("not implemented")
    }

    override fun getHeaderNames(): MutableCollection<String> {
        throw UnsupportedOperationException("not implemented")
    }
}