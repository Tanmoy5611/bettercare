package org.bettercare.data.repository;

import org.bettercare.business.entities.UserProfile;

import java.util.List;

public interface IUserProfileRepository {
    UserProfile findById(int id);
    List<UserProfile> findAll();
    void save(UserProfile userProfile);
    void update(UserProfile userProfile);
    void delete(Long id);
    List<UserProfile> findByUserAccountId(int userAccountId);
}
