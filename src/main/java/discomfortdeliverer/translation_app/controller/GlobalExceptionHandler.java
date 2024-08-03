package discomfortdeliverer.translation_app.controller;

import discomfortdeliverer.translation_app.exceptions.InternalServiceException;
import discomfortdeliverer.translation_app.exceptions.InvalidEnteredDataException;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<String> handleLanguageNotFoundException() {
        return ResponseEntity.status(400).body("Не найден язык исходного сообщения");
    }

    @ExceptionHandler(TranslationResourceAccessException.class)
    public ResponseEntity<String> handleTranslationResourceAccessException() {
        return ResponseEntity.status(400).body("Ошибка доступа к ресурсу перевода");
    }

    @ExceptionHandler(InternalServiceException.class)
    public ResponseEntity<String> handleInternalServiceException() {
        return ResponseEntity.status(500).body("Внутренняя ошибка сервиса");
    }

    @ExceptionHandler(InvalidEnteredDataException.class)
    public ResponseEntity<String> handleInvalidEnteredDataException(InvalidEnteredDataException e) {
        return ResponseEntity.status(400).body(e.getMessage());
    }
}
