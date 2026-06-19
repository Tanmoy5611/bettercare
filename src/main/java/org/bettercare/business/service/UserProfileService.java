package org.bettercare.business.service;

import org.bettercare.domain.model.UserAccount;
import org.bettercare.domain.model.UserProfile;
import org.bettercare.business.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final UserProfileRepository repo;

    public UserProfileService(UserProfileRepository repo) {
        this.repo = repo;
    }

    public UserProfile findById(int id) {
        return repo.findById(id);
    }

    public List<UserProfile> findAll() {
        return repo.findAll();
    }

    public void save(UserProfile profile) {
        repo.save(profile);
    }

    public void update(UserProfile profile, UserAccount account) {
        if (account != null) {
            profile.setUserAccountId(account.getUserId());
        }
        repo.update(profile);
    }

    public void delete(Long id) {
        repo.delete(id);
    }

    public List<UserProfile> getProfileByUserId(int userId) {
        return repo.findByUserAccountId(userId);
    }
}