package in.athenaeum.jpmcspringjune2025day2.repositories;

import in.athenaeum.jpmcspringjune2025day2.exceptions.RecordNotFoundException;
import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class CustomerJdbcRepository {
    private final RowMapper<Customer> customerRowMapper;
    private final JdbcTemplate jdbcTemplate;

    public CustomerJdbcRepository(RowMapper<Customer> customerRowMapper, JdbcTemplate jdbcTemplate) {
        this.customerRowMapper = customerRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Customer> getAll() {
        String sql = "SELECT customer_id, first_name, last_name, email, city FROM customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    public Customer getById(int customerId) {
        String sql = "SELECT customer_id, first_name, last_name, email, city FROM customer WHERE customer_id=?";
        try {
            return jdbcTemplate.queryForObject(sql, customerRowMapper, customerId);
        } catch (EmptyResultDataAccessException exception) {
            System.out.println(exception.getMessage());
            throw new RecordNotFoundException(String.format("Customer with id %d is not found", customerId));
        }

    }

    public Customer create(Customer customer) {
        String sql = "INSERT INTO customer (first_name, last_name, email, city) VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getCity());
            return ps;
        }, keyHolder);

        // Retrieve the generated customer ID
        Number key = keyHolder.getKey();
        if (key != null) {
            customer.setCustomerId(key.intValue());
        }

        return customer;
    }

    public Customer updateCustomer(Customer customer) {
        String sql = "UPDATE public.customers SET first_name = ?, last_name = ?, email =?, city =? WHERE customer_id=?";
        this.jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName(),
                customer.getEmail(), customer.getCity(), customer.getCustomerId());
        return customer;
    }

    public void deleteById(int customerId) {
        String sql = "DELETE FROM customer WHERE customer_id=?";
        int rows = jdbcTemplate.update(sql, customerId);

        if (rows == 0) {
            throw new RecordNotFoundException(String.format("Customer with id %d is not found", customerId));
        }
    }
}
