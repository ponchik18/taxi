package com.modsen.promocodeservice.exception

import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.NoHandlerFoundException
import java.util.*

@RestControllerAdvice
class PromoCodeExceptionHandler {

    @ExceptionHandler(PromoCodeNotFountException::class, NoHandlerFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(exception: Exception) = ErrorMessageResponse(
        statusCode = HttpStatus.NOT_FOUND.value(),
        timestamp = Date(),
        message = exception.message!!
    )

    @ExceptionHandler(DuplicateKeyException::class)
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleDuplicateException(exception: Exception) = ErrorMessageResponse(
        statusCode = HttpStatus.CONFLICT.value(),
        timestamp = Date(),
        message = exception.message!!
    )

    @ExceptionHandler(Exception::class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleServerErrorException(exception: Exception) = ErrorMessageResponse(
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value(),
        timestamp = Date(),
        message = exception.message!!
    )

    @ExceptionHandler(AccessDeniedException::class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    fun handleAccessDeniedException(exception: Exception) = ErrorMessageResponse(
        statusCode = HttpStatus.FORBIDDEN.value(),
        timestamp = Date(),
        message = exception.message!!
    )
}