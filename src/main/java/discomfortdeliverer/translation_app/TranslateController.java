package discomfortdeliverer.translation_app;

import discomfortdeliverer.translation_app.service.TranslationApiService;
import discomfortdeliverer.translation_app.service.TranslationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslateController {
    private static final int MAX_LENGTH_LANGUAGE_CODE = 2;
    @Autowired
    private TranslationService translationService;
    @PostMapping("/translate")
    public String translate(@RequestBody String text,
                        @RequestParam String sourceLanguage,
                        @RequestParam String targetLanguage) {
        if(!isValidLanguageName(sourceLanguage) || !isValidLanguageName(targetLanguage)) {
            return "неверный код языка";
        }
        if (text.trim().isEmpty()) return "Пустой текст для перевода";

        TranslationRequestDto translationRequestDto = new TranslationRequestDto();
        translationRequestDto.setSourceLanguage(sourceLanguage);
        translationRequestDto.setTargetLanguage(targetLanguage);
        translationRequestDto.setTextToTranslate(text);

        return translationService.translate(translationRequestDto);
    }
    private boolean isValidLanguageName(String languageCode) {
        return languageCode.length() <= MAX_LENGTH_LANGUAGE_CODE;
    }

}
