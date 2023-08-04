package ru.practicum.ewm.main.service.rate.compilation_rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.main.service.compilation.model.Compilation;
import ru.practicum.ewm.main.service.rate.dto.RateDto;

import java.util.List;

public interface CompilationRateRepository extends JpaRepository<CompilationRate, CompilationRateIdClass> {

    @Query("SELECT new ru.practicum.ewm.main.service.rate.dto.RateDto(" +
            "   cr.idClass.compilation.id," +
            "   COUNT(CASE WHEN cr.isLike = true THEN 1 END), " +
            "   COUNT(CASE WHEN cr.isLike = false THEN 1 END)) " +
            "FROM CompilationRate cr " +
            "WHERE cr.idClass.compilation IN(:compilations) " +
            "GROUP BY cr.idClass.compilation ")
    List<RateDto> getRatesByCompilationsIn(@Param("compilations") List<Compilation> compilations);

}
