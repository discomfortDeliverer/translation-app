package discomfortdeliverer.translation_app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import discomfortdeliverer.translation_app.LanguageNotFoundException;
import discomfortdeliverer.translation_app.TranslationRequestDto;
import discomfortdeliverer.translation_app.TranslationResourceAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Service
public class TranslationApiService {
    private static final String TRANSLATE_API_URL = "https://ftapi.pythonanywhere.com/translate";
    private static final int MAX_THREADS = 10;
    private final RestTemplate restTemplate;
    private Map<String, String> languageMap;

    @Autowired
    public TranslationApiService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public String translate(TranslationRequestDto translationRequestDto) throws InterruptedException,
            ExecutionException, JsonProcessingException, LanguageNotFoundException {
        String textToTranslate = translationRequestDto.getTextToTranslate();
        String sourceLanguage = translationRequestDto.getSourceLanguage();
        String targetLanguage = translationRequestDto.getTargetLanguage();

        if(languageMap == null) {
            getAllSupportedLanguages();
        }
        if (!languageMap.containsKey(sourceLanguage) || !languageMap.containsKey(targetLanguage)){
            throw new LanguageNotFoundException();
        }

        List<String> words = Arrays.asList(textToTranslate.split(" "));

        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);

        List<Callable<String>> tasks = words.stream()
                .map(word -> (Callable<String>) () -> translateWord(restTemplate, word, sourceLanguage, targetLanguage))
                .toList();

        List<Future<String>> futures = executorService.invokeAll(tasks);

        StringBuilder sb = new StringBuilder();
        for (Future<String> future : futures) {
            String translatedWordFromJson = getTranslatedWordFromJson(future.get());
            sb.append(translatedWordFromJson);
            sb.append(" ");
            System.out.println(translatedWordFromJson);
        }

        executorService.shutdown();

        return sb.toString().trim();
    }

    private String translateWord(RestTemplate restTemplate, String word, String sl, String tl)
            throws TranslationResourceAccessException {
        StringBuilder urlBuilder = new StringBuilder(TRANSLATE_API_URL);
        urlBuilder.append("?sl=").append(sl)
                .append("&dl=").append(tl)
                .append("&text=").append(word);

        System.out.println(urlBuilder);

        return restTemplate.getForObject(urlBuilder.toString(), String.class);
    }

    private String getTranslatedWordFromJson(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(json);

        return jsonNode.get("destination-text").asText();
    }

    private void getAllSupportedLanguages() {
        String url = "https://ftapi.pythonanywhere.com/languages";
        String allSupportedLanguageJson = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            languageMap = mapper.readValue(allSupportedLanguageJson,
                    new TypeReference<Map<String, String>>() {});

            System.out.println(languageMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
