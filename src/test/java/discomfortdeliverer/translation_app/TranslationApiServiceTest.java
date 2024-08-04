package discomfortdeliverer.translation_app;

import com.fasterxml.jackson.core.JsonProcessingException;
import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.exceptions.InternalServiceException;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import discomfortdeliverer.translation_app.model.Translation;
import discomfortdeliverer.translation_app.repository.TranslationRepository;
import discomfortdeliverer.translation_app.service.TranslationApiService;
import discomfortdeliverer.translation_app.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.ExecutionException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TranslationApiServiceTest {
    @Autowired
    private TranslationApiService translationApiService;
    private TranslationRequestDto translationRequestDto;

    @BeforeEach
    public void setUp() {
        translationRequestDto = new TranslationRequestDto();
    }
    @Test
    void shouldReturnTranslationResultDto()
            throws LanguageNotFoundException, TranslationResourceAccessException, ExecutionException,
            InterruptedException, JsonProcessingException {

        translationRequestDto.setIpAddress("0.0.0.0.0.0.1");
        translationRequestDto.setTextToTranslate("One two & & & Three? ?");
        translationRequestDto.setSourceLanguage("en");
        translationRequestDto.setTargetLanguage("ru");

        TranslationResultDto translationResultDto = translationApiService.translate(translationRequestDto);
        assertThat(translationResultDto.getTranslatedText()).isEqualTo("Один два & & & Три? ?");
    }

    @Test
    void shouldReturnTextWithSymbols()
            throws LanguageNotFoundException, TranslationResourceAccessException, ExecutionException,
            InterruptedException, JsonProcessingException {

        translationRequestDto.setIpAddress("0.0.0.0.0.0.1");
        translationRequestDto.setTextToTranslate("~ one `  !  @  # $  % ^  & * ( two ) _  + { }  | \\ : ; \" ' < , > . ! \" № ; % : ? * ( ) - + \\ /  . ,");
        translationRequestDto.setSourceLanguage("en");
        translationRequestDto.setTargetLanguage("ru");

        TranslationResultDto translationResultDto = translationApiService.translate(translationRequestDto);
        assertThat(translationResultDto.getTranslatedText()).isEqualTo("~ один ` ! @ # $ % ^ & * ( два ) _ + { } | \\ : ; \" ' < , > . ! \" № ; % : ? * ( ) - + \\ / . ,");
    }


}
