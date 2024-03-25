package ru.yandex.practicum.filmorate.storage.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor

public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findByID(Long id) {
        String sqlQuery = "SELECT * FROM USERS WHERE id = ?";
        List<User> result = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if (result.size() != 1) {
            throw new NotFoundException("Пользователь не найден", HttpStatus.NOT_FOUND);
        } else
            return result.get(0);
    }


    @Override
    public List<User> findAll() {
        String sqlQuery = "select * from USERS";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User save(User user) {
        String sqlQuery = "insert into USERS (NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "update USERS set " +
                "NAME = ?, EMAIL = ?, LOGIN = ?, BIRTHDAY = ?" +
                "where id = ?";
        int update = jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId());
        if (update > 0) {
            return user;
        } else throw new NotFoundException("Пользователь не обновлен", HttpStatus.NOT_FOUND);
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        String sqlQuery = "INSERT INTO FRIENDSHIP(USER_ID, FRIEND_ID) " +
                "SELECT ?, ? " +
                "FROM DUAl " +
                "WHERE NOT EXISTS " +
                "(SELECT ? FROM " +
                "FRIENDSHIP " +
                "WHERE (USER_ID = ? " +
                "AND FRIEND_ID = ?) " +
                "OR (USER_ID = ? " +
                "AND FRIEND_ID = ?))";
        return jdbcTemplate.update(sqlQuery, id, friendId, id, id, friendId, friendId, id) > 0;
    }


    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        String sqlString = "DELETE FROM FRIENDSHIP WHERE USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(sqlString, id, friendId);
        return true;

    }

    @Override
    public List<User> getAllFriends(Long id) {
        String sqlString = "SELECT USERS.id, email, login, name, birthday " +
                "FROM USERS " +
                "LEFT JOIN friendship f on users.id = f.friend_id " +
                "where f.user_id = ?";
        return jdbcTemplate.query(sqlString, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        String sqlString = "SELECT u.id, email, login, name, birthday " +
                "FROM friendship AS f " +
                "LEFT JOIN users u ON u.id = f.friend_id " +
                "WHERE f.user_id = ? AND f.friend_id IN ( " +
                "SELECT friend_id " +
                "FROM friendship AS f " +
                "LEFT JOIN users AS u ON u.id = f.friend_id " +
                "WHERE f.user_id = ? )";

        return jdbcTemplate.query(sqlString, this::mapRowToUser, id, otherId);
    }

    private User mapRowToUser(ResultSet resultSet, int i) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("NAME"))
                .email(resultSet.getString("EMAIL"))
                .login(resultSet.getString("LOGIN"))
                .birthday(resultSet.getDate("BIRTHDAY").toLocalDate())
                .build();
    }

    @Override
    public boolean isExistById(Long id) {
        String sqlQuery = "SELECT EXISTS(SELECT 1 FROM USERS WHERE ID = ?)";
        return jdbcTemplate.queryForObject(sqlQuery, Boolean.class, id);
    }
}
