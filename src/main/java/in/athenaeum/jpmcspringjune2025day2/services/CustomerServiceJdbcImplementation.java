package in.athenaeum.jpmcspringjune2025day2.services;

import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import in.athenaeum.jpmcspringjune2025day2.repositories.CustomerJdbcRepository;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceJdbcImplementation implements CustomerService {
    private final CustomerJdbcRepository customerJdbcRepository;

    public CustomerServiceJdbcImplementation(CustomerJdbcRepository customerJdbcRepository) {
        this.customerJdbcRepository = customerJdbcRepository;
    }

    @Override
    public List<CustomerViewModel> getAll() {
        return customerJdbcRepository
                .getAll()
                .stream()
                .map(this::toViewModel)
                .toList();                
    }

    @Override
    public CustomerViewModel getById(int customerId) {
        return toViewModel(customerJdbcRepository.getById(customerId));
    }

    @Override
    public CustomerViewModel create(CustomerCreateViewModel viewModel) {
        return toViewModel(customerJdbcRepository.create(toDomain(viewModel)));
    }

    @Override
    public CustomerViewModel update(int customerId, CustomerUpdateViewModel viewModel) {
        return null;
    }

    @Override
    public void deleteById(int customerId) {

    }
    
    private CustomerViewModel toViewModel(Customer customer) {
        CustomerViewModel viewModel = new CustomerViewModel();
        BeanUtils.copyProperties(customer, viewModel);
        return viewModel;
    }
    
    private Customer toDomain(CustomerCreateViewModel viewModel) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(viewModel, customer);
        return customer;
    }
}
