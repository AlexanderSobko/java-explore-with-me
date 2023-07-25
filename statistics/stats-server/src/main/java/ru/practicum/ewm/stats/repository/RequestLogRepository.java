package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.ewm.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {

    @Query("SELECT new ru.practicum.ewm.stats.dto.StatsDto(rl.app, rl.uri," +
                  "CASE WHEN :unique = true THEN COUNT(DISTINCT rl.ip) ELSE COUNT(rl.ip) END as hits) " +
                  "FROM RequestLog rl " +
                  "WHERE rl.timeStamp BETWEEN :start AND :end " +
                  "AND (rl.uri IN(:uris) OR :uris IS NULL)" +
                  "GROUP BY rl.app, rl.uri " +
                  "ORDER BY hits DESC")
    List<StatsDto> getStats(@Param("start") LocalDateTime start,
                            @Param("end") LocalDateTime end,
                            @Param("uris") List<String> uris,
                            @Param("unique") boolean isUnique);
}
