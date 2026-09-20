package NeighborhoodWatch.dto;

public class LoginResponse {

    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(
            Long userId,
            String fullName,
            String email,
            String phoneNumber,
            String role) {

        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }
}
