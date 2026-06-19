package org.bettercare.business.service;

import org.bettercare.domain.model.UserAccount;
import org.bettercare.business.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    private final UserAccountRepository repo;
    private final PasswordService passwordService;


    public UserAccountService(UserAccountRepository repo,
                              PasswordService passwordService) {
        this.repo = repo;
        this.passwordService = passwordService;
    }

    public UserAccount findById(int id) {
        return repo.findById(id);
    }

    public List<UserAccount> findAll() {
        return repo.findAll();
    }

    public void save(UserAccount account) {
        repo.save(account);
    }

    public void update(UserAccount account) {
        repo.update(account);
    }

    public void delete(Long id) {
        repo.delete(id);
    }

    public UserAccount findByEmail(String email) {
        return repo.findByEmail(email);
    }

    public UserAccount findByName(String name){return repo.findByName(name);}

    public UserAccount getUserById(int id) {
        return repo.findById(id);
    }

    public void insertUserAccount(UserAccount userAccount){
        // Never send the plain password to the database
        userAccount.setPassword(passwordService.hash(userAccount.getPassword()));
        repo.insertUserAccount(userAccount);
    }

    public UserAccount loginVerification(String name, String password){
        // First find the user, then compare the hashed password
        UserAccount account = repo.findByName(name);
        if (account == null || !passwordService.matches(password, account.getPassword())) {
            return null;
        }
        return account;
    }

    public List<UserAccount> getUsersWithEmailAlertsEnabled() {
        return repo.findAllWithEmailAlertsEnabled();
    }

    public void updateEmailAlertPreference(int userId, boolean receiveAlerts) {
        repo.updateReceiveEmailAlerts(userId, receiveAlerts);
    }

    public void updateReceiveEmailAlerts(int userId, boolean receiveAlerts){repo.updateReceiveEmailAlerts(userId,receiveAlerts);}
}