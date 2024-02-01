package ee.lhv.homework.controller;

import ee.lhv.homework.controller.model.EntityFieldValidationErrorData;
import ee.lhv.homework.controller.model.ErrorData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException,
            HttpHeaders httpHeaders,
            HttpStatusCode httpStatusCode,
            WebRequest webRequest) {
        List<EntityFieldValidationErrorData> entityFieldValidationErrorDataList = methodArgumentNotValidException
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectError -> {
                    String fieldName = ((FieldError) objectError).getField();
                    String validationErrorMessage = objectError.getDefaultMessage();
                    return EntityFieldValidationErrorData.builder()
                            .fieldName(fieldName)
                            .validationErrorMessage(validationErrorMessage)
                            .build();
                })
                .sorted(Comparator.comparing(EntityFieldValidationErrorData::getFieldName))
                .toList();
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorData errorData = ErrorData.builder().entityFieldValidationErrors(entityFieldValidationErrorDataList).build();
        return handleExceptionInternal(methodArgumentNotValidException, errorData, httpHeaders, httpStatus, webRequest);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<Object> handleResponseStatusException(
            ResponseStatusException responseStatusException,
            WebRequest webRequest) {
        return resolveResponseEntity(responseStatusException.getStatusCode(), responseStatusException, webRequest);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleGeneralException(Exception exception, WebRequest webRequest) {
        return resolveResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, webRequest);
    }

    private ResponseEntity<Object> resolveResponseEntity(
            HttpStatusCode httpStatusCode,
            Exception exception,
            WebRequest webRequest) {
        ErrorData errorData = resolveErrorData(httpStatusCode, exception);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return handleExceptionInternal(exception, errorData, httpHeaders, httpStatusCode, webRequest);
    }

    private static ErrorData resolveErrorData(HttpStatusCode httpStatusCode, Exception exception) {
        UUID uuid = UUID.randomUUID();
        log.error(String.format("Unable to handle the request [ERROR_UUID:%s]", uuid), exception);
        String message = switch ((HttpStatus) httpStatusCode) {
            case BAD_REQUEST -> "Bad Request";
            case INTERNAL_SERVER_ERROR -> "Internal Server Error";
            default -> exception.getMessage();
        };
        return ErrorData.builder().uuid(uuid).message(message).build();
    }

}
