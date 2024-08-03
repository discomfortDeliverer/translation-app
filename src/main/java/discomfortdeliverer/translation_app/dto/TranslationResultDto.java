package discomfortdeliverer.translation_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationResultDto {
    private String sourceLanguage;
    private String targetLanguage;
    private String textToTranslate;
    private String ipAddress;
    private String translatedText;
}
