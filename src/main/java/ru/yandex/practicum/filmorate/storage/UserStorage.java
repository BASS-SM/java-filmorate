package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User findByID(Long id);

    List<User> findAll();

    User save(User user);

    User update(User user);

    boolean addFriend(Long id, Long friendId);

    boolean deleteFriend(Long id, Long friendId);

    List<User> getAllFriends(Long id);

    List<User> getCommonFriends(Long id, Long otherId);
}
