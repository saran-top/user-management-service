package com.saran.api.usermanagement.controller.handler;

import com.saran.api.usermanagement.dto.ErrorResponseDTO;
import com.saran.api.usermanagement.exception.ResourceExistsException;
import com.saran.api.usermanagement.exception.ResourceNotFoundException;
import com.saran.api.usermanagement.util.ValidationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler
{

    private static final String OPTIMISTIC_LOCKING_ERROR = "OPTIMISTIC_LOCKING_ERROR";
    private static final String NOT_FOUND_ERROR = "NOT_FOUND_ERROR";
    private static final String EXISTS_ERROR = "EXISTS_ERROR";
    private static final String VALIDATION_ERROR = "VALIDATION_ERROR";
    private static final String SYSTEM_ERROR = "SYSTEM_ERROR";

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    protected ResponseEntity<ErrorResponseDTO> handleObjectOptimisticLockingFailure(final ObjectOptimisticLockingFailureException exception)
    {
        log.error(OPTIMISTIC_LOCKING_ERROR, exception);
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode(OPTIMISTIC_LOCKING_ERROR)
                .errorMessage("Entity was updated or deleted by another transaction. Try after the refreshing the entity.")
                .build();
        return new ResponseEntity<>(dto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ErrorResponseDTO> handleResourceNotFound(final ResourceNotFoundException exception)
    {
        log.error(NOT_FOUND_ERROR, exception);
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode(NOT_FOUND_ERROR)
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistsException.class)
    protected ResponseEntity<ErrorResponseDTO> handleResourceExists(final ResourceExistsException exception)
    {
        log.error(EXISTS_ERROR, exception);
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode(EXISTS_ERROR)
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDTO> handleSystem(final Exception exception)
    {
        log.error(SYSTEM_ERROR, exception);
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode(SYSTEM_ERROR)
                .errorMessage(exception.getMessage())
                .build();
        return new ResponseEntity<>(dto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request)
    {
        log.error(VALIDATION_ERROR, exception);
        System.out.println(ValidationUtil.convert(exception.getBindingResult()));
        ErrorResponseDTO dto = ErrorResponseDTO.builder()
                .errorCode(VALIDATION_ERROR)
                .errorMessage("Validation errors. Refer errors for details")
                .errors(ValidationUtil.convert(exception.getBindingResult()))
                .build();
        return handleExceptionInternal(exception, dto,headers, HttpStatus.BAD_REQUEST, request);
    }
}
