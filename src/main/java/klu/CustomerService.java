package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public Customer registerCustomer(Customer customer) {
        if (customerRepo.existsByEmail(customer.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        if ("admin".equalsIgnoreCase(customer.getRole())) {
            throw new RuntimeException("Invalid role: Cannot assign 'admin' role");
        }

        
        return customerRepo.save(customer);
    }
}
