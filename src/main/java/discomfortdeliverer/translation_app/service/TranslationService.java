package discomfortdeliverer.translation_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import discomfortdeliverer.translation_app.exceptions.InternalServiceException;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import discomfortdeliverer.translation_app.model.Translation;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.repository.TranslationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class TranslationService {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    @Autowired
    private TranslationApiService translationApiService;

    @Autowired
    private TranslationRepository translationRepository;
    public String translate(TranslationRequestDto translationRequestDto)
            throws LanguageNotFoundException, TranslationResourceAccessException, InternalServiceException {

        toLowerCaseAndTrim(translationRequestDto);

        try {
            TranslationResultDto translationResultDto = translationApiService.translate(translationRequestDto);

            Translation translation = convertDtoToModel(translationResultDto);
            return translationRepository.saveTranslation(translation);
        } catch (InterruptedException | JsonProcessingException | ExecutionException e) {
            log.warn("Исключение поймано в методе translate", e);
            throw new InternalServiceException("Процесс перевода завершился неудачей", e);
        }
    }

    private Translation convertDtoToModel(TranslationResultDto translationResultDto) {
        return new Translation(translationResultDto.getIpAddress(),
                translationResultDto.getTextToTranslate(), translationResultDto.getTranslatedText());
    }

    private void toLowerCaseAndTrim(TranslationRequestDto translationRequestDto) {
        translationRequestDto.setSourceLanguage(translationRequestDto.getSourceLanguage().toLowerCase());
        translationRequestDto.setTargetLanguage(translationRequestDto.getTargetLanguage().toLowerCase());
        translationRequestDto.setTextToTranslate(translationRequestDto.getTextToTranslate().trim());
    }

    public List<Translation> getAllTranslations() {
        return translationRepository.findAllTranslation();
    }
}
