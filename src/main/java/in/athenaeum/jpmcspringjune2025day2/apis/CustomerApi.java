package in.athenaeum.jpmcspringjune2025day2.apis;

import in.athenaeum.jpmcspringjune2025day2.exceptions.RecordNotFoundException;
import in.athenaeum.jpmcspringjune2025day2.services.CustomerService;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerApi {
    private final CustomerService customerService;

    public CustomerApi(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<CustomerViewModel>> getAll() {
        return ResponseEntity.ok(customerService.getAll());
    }
    
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerViewModel> getById(@PathVariable int customerId) {
        return ResponseEntity.ok(customerService.getById(customerId));
    }
    
    @PostMapping
    public ResponseEntity<CustomerViewModel> create(@RequestBody CustomerCreateViewModel viewModel) {
        return ResponseEntity.ok(customerService.create(viewModel));
    }
    
    @PutMapping("{customerId}")
    public ResponseEntity<CustomerViewModel> update(@PathVariable int customerId, @RequestBody CustomerUpdateViewModel viewModel) {
        return ResponseEntity.ok(customerService.update(customerId, viewModel));
    }
    
    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> deleteById(@PathVariable int customerId) {
        customerService.deleteById(customerId);
        return ResponseEntity.noContent().build();
    }
    
    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleRecordNotFoundException(RecordNotFoundException exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException exception) {
        System.out.println(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }
}
