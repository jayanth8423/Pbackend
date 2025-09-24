package klu;

import javax.persistence.Column;

@javax.persistence.Entity
public class Customer {

    @javax.persistence.Id
    private String email;

    private String username;
    private String password;
    private String role;
    private String phno;

    @Column(name = "reset_token")
    private String resetToken;

    @Column(length = 1000) 
    private String orders;

    // Constructors
    public Customer() {
    }

    public Customer(String email, String username, String password, String role, String phno, String resetToken, String orders) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.phno = phno;
        this.resetToken = resetToken;
        this.orders = orders;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Customer [email=" + email + ", username=" + username + ", password=" + password +
                ", role=" + role + ", phno=" + phno + ", resetToken=" + resetToken + ", orders=" + orders + "]";
    }
}
