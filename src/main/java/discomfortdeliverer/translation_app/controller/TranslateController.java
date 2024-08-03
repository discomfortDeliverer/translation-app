package discomfortdeliverer.translation_app.controller;

import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.exceptions.InternalServiceException;
import discomfortdeliverer.translation_app.exceptions.InvalidEnteredDataException;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import discomfortdeliverer.translation_app.model.Translation;
import discomfortdeliverer.translation_app.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TranslateController {
    private static final int MAX_LENGTH_LANGUAGE_CODE = 2;
    @Autowired
    private TranslationService translationService;
    @PostMapping("/translate")
    public String translate(@RequestBody String text,
                            @RequestParam String sourceLanguage,
                            @RequestParam String targetLanguage,
                            HttpServletRequest req) throws LanguageNotFoundException,
            TranslationResourceAccessException, InternalServiceException, InvalidEnteredDataException {
        if(!isValidLanguageName(sourceLanguage) || !isValidLanguageName(targetLanguage)) {
            throw new InvalidEnteredDataException("Некорректно указаны sourceLanguage и targetLanguage");
        }
        if (text.trim().isEmpty()) throw new InvalidEnteredDataException("Пустой текст для перевода");

        String ipAddress = req.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = req.getRemoteAddr();
        }

        TranslationRequestDto translationRequestDto = new TranslationRequestDto();
        translationRequestDto.setSourceLanguage(sourceLanguage);
        translationRequestDto.setTargetLanguage(targetLanguage);
        translationRequestDto.setTextToTranslate(text);
        translationRequestDto.setIpAddress(ipAddress);

        return translationService.translate(translationRequestDto);
    }

    @GetMapping("/translations")
    public List<Translation> translations() {
        return translationService.getAllTranslations();
    }
    private boolean isValidLanguageName(String languageCode) {
        return languageCode.length() <= MAX_LENGTH_LANGUAGE_CODE;
    }

}
