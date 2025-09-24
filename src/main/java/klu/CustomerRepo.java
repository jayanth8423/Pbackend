package klu;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, String> {
    boolean existsByEmail(String email);
    Customer findByResetToken(String resetToken);
	Customer findByEmail(String email);
}