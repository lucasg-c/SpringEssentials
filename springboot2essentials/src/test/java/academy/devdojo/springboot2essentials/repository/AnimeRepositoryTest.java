package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
class AnimeRepositoryTest
{
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save creates anime when sucessful")
    public void save_PersistAnime_WhenSucessful()
    {
        Anime animeToBeSaved = createAnime();
        Anime animeSaved = this.animeRepository.save(animeToBeSaved);
        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToBeSaved.getName());
    }

    private Anime createAnime()
    {
        return Anime.builder().name("Vivy -Fluorite Eye`s Song-").build();
    }
}