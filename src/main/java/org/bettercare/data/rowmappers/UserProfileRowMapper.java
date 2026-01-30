package org.bettercare.data.rowmappers;

import org.bettercare.business.entities.enums.SKIN_COLOR;
import org.bettercare.business.entities.UserProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProfileRowMapper implements RowMapper<UserProfile> {

    @Override
    public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserProfile profile = new UserProfile();
        profile.setProfileId(rs.getInt("profile_id"));
        profile.setName(rs.getString("name"));
        profile.setSkinColor(SKIN_COLOR.valueOf(rs.getString("skin_color")));
        profile.setAge(rs.getInt("age"));
        return profile;
    }
}
