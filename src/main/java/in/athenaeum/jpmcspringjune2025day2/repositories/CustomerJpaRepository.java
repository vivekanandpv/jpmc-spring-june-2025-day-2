package in.athenaeum.jpmcspringjune2025day2.repositories;

import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerJpaRepository extends JpaRepository<Customer, Integer> {
}
