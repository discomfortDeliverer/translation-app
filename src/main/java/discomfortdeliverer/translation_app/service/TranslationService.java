package discomfortdeliverer.translation_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import discomfortdeliverer.translation_app.model.Translation;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.repository.TranslationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TranslationService {
    @Autowired
    private TranslationApiService translationApiService;

    @Autowired
    private TranslationRepository translationRepository;
    public String translate(TranslationRequestDto translationRequestDto) {
        setLanguageCodesToLowerCase(translationRequestDto);

        try {
            TranslationResultDto translationResultDto = translationApiService.translate(translationRequestDto);

            Translation translation = convertDtoToModel(translationResultDto);
            return translationRepository.saveTranslation(translation);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (LanguageNotFoundException e) {
            System.out.println("Язык не найден");
            return null;
        }
    }

    private Translation convertDtoToModel(TranslationResultDto translationResultDto) {
        return new Translation(translationResultDto.getIpAddress(),
                translationResultDto.getTextToTranslate(), translationResultDto.getTranslatedText());
    }

    private void setLanguageCodesToLowerCase(TranslationRequestDto translationRequestDto) {
        translationRequestDto.setSourceLanguage(translationRequestDto.getSourceLanguage().toLowerCase());
        translationRequestDto.setTargetLanguage(translationRequestDto.getTargetLanguage().toLowerCase());
    }

    public List<Translation> getAllTranslations() {
        return translationRepository.findAllTranslation();
    }
}
