package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AnimeService implements AnimeRepository {
    // private final AnimeRepository animeRepository;
    public List<Anime> listAll()
    {
        return List.of(new Anime(1L,"86 —Eighty Six—"), new Anime(2L,"Vivy -Fluorite Eye's Song-"));
    }
}
