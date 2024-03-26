package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    @ExceptionHandler
    public User getUserByID(Long id) {
        User user = userStorage.findByID(id);
        if (user == null) {
            throw new NotFoundException("Не найден ID: " + id, NOT_FOUND);
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.findAll();
    }

    @Override
    public User createUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.save(user);
    }

    @Override
    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        return userStorage.update(user);
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        checkIds(id, friendId);
        boolean isAdded = userStorage.addFriend(id, friendId);
        if (!isAdded) {
            throw new BadRequestException("Пользователи уже друзья: ", BAD_REQUEST);
        }
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        checkIds(id, friendId);
        userStorage.deleteFriend(id, friendId);
    }

    @Override
    public List<User> getAllFriends(Long id) {
        if (!userStorage.isExistById(id)) {
            throw new NotFoundException("Не найден пользователь по ID: " + id, NOT_FOUND);
        }
        return userStorage.getAllFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long otherId) {
        checkIds(id, otherId);
        return userStorage.getCommonFriends(id, otherId);
    }

    public void checkIds(Long userId1, Long userId2) {
        User user1 = userStorage.findByID(userId1);
        User user2 = userStorage.findByID(userId2);

        if (user1 == null && user2 == null) {
            throw new NotFoundException("Не найден пользователь по ID: " + userId1
                    + ". Не найден пользователь по ID: " + userId2, NOT_FOUND);
        }

        if (user1 == null) {
            throw new NotFoundException("Не найден пользователь по ID: " + userId1, NOT_FOUND);
        }

        if (user2 == null) {
            throw new NotFoundException("Не найден пользователь по ID: " + userId2, NOT_FOUND);
        }
    }
}
