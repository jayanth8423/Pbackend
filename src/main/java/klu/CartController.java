package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/backend/cart")
public class CartController {

    @Autowired
    private CustomerRepo customerRepo;

    @PostMapping("/add")
    public String addToCart(@RequestBody List<OrderRequest> orderRequests) {
        if (orderRequests == null || orderRequests.isEmpty()) {
            return "No orders received!";
        }

        // Assuming all items belong to the same customer (same email)
        String email = orderRequests.get(0).getEmail();
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            return "Customer not found!";
        }

        // Build new orders string
        StringBuilder newOrders = new StringBuilder(
            customer.getOrders() == null ? "" : customer.getOrders() + "; "
        );

        for (OrderRequest order : orderRequests) {
            newOrders.append(order.getProductName())
                     .append(" x ").append(order.getQuantity())
                     .append(" = ₹").append(order.getPrice())
                     .append("; ");
        }

        customer.setOrders(newOrders.toString());
        customerRepo.save(customer);

        return "Orders saved successfully!";
    }

    // ✅ New endpoint to fetch saved orders
    @GetMapping("/{email}")
    public String getCart(@PathVariable String email) {
        Customer customer = customerRepo.findByEmail(email);
        if (customer == null) {
            return "Customer not found!";
        }
        return customer.getOrders() == null ? "" : customer.getOrders();
    }
}
