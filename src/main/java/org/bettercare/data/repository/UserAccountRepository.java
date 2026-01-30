package org.bettercare.data.repository;

import org.bettercare.business.entities.UserAccount;
import org.bettercare.data.rowmappers.UserAccountRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public class UserAccountRepository implements IUserAccountRepository {

    private final JdbcTemplate jdbc;
    private final UserAccountRowMapper accountMapper = new UserAccountRowMapper();
    private final JdbcClient jdbcClient;
    public UserAccountRepository(JdbcTemplate jdbc, JdbcClient jdbcClient) {
        this.jdbcClient=jdbcClient;
        this.jdbc = jdbc;
    }

    @Override
    public UserAccount findById(int id) {
        String sql = """
        SELECT
            ua.profile_key AS ua_id,
            ua.email AS ua_email,
            ua.password AS ua_password,
            up.profile_key AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.profile_key
        WHERE ua.id = ?
    """;

        return jdbc.queryForObject(sql, accountMapper, id);
    }

    @Override
    public List<UserAccount> findAll() {
        String sql = """
        SELECT
            ua.profile_key AS ua_id,
            ua.email AS ua_email,
            ua.password AS ua_password,
        ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_key AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.profile_key
    """;

        return jdbc.query(sql, accountMapper);
    }

    @Override
    public void save(UserAccount account) {
        String sql = "INSERT INTO user_accounts (email, password) VALUES (?, ?)";
        jdbc.update(sql, account.getEmail(), account.getPassword());
    }

    @Override
    public void update(UserAccount account) {
        String sql = "UPDATE user_accounts SET email = ?, password = ? WHERE id = ?";
        jdbc.update(sql, account.getEmail(), account.getPassword(), account.getUserId());
    }

    @Override
    public void delete(Long id) {
        jdbc.update("DELETE FROM user_profile WHERE user_account_id = ?", id);
        jdbc.update("DELETE FROM user_accounts WHERE id = ?", id);
    }

    @Override
    public UserAccount findByEmail(String email) {
        String sql = """
        SELECT 
            ua.profile_key AS ua_id,
            ua.email AS ua_email,
            ua.password AS ua_password,
            up.profile_key AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.profile_key
        WHERE ua.email = ?
    """;

        return jdbc.queryForObject(sql, accountMapper, email);
    }
    public UserAccount findByName(String name) {
      return  jdbcClient.sql("SELECT * FROM user_accounts WHERE name = :name;")
                .param("name",name)
                .query((rs,rowNum)->
                        new UserAccount(rs.getString("password"),rs.getString("email")
                        ,rs.getInt("user_id"),rs.getString("name"))).single();

}

    @Override
    public void insertUserAccount(UserAccount userAccount) {
        String sql = """
                INSERT INTO user_accounts 
                    (name, email, password)
                VALUES (:name, :email, :password)
                """;

        jdbcClient.sql(sql)
                .params(Map.of(
                        "name", userAccount.getName(),
                        "email", userAccount.getEmail(),
                        "password", userAccount.getPassword()
                ))
                .update();
    }



    @Override
    public UserAccount loginVerification(String name, String password) {
        try {
            return jdbcClient.sql("""
                            SELECT user_id, name, email, password,
                                   date_subscription, receive_email_alerts
                            FROM user_accounts 
                            WHERE name = :name AND password = :password
                            """)
                    .params(Map.of("name", name, "password", password))
                    .query((rs, rowNum) -> {
                        UserAccount acc = new UserAccount();
                        acc.setUserId(rs.getInt("user_id"));
                        acc.setName(rs.getString("name"));
                        acc.setEmail(rs.getString("email"));
                        acc.setPassword(rs.getString("password"));
                        Date subDate = rs.getDate("date_subscription");
                        acc.setDateSubscription(subDate == null ? null : subDate.toLocalDate());
                        acc.setReceiveEmailAlerts(rs.getBoolean("receive_email_alerts"));
                        return acc;
                    })
                    .single();
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<UserAccount> findAllWithEmailAlertsEnabled() {

        String sql = """
        SELECT
            ua.profile_key AS ua_id,
            ua.email AS ua_email,
            ua.password AS ua_password,
            ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_key AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.profile_key
        WHERE ua.receive_email_alerts = true
    """;

        return jdbc.query(sql, accountMapper);
    }

    @Override
    public void updateReceiveEmailAlerts(int userId, boolean receiveAlerts) {
        String sql = """
            UPDATE user_accounts
            SET receive_email_alerts = :value
            WHERE user_id = :userId
        """;

        jdbcClient.sql(sql)
                .params(Map.of(
                        "value", receiveAlerts,
                        "userId", userId
                ))
                .update();
    }
}
