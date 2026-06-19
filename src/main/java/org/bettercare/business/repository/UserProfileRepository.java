package org.bettercare.business.repository;

import org.bettercare.domain.model.UserProfile;

import java.util.List;

public interface UserProfileRepository {
    UserProfile findById(int id);
    List<UserProfile> findAll();
    void save(UserProfile userProfile);
    void update(UserProfile userProfile);
    void delete(Long id);
    List<UserProfile> findByUserAccountId(int userAccountId);
}