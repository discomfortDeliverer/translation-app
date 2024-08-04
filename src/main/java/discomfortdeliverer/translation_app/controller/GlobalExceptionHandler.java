package discomfortdeliverer.translation_app.controller;

import discomfortdeliverer.translation_app.exceptions.InternalServiceException;
import discomfortdeliverer.translation_app.exceptions.InvalidEnteredDataException;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import discomfortdeliverer.translation_app.service.TranslationApiService;
import discomfortdeliverer.translation_app.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    @Autowired
    private TranslationApiService translationApiService;
    @ExceptionHandler(LanguageNotFoundException.class)
    public ResponseEntity<String> handleLanguageNotFoundException(LanguageNotFoundException e) {
        log.warn("Произошло исключение LanguageNotFoundException");
        return ResponseEntity.status(400).body(e.getMessage() + "\nСписок поддерживоемых языков:\n"
                + translationApiService.getLanguageMap());
    }

    @ExceptionHandler(TranslationResourceAccessException.class)
    public ResponseEntity<String> handleTranslationResourceAccessException() {
        log.warn("Произошло исключение TranslationResourceAccessException");
        return ResponseEntity.status(400).body("Ошибка доступа к ресурсу перевода");
    }

    @ExceptionHandler(InternalServiceException.class)
    public ResponseEntity<String> handleInternalServiceException() {
        log.warn("Произошло исключение InternalServiceException");
        return ResponseEntity.status(500).body("Внутренняя ошибка сервиса");
    }

    @ExceptionHandler(InvalidEnteredDataException.class)
    public ResponseEntity<String> handleInvalidEnteredDataException(InvalidEnteredDataException e) {
        log.warn("Произошло исключение InvalidEnteredDataException", e);
        return ResponseEntity.status(400).body(e.getMessage()  + "\nСписок поддерживоемых языков:\n"
                + translationApiService.getLanguageMap());
    }
}
