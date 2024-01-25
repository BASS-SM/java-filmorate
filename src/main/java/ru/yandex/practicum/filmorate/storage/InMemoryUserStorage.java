package ru.yandex.practicum.filmorate.storage;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public User findByID(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("не найден ID", HttpStatus.NOT_FOUND);
        }
        return users.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new NotFoundException("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public boolean addFriend(Long id, Long friendId) {
        users.get(id).getListFriends().add(friendId);
        return users.get(friendId).getListFriends().add(id);
    }

    @Override
    public boolean deleteFriend(Long id, Long friendId) {
        users.get(id).getListFriends().remove(friendId);
        return users.get(friendId).getListFriends().remove(id);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        return users.get(id).getListFriends().stream()
                .map(users::get)
                .collect(Collectors.toList());

    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        Set<Long> listUsers = users.get(id).getListFriends();
        return users.get(otherId).getListFriends().stream()
                .filter(listUsers::contains)
                .map(users::get)
                .collect(Collectors.toList());
    }
}
