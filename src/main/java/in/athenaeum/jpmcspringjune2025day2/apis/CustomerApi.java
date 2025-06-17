package in.athenaeum.jpmcspringjune2025day2.apis;

import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerCreateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerUpdateViewModel;
import in.athenaeum.jpmcspringjune2025day2.viewmodels.CustomerViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerApi {
    @GetMapping
    public ResponseEntity<List<CustomerViewModel>> getAll() {
        return ResponseEntity.ok(List.of(new CustomerViewModel()));
    }
    
    @GetMapping("{customerId}")
    public ResponseEntity<CustomerViewModel> getById(@PathVariable int customerId) {
        return ResponseEntity.ok(new CustomerViewModel());
    }
    
    @PostMapping
    public ResponseEntity<CustomerViewModel> create(@RequestBody CustomerCreateViewModel viewModel) {
        return ResponseEntity.ok(new CustomerViewModel());
    }
    
    @PutMapping("{customerId}")
    public ResponseEntity<CustomerViewModel> update(@PathVariable int customerId, @RequestBody CustomerUpdateViewModel viewModel) {
        return ResponseEntity.ok(new CustomerViewModel());
    }
    
    @DeleteMapping("{customerId}")
    public ResponseEntity<Void> deleteById(@PathVariable int customerId) {
        return ResponseEntity.noContent().build();
    }
}
