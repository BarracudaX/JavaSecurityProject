package dao;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@Repository
@Transactional
public class SpringComments implements Comments {

    private final NamedParameterJdbcTemplate template;

    public SpringComments(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public List<String> comments() {
        return template.query("SELECT * FROM comments",new CommentsMapper());
    }

    @Override
    public void addComment(String comment) {
        template.update("INSERT INTO COMMENTS VALUES(:comment)", new MapSqlParameterSource(Collections.singletonMap("comment", comment)));
    }

    private static class CommentsMapper implements RowMapper<String>{
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString("comment");
        }
    }
}
