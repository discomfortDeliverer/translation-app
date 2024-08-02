package discomfortdeliverer.translation_app;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslateController {
    private static final int MAX_LENGTH_LANGUAGE_CODE = 2;
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
        return "";
    }
    private boolean isValidLanguageName(String languageCode) {
        return languageCode.length() <= MAX_LENGTH_LANGUAGE_CODE;
    }

}
