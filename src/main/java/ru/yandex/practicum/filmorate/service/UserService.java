package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User getUserByID(Long id);

    List<User> getAllUsers();

    User createUser(User user);

    User updateUser(User user);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

    List<User> getAllFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
