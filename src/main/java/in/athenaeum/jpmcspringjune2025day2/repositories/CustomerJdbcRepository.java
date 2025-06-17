package in.athenaeum.jpmcspringjune2025day2.repositories;

import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CustomerJdbcRepository {
    private final RowMapper<Customer> customerRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public CustomerJdbcRepository(RowMapper<Customer> customerRowMapper, JdbcTemplate jdbcTemplate) {
        this.customerRowMapper = customerRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }
}
