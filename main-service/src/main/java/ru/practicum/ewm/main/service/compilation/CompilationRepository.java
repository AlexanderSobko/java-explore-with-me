package ru.practicum.ewm.main.service.compilation;

import ru.practicum.ewm.main.service.compilation.model.Compilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    List<Compilation> findAllByPinned(boolean pinned, Pageable pageable);

}
