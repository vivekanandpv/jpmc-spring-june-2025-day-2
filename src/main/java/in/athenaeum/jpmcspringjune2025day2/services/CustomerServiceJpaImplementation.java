package in.athenaeum.jpmcspringjune2025day2.services;

import in.athenaeum.jpmcspringjune2025day2.repositories.CustomerJpaRepository;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceJpaImplementation implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;

    public CustomerServiceJpaImplementation(CustomerJpaRepository customerJpaRepository) {
        this.customerJpaRepository = customerJpaRepository;
    }

    @Override
    public List<CustomerViewModel> getAll() {
        return List.of();
    }

    @Override
    public CustomerViewModel getById(int customerId) {
        return null;
    }

    @Override
    public CustomerViewModel create(CustomerCreateViewModel viewModel) {
        return null;
    }

    @Override
    public CustomerViewModel update(int customerId, CustomerUpdateViewModel viewModel) {
        return null;
    }

    @Override
    public void deleteById(int customerId) {

    }
}
