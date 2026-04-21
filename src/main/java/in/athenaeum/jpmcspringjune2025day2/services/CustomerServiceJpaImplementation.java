package in.athenaeum.jpmcspringjune2025day2.services;

import in.athenaeum.jpmcspringjune2025day2.aspects.AppLog;
import in.athenaeum.jpmcspringjune2025day2.exceptions.RecordNotFoundException;
import in.athenaeum.jpmcspringjune2025day2.models.Customer;
import in.athenaeum.jpmcspringjune2025day2.repositories.CustomerJpaRepository;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

@Service
public class CustomerServiceJpaImplementation implements CustomerService {
    private final CustomerJpaRepository customerJpaRepository;
    private final Tracer tracer;
    private final Counter counter;
    private final Deque<Customer> pendingCustomers = new LinkedList<>();
    private final MeterRegistry meterRegistry;
    private final DistributionSummary customerAddLatency;

    public CustomerServiceJpaImplementation(
            CustomerJpaRepository customerJpaRepository,
            Tracer tracer,
            MeterRegistry meterRegistry
    ) {
        this.customerJpaRepository = customerJpaRepository;
        this.tracer = tracer;
        this.counter = meterRegistry.counter("total.customers.created");
        this.meterRegistry = meterRegistry;
        
        this.customerAddLatency = DistributionSummary
                .builder("customer.add.latency")
                .description("The latency of adding a customer")
                .tag("service", "customer")
                .serviceLevelObjectives(1, 10, 20, 50, 100, 500)
                .publishPercentiles(0.5, 0.75, 0.95, 0.99)
                .register(meterRegistry);       
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
        double startTime = System.currentTimeMillis();
        
        
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
            
            pendingCustomers.add(customerDb);
            
            double endTime = System.currentTimeMillis();
            
            customerAddLatency.record(endTime - startTime);
            
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
    
    @PostConstruct
    public void init() {
        Gauge.builder("customer.pending.customers", pendingCustomers, Deque::size)
                .tag("service", "customer")
                .register(meterRegistry);
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
