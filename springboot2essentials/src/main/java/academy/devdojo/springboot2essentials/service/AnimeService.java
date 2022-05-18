package academy.devdojo.springboot2essentials.service;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AnimeService {
    private List<Anime> animes = List.of(new Anime(1L,"86 —Eighty Six—"), new Anime(2L,"Vivy -Fluorite Eye's Song-"));
    // private final AnimeRepository animeRepository;
    public List<Anime> listAll()
    {
        return animes;
    }
    public Anime findById(long id)
    {
        return animes.stream()
                .filter(anime -> anime.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Anime not found in the database."));
    }
}
