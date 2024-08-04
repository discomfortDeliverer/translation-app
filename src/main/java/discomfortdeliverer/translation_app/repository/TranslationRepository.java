package discomfortdeliverer.translation_app.repository;

import discomfortdeliverer.translation_app.model.Translation;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import discomfortdeliverer.translation_app.service.TranslationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TranslationRepository {
    private static final Logger log = LoggerFactory.getLogger(TranslationService.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TranslationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String saveTranslation(Translation translation) {
        String sql = "INSERT INTO translations (ip_address, source_text, translated_text) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, translation.getIpAddress(), translation.getSourceText(),
                translation.getTranslatedText());
        log.info("В базу данных добавлена строка: {}, {}, {}",translation.getIpAddress(), translation.getSourceText(),
                translation.getTranslatedText());
        return translation.getTranslatedText();
    }

    public List<Translation> findAllTranslation() {
        String sql = "SELECT * FROM translations";
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new Translation(
                        rs.getString("ip_address"),
                        rs.getString("source_text"),
                        rs.getString("translated_text")
                )
        );
    }
}
