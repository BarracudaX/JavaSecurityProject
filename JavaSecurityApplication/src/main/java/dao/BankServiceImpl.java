package dao;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Transactional
public class BankServiceImpl implements BankService {

    private final NamedParameterJdbcTemplate template;

    public BankServiceImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public long getAmount(String username) {
        return template.queryForObject(
                "SELECT amount FROM BANK_ACCOUNT WHERE username = :username",
                new MapSqlParameterSource(Collections.singletonMap("username",username)),
                Long.class
        );
    }

    @Override
    public void transfer(String from, String to, long amount) {

        if (!accountExists(from) || !accountExists(to)) {
            throw new IllegalArgumentException("Can't transfer from/to a non-existing account.");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("param1", amount);
        params.put("from", from);
        template.update("UPDATE BANK_ACCOUNT SET amount = amount - :param1 WHERE username = :from", params);

        params = new HashMap<>();
        params.put("param1", amount);
        params.put("to", to);
        template.update("UPDATE BANK_ACCOUNT SET amount = amount + :param1 WHERE username = :to", params);
    }

    @Override
    public boolean accountExists(String username) {
        return template.queryForObject("SELECT 1 as account FROM BANK_ACCOUNT WHERE username = :username"
                ,new MapSqlParameterSource(Collections.singletonMap("username",username)),
                (rs, rowNum) -> rs.getLong("account")) == 1;
    }
}
