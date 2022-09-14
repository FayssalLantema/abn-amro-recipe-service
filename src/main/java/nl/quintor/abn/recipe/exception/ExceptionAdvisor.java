package nl.quintor.abn.recipe.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonAlreadyExistEception.class)
    protected ResponseEntity<Object> handlePersonAlreadyExist(PersonAlreadyExistEception exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IngredientAlreadyExistException.class)
    protected ResponseEntity<Object> handleIngredientAlreadyExist(IngredientAlreadyExistException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    protected ResponseEntity<Object> handlePersonNotFound(PersonNotFoundException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IngredientDoesNotExistException.class)
    protected ResponseEntity<Object> handleIngredientNotFound(IngredientDoesNotExistException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RecipeNotFoundException.class)
    protected ResponseEntity<Object> handleRecipeNotFound(RecipeNotFoundException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InstructionNotFoundException.class)
    protected ResponseEntity<Object> handleInstructionNotFound(InstructionNotFoundException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DoesNotBelongException.class)
    protected ResponseEntity<Object> handleDoesNotBelong(DoesNotBelongException exception) {
        return new ResponseEntity<>(setBody(exception), HttpStatus.NOT_FOUND);
    }

    private Map<String, Object> setBody(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        return body;
    }


}