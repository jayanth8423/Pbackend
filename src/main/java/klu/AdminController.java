package klu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/backend/api/admin")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AdminController {

    private static final String ADMIN_EMAIL = "jsnstore@gmail.com";
    private static final String ADMIN_USERNAME = "jsn";
    private static final String ADMIN_PASSWORD = "jsn";

    @Autowired
    private CustomerRepo customerRepo;

    private boolean isAdmin(String email, String username, String password) {
        return ADMIN_EMAIL.equals(email) &&
               ADMIN_USERNAME.equals(username) &&
               ADMIN_PASSWORD.equals(password);
    }

    // ✅ Get all users
    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers(
            @RequestHeader("X-Admin-Email") String adminEmail,
            @RequestHeader("X-Admin-Username") String adminUsername,
            @RequestHeader("X-Admin-Password") String adminPassword) {

        if (!isAdmin(adminEmail, adminUsername, adminPassword)) {
            return ResponseEntity.status(403).body("Unauthorized: Only admin can view all users");
        }

        List<Customer> users = customerRepo.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.ok("No users found in the database.");
        }
        return ResponseEntity.ok(users);
    }

    // ✅ Update role of a user
    @PutMapping("/update-customer-role/{email}")
    public ResponseEntity<?> updateRole(
            @PathVariable String email,
            @RequestBody RoleUpdateRequest req,
            @RequestHeader("X-Admin-Email") String adminEmail,
            @RequestHeader("X-Admin-Username") String adminUsername,
            @RequestHeader("X-Admin-Password") String adminPassword) {

        if (!isAdmin(adminEmail, adminUsername, adminPassword)) {
            return ResponseEntity.status(403).body("Unauthorized: Only admin can update roles");
        }

        Customer user = customerRepo.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setRole(req.getRole());
        customerRepo.save(user);

        return ResponseEntity.ok("Role updated successfully for " + email);
    }
}
