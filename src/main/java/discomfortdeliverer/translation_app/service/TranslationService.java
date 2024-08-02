package discomfortdeliverer.translation_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import discomfortdeliverer.translation_app.LanguageNotFoundException;
import discomfortdeliverer.translation_app.TranslationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class TranslationService {
    @Autowired
    private TranslationApiService translationApiService;
    public String translate(TranslationRequestDto translationRequestDto) {
        setLanguageCodesToLowerCase(translationRequestDto);

        try {
            return translationApiService.translate(translationRequestDto);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (LanguageNotFoundException e) {
            System.out.println("Язык не найден");
            return "Язык не найден";
        }
    }

    private void setLanguageCodesToLowerCase(TranslationRequestDto translationRequestDto) {
        translationRequestDto.setSourceLanguage(translationRequestDto.getSourceLanguage().toLowerCase());
        translationRequestDto.setTargetLanguage(translationRequestDto.getTargetLanguage().toLowerCase());
    }

}
