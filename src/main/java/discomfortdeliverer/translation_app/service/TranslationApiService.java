package discomfortdeliverer.translation_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discomfortdeliverer.translation_app.exceptions.LanguageNotFoundException;
import discomfortdeliverer.translation_app.dto.TranslationRequestDto;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.exceptions.TranslationResourceAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class TranslationApiService {
    private static final String TRANSLATE_API_URL = "https://ftapi.pythonanywhere.com/translate";
    private static final String LANGUAGES_API_URL = "https://ftapi.pythonanywhere.com/languages";
    private static final int MAX_THREADS = 10;
    private final RestTemplate restTemplate;
    private Map<String, String> languageMap;
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);

    @Autowired
    public TranslationApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public TranslationResultDto translate(TranslationRequestDto translationRequestDto)
            throws InterruptedException, ExecutionException, JsonProcessingException, LanguageNotFoundException,
            TranslationResourceAccessException {

        String textToTranslate = translationRequestDto.getTextToTranslate();
        String sourceLanguage = translationRequestDto.getSourceLanguage();
        String targetLanguage = translationRequestDto.getTargetLanguage();

        if(languageMap == null) {
            getSupportedLanguages();
        }
        if (!languageMap.containsKey(sourceLanguage)){
            log.info("Указанный язык={} не поддерживается", sourceLanguage);
            throw new LanguageNotFoundException("Не найден язык исходного сообщения " + sourceLanguage);
        } else if (!languageMap.containsKey(targetLanguage)) {
            log.info("Указанный язык={} не поддерживается", targetLanguage);
            throw new LanguageNotFoundException("Не найден язык целевого сообщения " + targetLanguage);
        }

        List<String> words = Arrays.asList(textToTranslate.split("\\s+"));

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        List<Callable<String>> tasks = words.stream()
                .map(word -> (Callable<String>) () -> translateWord(restTemplate, word, sourceLanguage, targetLanguage))
                .toList();

        List<Future<String>> futures = executorService.invokeAll(tasks);

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            String translatedWord = getTranslatedWordFromJson(future.get());
            translatedText.append(translatedWord);
            translatedText.append(" ");
            System.out.println(translatedWord);
        }

        executorService.shutdown();
        TranslationResultDto translationResultDto = convertDto(translationRequestDto);

        translationResultDto.setTranslatedText(translatedText.toString().trim());
        log.info("Результат перевода={}", translationResultDto.getTranslatedText());
        return translationResultDto;
    }

    private TranslationResultDto convertDto(TranslationRequestDto translationRequestDto) {
        TranslationResultDto translationResultDto = new TranslationResultDto();
        translationResultDto.setSourceLanguage(translationRequestDto.getSourceLanguage());
        translationResultDto.setTargetLanguage(translationRequestDto.getTargetLanguage());
        translationResultDto.setTextToTranslate(translationRequestDto.getTextToTranslate());
        translationResultDto.setIpAddress(translationRequestDto.getIpAddress());

        return translationResultDto;
    }

    private String translateWord(RestTemplate restTemplate, String word, String sl, String tl) {
        // Чтобы & не ломал параметр запроса к API
        if(word.equals("&")) return word;

        StringBuilder urlBuilder = new StringBuilder(TRANSLATE_API_URL);

        urlBuilder.append("?sl=").append(sl)
                .append("&dl=").append(tl)
                .append("&text=").append(word);

        System.out.println(urlBuilder);

        return restTemplate.getForObject(urlBuilder.toString(), String.class);
    }

    private String getTranslatedWordFromJson(String json) throws JsonProcessingException {
        if(json.equals("&")) return json;

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        return jsonNode.get("destination-text").asText();
    }

    private void getSupportedLanguages() throws TranslationResourceAccessException {
        String supportedLanguageJson = restTemplate.getForObject(LANGUAGES_API_URL, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            languageMap = mapper.readValue(supportedLanguageJson,
                    new TypeReference<Map<String, String>>() {});

            System.out.println(languageMap);
        } catch (IOException e) {
            throw new TranslationResourceAccessException("Ошибка получения списка всех доступных языков", e);
        }
    }

    public Map<String, String> getLanguageMap() {
        if (languageMap == null) {
            try {
                getSupportedLanguages();
            } catch (TranslationResourceAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return languageMap;
    }
}
