package org.bettercare.data.rowmappers;

import org.bettercare.business.entities.UserAccount;
import org.bettercare.business.entities.UserProfile;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountRowMapper implements RowMapper<UserAccount> {

    @Override
    public UserAccount mapRow(ResultSet rs, int rowNum) throws SQLException {

        UserAccount account = new UserAccount();

        account.setUserId(rs.getInt("ua_id"));
        account.setEmail(rs.getString("ua_email"));
        account.setPassword(rs.getString("ua_password"));

        account.setReceiveEmailAlerts(rs.getBoolean("ua_receive_alerts"));

        int profileId = rs.getInt("up_id");
        if (profileId != 0) {
            UserProfile profile = new UserProfile();
            profile.setProfileId(profileId);
            profile.setName(rs.getString("up_username"));
            profile.setAge(rs.getInt("up_age"));
            profile.setUserAccountId(account.getUserId());
            account.setProfile(profile);
        }

        return account;
    }
}