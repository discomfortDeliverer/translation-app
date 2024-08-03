package discomfortdeliverer.translation_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TranslationRequestDto {
    private String sourceLanguage;
    private String targetLanguage;
    private String textToTranslate;
    private String ipAddress;
}
