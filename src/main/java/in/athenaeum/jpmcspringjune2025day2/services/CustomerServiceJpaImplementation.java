package in.athenaeum.jpmcspringjune2025day2.services;

import in.athenaeum.jpmcspringjune2025day2.exceptions.RecordNotFoundException;
import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import in.athenaeum.jpmcspringjune2025day2.repositories.CustomerJpaRepository;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import org.springframework.beans.BeanUtils;
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
        return customerJpaRepository
                .findAll()
                .stream()
                .map(this::toViewModel)
                .toList();
    }

    @Override
    public CustomerViewModel getById(int customerId) {
        return toViewModel(fromId(customerId));
    }

    @Override
    public CustomerViewModel create(CustomerCreateViewModel viewModel) {
        return toViewModel(customerJpaRepository.saveAndFlush(toDomain(viewModel)));
    }

    @Override
    public CustomerViewModel update(int customerId, CustomerUpdateViewModel viewModel) {
        Customer customerDb = fromId(customerId);
                
        customerDb.setFirstName(viewModel.getFirstName());
        customerDb.setLastName(viewModel.getLastName());
        customerDb.setCity(viewModel.getCity());

        return toViewModel(customerJpaRepository.saveAndFlush(customerDb));
    }

    @Override
    public void deleteById(int customerId) {
        customerJpaRepository.delete(fromId(customerId));
    }
    
    private Customer fromId(int customerId) {
        return customerJpaRepository
                .findById(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Could not find the customer: " + customerId));
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
