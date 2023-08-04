package ru.practicum.ewm.main.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.main.service.event.dto.SortParam;
import ru.practicum.ewm.main.service.user.dto.UserDto;
import ru.practicum.ewm.main.service.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) List<Integer> ids,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                     @RequestParam(defaultValue = "10") @Positive int size,
                                                     @RequestParam(required = false) SortParam sortParam) {
        return ResponseEntity.ok(service.getAll(ids, from, size, sortParam));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody @Valid UserDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("userId") @Positive int userId) {
        service.deleteById(userId);
    }

}
