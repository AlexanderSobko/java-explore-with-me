package ru.practicum.ewm.main.service.rate.compilation_rate.service;

public interface CompilationRateService {

    void rateCompilation(boolean isLike, int fanId, int compId);

    void updateCompilationRate(boolean isLike, int fanId, int compId);

    void deleteCompilationRate(int fanId, int compId);

}
