package discomfortdeliverer.translation_app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Translation {
    private String ipAddress;
    private String sourceText;
    private String translatedText;
}
