package org.bettercare.infrastructure.persistence.rowmapper;

import org.bettercare.domain.model.enums.SkinColor;
import org.bettercare.domain.model.UserProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRowMapper implements RowMapper<UserProfile> {

    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setProfileId(rs.getInt("profile_id"));
        profile.setName(rs.getString("name"));
        profile.setSkinColor(SkinColor.valueOf(rs.getString("skin_color")));
        profile.setAge(rs.getInt("age"));
        return profile;
    }
}