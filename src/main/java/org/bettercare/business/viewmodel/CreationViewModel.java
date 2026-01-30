package org.bettercare.business.viewmodel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreationViewModel {

        @NotBlank(message = "Email is required")
        @Email(message = "Please enter a valid email address")
        private String email;

        @NotBlank(message="name is required")
        @Size(min = 4,max = 20,message="Name must be between 4 to 20")
        private String name;

        @NotBlank(message="Please make yourself a password")
        private String password;

        public void setName(String name) {
                this.name = name;
        }

        public void setPassword( String password) {
                this.password = password;
        }

        public String getName() {
                return name;
        }

        public String getPassword() {
                return password;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail( String email) {
                this.email = email;
        }
}
