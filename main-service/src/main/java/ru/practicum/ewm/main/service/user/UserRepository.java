package ru.practicum.ewm.main.service.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.main.service.user.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByIdIn(List<Integer> ids);

    boolean existsByEmail(String email);

}
