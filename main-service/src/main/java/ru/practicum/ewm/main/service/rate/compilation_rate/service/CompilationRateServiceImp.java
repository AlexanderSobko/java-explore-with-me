package ru.practicum.ewm.main.service.rate.compilation_rate.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.service.compilation.service.CompilationService;
import ru.practicum.ewm.main.service.exception.NotFoundException;
import ru.practicum.ewm.main.service.rate.compilation_rate.CompilationRateRepository;
import ru.practicum.ewm.main.service.rate.compilation_rate.model.CompilationRate;
import ru.practicum.ewm.main.service.rate.compilation_rate.model.CompilationRateIdClass;
import ru.practicum.ewm.main.service.user.service.UserService;

import javax.transaction.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompilationRateServiceImp implements CompilationRateService {

    CompilationRateRepository compilationRateRepo;
    CompilationService compilationService;
    UserService userService;

    @Override
    public void rateCompilation(boolean isLike, int fanId, int compId) {
        CompilationRateIdClass idClass = getCompilationRateIdClass(fanId, compId);
        CompilationRate compilationRate = compilationRateRepo.save(CompilationRate.builder()
                .idClass(idClass)
                .isLike(isLike)
                .build());
        log.info("Оценка к подборке событий с id({}) сохранена! {}", compId, compilationRate);
    }

    @Override
    public void updateCompilationRate(boolean isLike, int fanId, int compId) {
        CompilationRate compilationRate = getCompilationRate(fanId, compId);
        compilationRate.setLike(isLike);
        compilationRate = compilationRateRepo.save(compilationRate);
        log.info("Оценка к подборке событий с id({}) изменена! {}", compId, compilationRate);
    }

    @Override
    public void deleteCompilationRate(int fanId, int compId) {
        CompilationRate compilationRate = getCompilationRate(fanId, compId);
        compilationRateRepo.delete(compilationRate);
        log.info("Оценка к подборке событий с id({}) удалена! {}", compId, compilationRate);
    }

    private CompilationRate getCompilationRate(int fanId, int compId) {
        CompilationRateIdClass idClass = getCompilationRateIdClass(fanId, compId);
        return compilationRateRepo.findById(idClass).orElseThrow(() ->
                new NotFoundException(String.format(
                        "Пользователь с id(%d) не оценивал подборку событий с id(%d)!", fanId, compId)));
    }

    private CompilationRateIdClass getCompilationRateIdClass(int fanId, int compId) {
        return CompilationRateIdClass.builder()
                .fan(userService.getById(fanId))
                .compilation(compilationService.getCompilationById(compId))
                .build();
    }

}
