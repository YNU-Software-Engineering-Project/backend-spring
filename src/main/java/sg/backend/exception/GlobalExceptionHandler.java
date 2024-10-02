package sg.backend.exception;

import org.apache.tomcat.util.http.fileupload.impl.InvalidContentTypeException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import sg.backend.dto.response.ResponseDto;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseDto response = ResponseDto.builder()
                .code("METHOD_NOT_ALLOWED")
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ResponseDto response = ResponseDto.builder()
                .code("VALIDATION_FAILED")
                .message(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SpelEvaluationException.class)
    public ResponseEntity<ResponseDto> handleSpelEvaluationException() {
        ResponseDto response = ResponseDto.builder()
                .code("AUTHORIZATION_FAIL")
                .message("Authentication token is missing or invalid")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidContentTypeException.class)
    public ResponseEntity<ResponseDto> handleInvalidContentType() {
        ResponseDto response = ResponseDto.builder()
                .code("INVALID_CONTENT_TYPE")
                .message("Invalid content type. Please ensure you are sending a multipart/form-data request.")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ResponseDto> handleCustomException(UnauthorizedAccessException ex) {
        ResponseDto response = ResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto> handleCustomException(CustomException ex) {
        ResponseDto response = ResponseDto.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
