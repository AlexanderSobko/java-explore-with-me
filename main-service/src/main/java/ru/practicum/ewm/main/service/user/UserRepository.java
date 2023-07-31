package ru.practicum.ewm.main.service.user;

import ru.practicum.ewm.main.service.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    List<User> findAllByIdIn(List<Integer> ids);

    boolean existsByEmail(String email);

}
