package org.bettercare.business.services;

import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.UserProfile;
import org.bettercare.data.repository.IUserProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProfileService {

    private final IUserProfileRepository repo;

    public UserProfileService(IUserProfileRepository repo) {
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