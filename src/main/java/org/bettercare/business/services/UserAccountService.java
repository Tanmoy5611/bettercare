package org.bettercare.business.services;

import org.bettercare.business.entities.UserAccount;
import org.bettercare.data.repository.IUserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAccountService {

    private final IUserAccountRepository repo;


    public UserAccountService(IUserAccountRepository repo) {
        this.repo = repo;
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

    public void insertUserAccount(UserAccount userAccount){repo.insertUserAccount(userAccount);}

    public UserAccount loginVerification(String name, String password){return repo.loginVerification(name, password);}

    public List<UserAccount> getUsersWithEmailAlertsEnabled() {
        return repo.findAllWithEmailAlertsEnabled();
    }

    public void updateEmailAlertPreference(int userId, boolean receiveAlerts) {
        repo.updateReceiveEmailAlerts(userId, receiveAlerts);
    }

    public void updateReceiveEmailAlerts(int userId, boolean receiveAlerts){repo.updateReceiveEmailAlerts(userId,receiveAlerts);}
}