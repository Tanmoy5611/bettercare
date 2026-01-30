package org.bettercare.business.viewmodel;
import jakarta.validation.constraints.NotBlank;

public class LoginViewModel {

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "password is required")
    private String password;

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}