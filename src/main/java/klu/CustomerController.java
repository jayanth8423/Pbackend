package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/backend/customers")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepo customerRepo;


    private static final String ADMIN_USERNAME = "jsn";
    private static final String ADMIN_PASSWORD = "jsn";
    private static final String ADMIN_EMAIL = "jsnstore@gmail.com";
    private static final String ADMIN_ROLE = "Admin";


    
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody Customer customer) {
        try {
            customer.setPassword(customer.getPassword());
            customerService.registerCustomer(customer);
            return ResponseEntity.ok(Map.of("message", "User registered successfully!"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Customer loginRequest) {
        if (ADMIN_EMAIL.equalsIgnoreCase(loginRequest.getEmail()) &&
            ADMIN_PASSWORD.equals(loginRequest.getPassword())) {

            Customer adminUser = new Customer();
            adminUser.setEmail(ADMIN_EMAIL);
            adminUser.setUsername(ADMIN_USERNAME);
            adminUser.setRole(ADMIN_ROLE);

            return ResponseEntity.ok(adminUser);
        }

        Customer user = customerRepo.findByEmail(loginRequest.getEmail());
        if (user == null || loginRequest.getPassword().equals(user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        return ResponseEntity.ok(user);
    }

    // ✅ Forgot password
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        Customer customer = customerRepo.findByEmail(email);

        if (customer == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Email not found."));
        }

        String resetToken = UUID.randomUUID().toString();
        customer.setResetToken(resetToken);
        customerRepo.save(customer);

        String resetLink = "http://localhost:8081/backend/customers/reset-password?token=" + resetToken;
        String subject = "Password Recovery - JSN Store";
        String body = "Hello " + customer.getUsername() + ",\n\n"
                + "Click the link below to reset your password:\n"
                + resetLink + "\n\n"
                + "If you did not request a password reset, please ignore this email.\n\n"
                + "Regards,\nJSN Store Team";

        try {
            
            return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email."));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to send email: " + e.getMessage()));
        }
    }

    // ✅ Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestBody Map<String, String> payload) {
        Customer customer = customerRepo.findByResetToken(token);
        if (customer == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Invalid or expired token."));
        }

        String newPassword = payload.get("newPassword");
        customer.setPassword(newPassword);
        customer.setResetToken(null);
        customerRepo.save(customer);
        return ResponseEntity.ok(Map.of("message", "Password has been successfully updated."));
    }

    // ✅ Get all users (Admin only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers(@RequestHeader("jsnstore@gmail.com") String email,
                                         @RequestHeader("jsn") String password) {
        if (ADMIN_EMAIL.equals(email) && ADMIN_PASSWORD.equals(password)) {
            return ResponseEntity.ok(customerRepo.findAll());
        }
        return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
    }

    // ✅ Update role
    @PutMapping("/update-role/{email}")
    public ResponseEntity<?> updateRole(@PathVariable String email,
                                        @RequestBody Map<String, String> req,
                                        @RequestHeader("jsnstore@gmail.com") String adminEmail,
                                        @RequestHeader("jsn") String adminPassword) {
        if (!(ADMIN_EMAIL.equals(adminEmail) && ADMIN_PASSWORD.equals(adminPassword))) {
            return ResponseEntity.status(403).body(Map.of("error", "Unauthorized"));
        }

        Customer user = customerRepo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "User not found"));
        }

        String role = req.get("role");
        user.setRole(role);
        customerRepo.save(user);
        return ResponseEntity.ok(Map.of("message", "Role updated successfully"));
    }
}
