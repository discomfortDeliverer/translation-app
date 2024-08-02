package discomfortdeliverer.translation_app;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class TranslationRequestDto {
    private String sourceLanguage;
    private String targetLanguage;
    private String textToTranslate;
}
