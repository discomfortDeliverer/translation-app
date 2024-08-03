package discomfortdeliverer.translation_app.repository;

import discomfortdeliverer.translation_app.Translation;
import discomfortdeliverer.translation_app.dto.TranslationResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TranslationRepository {
    private final JdbcTemplate jdbcTemplate;

    public TranslationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public TranslationResultDto saveTranslation(TranslationResultDto translationResultDto) {
        String sql = "INSERT INTO translations (ip_address, source_text, translated_text) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, translationResultDto.getIpAddress(), translationResultDto.getTextToTranslate(),
                translationResultDto.getTranslatedText());
        return translationResultDto;
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
