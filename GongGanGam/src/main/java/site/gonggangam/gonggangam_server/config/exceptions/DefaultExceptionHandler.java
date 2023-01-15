package site.gonggangam.gonggangam_server.config.exceptions;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import site.gonggangam.gonggangam_server.config.ResponseCode;
import site.gonggangam.gonggangam_server.dto.ErrorResponseDto;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ErrorResponseDto> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode.VALIDATION_ERROR, request);
    }

    @ExceptionHandler(value = {GeneralException.class})
    public ResponseEntity<ErrorResponseDto> handleGeneralException(GeneralException e, WebRequest request) {
        return handleExceptionInternal(e, e.getErrorCode(), request);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    public ResponseEntity<ErrorResponseDto> handleAuthentication(AuthenticationException e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode.TOKEN_INVALID, request);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
        return handleExceptionInternal(e, ResponseCode.PERMISSION_DENIED, request);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleUnexpectedException(Exception e, WebRequest request) {
        log.error("server internal error occurred: " + e + " request = " + request);
        return handleExceptionInternal(e, ResponseCode.INTERNAL_ERROR, request);
    }

//    @Override
//    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body,
//                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
//        return handleExceptionInternal(e, ResponseCode.valueOf(status), headers, status, request);
//    }

    private ResponseEntity<ErrorResponseDto> handleExceptionInternal(Exception e, ResponseCode errorCode,
                                                           WebRequest request) {

        return handleExceptionInternal(
                e,
                errorCode,
                HttpHeaders.EMPTY,
                errorCode.getHttpStatus(),
                request
        );
    }

    private ResponseEntity<ErrorResponseDto> handleExceptionInternal(Exception e, ResponseCode code, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return ResponseEntity
                .status(code.getHttpStatus())
                .body(ErrorResponseDto.of(code));

    }
}
