package in.athenaeum.jpmcspringjune2025day2.services;

import in.athenaeum.jpmcspringjune2025day2.aspects.AppLog;
import in.athenaeum.jpmcspringjune2025day2.exceptions.RecordNotFoundException;
import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import in.athenaeum.jpmcspringjune2025day2.repositories.CustomerJpaRepository;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceJpaImplementation implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;
    private final Tracer tracer;
    private final Counter counter;

    public CustomerServiceJpaImplementation(
            CustomerJpaRepository customerJpaRepository,
            Tracer tracer,
            MeterRegistry meterRegistry
    ) {
        this.customerJpaRepository = customerJpaRepository;
        this.tracer = tracer;
        this.counter = meterRegistry.counter("total.customers.created");
    }

    @Override
    public List<CustomerViewModel> getAll() {
        return customerJpaRepository
                .findAll()
                .stream()
                .map(this::toViewModel)
                .toList();
    }

    @AppLog
    @Override
    public CustomerViewModel getById(int customerId) {
        return toViewModel(fromId(customerId));
    }

    @Override
    public CustomerViewModel create(CustomerCreateViewModel viewModel) {
        Span span = tracer.spanBuilder("customers.created")
                .setAttribute("customer.email", viewModel.getEmail())
                .startSpan();

        try {
            span.addEvent("customer.validation.start");
            Customer newCustomer = toDomain(viewModel);
            span.addEvent("customer.validation.end");
            
            span.addEvent("customer.save.start");
            Customer customerDb = customerJpaRepository.saveAndFlush(newCustomer);
            counter.increment();
            span.addEvent("customer.save.end");
            
            span.setAttribute("customer.id", customerDb.getCustomerId());
            return toViewModel(customerDb);
        } catch (RuntimeException ex) {
            span.addEvent("customer.create.error");
            span.recordException(ex);
            throw ex;
        } finally {
            span.end();
        }
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
