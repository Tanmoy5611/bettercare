package org.bettercare.business.repository;

import org.bettercare.domain.model.UserAccount;

import java.util.List;

public interface UserAccountRepository {
    UserAccount findById(int id);

    List<UserAccount> findAll();

    void save(UserAccount userAccount);

    void update(UserAccount userAccount);

    void delete(Long id);

    UserAccount findByEmail(String email);

    UserAccount findByName(String name);

    List<UserAccount> findAllWithEmailAlertsEnabled();

    void insertUserAccount(UserAccount userAccount);

    void updateReceiveEmailAlerts(int userId, boolean receiveAlerts);

    UserAccount loginVerification(String name, String password);

}