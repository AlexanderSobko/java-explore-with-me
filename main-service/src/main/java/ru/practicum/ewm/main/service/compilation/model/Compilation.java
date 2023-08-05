package ru.practicum.ewm.main.service.compilation.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import ru.practicum.ewm.main.service.compilation.dto.CompilationCreateDto;
import ru.practicum.ewm.main.service.event.model.Event;
import ru.practicum.ewm.main.service.rate.dto.RateDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Table(name = "compilations")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Compilation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(nullable = false)
    String title;
    @Column(nullable = false)
    boolean pinned;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "compilations_events",
            joinColumns = {@JoinColumn(name = "compilation_id")},
            inverseJoinColumns = {@JoinColumn(name = "event_id")}
    )
    List<Event> events;
    @Transient
    RateDto rate;

    public long getRating() {
        return rate.getLikes() - rate.getDislikes();
    }

    public static Compilation mapToCompilation(CompilationCreateDto dto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(dto.isPinned());
        compilation.setTitle(dto.getTitle());
        return compilation;
    }

}
