package org.bettercare.infrastructure.persistence.repository;

// This repository saves and finds user account data

import org.bettercare.domain.model.UserAccount;
import org.bettercare.business.repository.UserAccountRepository;
import org.bettercare.infrastructure.persistence.rowmapper.UserAccountRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcUserAccountRepository implements UserAccountRepository {
    private static final Logger log = LoggerFactory.getLogger(JdbcUserAccountRepository.class);

    private final JdbcTemplate jdbc;
    private final UserAccountRowMapper accountMapper = new UserAccountRowMapper();
    private final JdbcClient jdbcClient;
    public JdbcUserAccountRepository(JdbcTemplate jdbc, JdbcClient jdbcClient) {
        this.jdbcClient=jdbcClient;
        this.jdbc = jdbc;
    }

    @Override
    public UserAccount findById(int id) {
        String sql = """
        SELECT
            ua.user_id AS ua_id,
            ua.name AS ua_name,
            ua.email AS ua_email,
            ua.password AS ua_password,
            ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_id AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.user_id
        WHERE ua.user_id = ?
    """;

        return jdbc.queryForObject(sql, accountMapper, id);
    }

    @Override
    public List<UserAccount> findAll() {
        String sql = """
        SELECT
            ua.user_id AS ua_id,
            ua.name AS ua_name,
            ua.email AS ua_email,
            ua.password AS ua_password,
            ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_id AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.user_id
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
        String sql = "UPDATE user_accounts SET email = ?, password = ? WHERE user_id = ?";
        jdbc.update(sql, account.getEmail(), account.getPassword(), account.getUserId());
    }

    @Override
    public void delete(Long id) {
        jdbc.update("DELETE FROM user_profile WHERE profile_key = ?", id);
        jdbc.update("DELETE FROM user_accounts WHERE user_id = ?", id);
    }

    @Override
    public UserAccount findByEmail(String email) {
        String sql = """
        SELECT 
            ua.user_id AS ua_id,
            ua.name AS ua_name,
            ua.email AS ua_email,
            ua.password AS ua_password,
            ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_id AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.user_id
        WHERE ua.email = ?
    """;

        return jdbc.queryForObject(sql, accountMapper, email);
    }
    public UserAccount findByName(String name) {
        try {
            return jdbcClient.sql("""
                            SELECT user_id, name, email, password,
                                   date_subscription, receive_email_alerts
                            FROM user_accounts
                            WHERE name = :name
                            """)
                    .param("name", name)
                    .query((rs, rowNum) -> {
                        UserAccount account = new UserAccount();
                        account.setUserId(rs.getInt("user_id"));
                        account.setName(rs.getString("name"));
                        account.setEmail(rs.getString("email"));
                        account.setPassword(rs.getString("password"));
                        Date subDate = rs.getDate("date_subscription");
                        account.setDateSubscription(subDate == null ? null : subDate.toLocalDate());
                        account.setReceiveEmailAlerts(rs.getBoolean("receive_email_alerts"));
                        return account;
                    })
                    .single();
        } catch (Exception e) {
            return null;
        }

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
            log.warn("Login lookup failed", e);
            return null;
        }
    }

    @Override
    public List<UserAccount> findAllWithEmailAlertsEnabled() {

        String sql = """
        SELECT
            ua.user_id AS ua_id,
            ua.name AS ua_name,
            ua.email AS ua_email,
            ua.password AS ua_password,
            ua.receive_email_alerts AS ua_receive_alerts,
            up.profile_id AS up_id,
            up.name AS up_username,
            up.age AS up_age
        FROM user_accounts ua
        LEFT JOIN user_profile up ON up.profile_key = ua.user_id
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