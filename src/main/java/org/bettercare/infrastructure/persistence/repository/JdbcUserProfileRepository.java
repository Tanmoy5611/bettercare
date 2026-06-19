package org.bettercare.infrastructure.persistence.repository;

import org.bettercare.domain.model.UserProfile;
import org.bettercare.business.repository.UserProfileRepository;
import org.bettercare.infrastructure.persistence.rowmapper.UserProfileRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcUserProfileRepository implements UserProfileRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserProfileRowMapper rowMapper = new UserProfileRowMapper();

    public JdbcUserProfileRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserProfile findById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT profile_id, name, skin_color, age, profile_key FROM user_profile WHERE profile_id = ?",
                rowMapper,
                id
        );
    }

    @Override
    public List<UserProfile> findAll() {
        return jdbcTemplate.query(
                "SELECT profile_id, name, skin_color, age FROM user_profile",
                rowMapper
        );
    }

    // Save a UserProfile (userAccountId must already be set)
    @Override
    public void save(UserProfile profile) {
        jdbcTemplate.update(
                "INSERT INTO user_profile (name, skin_color, age" +
                        ", profile_key" +
                        ") VALUES (?, ?, ?, ?)",
                profile.getName(),
                profile.getSkinColor().name(),
                profile.getAge(),
                profile.getUserAccountId()
        );
    }

    // Update a UserProfile (userAccountId must already be set)
    @Override
    public void update(UserProfile profile) {
        jdbcTemplate.update(
                "UPDATE user_profile SET name = ?, skin_color = ?, age = ? WHERE profile_id = ?",
                profile.getName(),
                profile.getSkinColor().name(),
                profile.getAge(),
                profile.getProfileId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM user_profile WHERE profile_id = ?", id);
    }

    // Optional helper method to find profile by userAccountId
    @Override
    public List<UserProfile> findByUserAccountId(int userAccountId) {
        return jdbcTemplate.query(
                "SELECT profile_id, name, skin_color, age, profile_key FROM user_profile WHERE profile_key = ?",
                rowMapper,
                userAccountId
        );
    }
}