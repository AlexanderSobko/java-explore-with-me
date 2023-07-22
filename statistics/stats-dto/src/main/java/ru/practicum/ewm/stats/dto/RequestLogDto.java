package ru.practicum.ewm.stats.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;

@Getter
@Setter
@Builder
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestLogDto {

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @NotBlank(message = "Названее сервиса не может быть пустым!")
    String app;
    @NotBlank(message = "URI не может быть пустым!")
    String uri;
    @NotBlank(message = "Ip не может быть пустым!")
    String ip;

    @AssertTrue(message = "IP адрес должен быть корректным!")
    public boolean isValidateIp() {
        if (ip == null) {
            return true;
        } else {
            String regex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
            if (ip.matches(regex)) {
                return Arrays.stream(ip.split("\\."))
                        .allMatch(num -> Integer.parseInt(num) >= 0 && Integer.parseInt(num) <= 255);
            } else {
                return false;
            }
        }
    }

}
