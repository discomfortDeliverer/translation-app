package discomfortdeliverer.translation_app;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TranslateController {
    @PostMapping("/translate")
    public String translate(@RequestBody String reqBod,
                        @RequestParam String sourceLanguage,
                        @RequestParam String targetLanguage) {

        return "";
    }

}
