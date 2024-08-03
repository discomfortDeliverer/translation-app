package discomfortdeliverer.translation_app;

import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
                            HttpServletRequest req) {
        if(!isValidLanguageName(sourceLanguage) || !isValidLanguageName(targetLanguage)) {
            return "неверный код языка";
        }
        if (text.trim().isEmpty()) return "Пустой текст для перевода";

        String ipAddress = req.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = req.getRemoteAddr();
        }

        TranslationRequestDto translationRequestDto = new TranslationRequestDto();
        translationRequestDto.setSourceLanguage(sourceLanguage);
        translationRequestDto.setTargetLanguage(targetLanguage);
        translationRequestDto.setTextToTranslate(text);
        translationRequestDto.setIpAddress(ipAddress);

        TranslationResultDto translationResultDto = translationService.translate(translationRequestDto);
        return translationResultDto.getTranslatedText();
    }

    @GetMapping("/translations")
    public List<Translation> translations() {
        return translationService.getAllTranslations();
    }
    private boolean isValidLanguageName(String languageCode) {
        return languageCode.length() <= MAX_LENGTH_LANGUAGE_CODE;
    }

}
