package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@DisplayName("Tests for Anime Repository")
@Log4j2
class AnimeRepositoryTest
{
    @Autowired
    private AnimeRepository animeRepository;

    @Test
    @DisplayName("Save persists anime when sucessful")
    public void save_PersistAnime_WhenSucessful()
    {
        Anime createAnimeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(createAnimeToBeSaved);

        Assertions.assertThat(animeSaved).isNotNull();
        Assertions.assertThat(animeSaved.getId()).isNotNull();
        Assertions.assertThat(animeSaved.getName()).isEqualTo(createAnimeToBeSaved.getName());
    }

    @Test
    @DisplayName("Save updates anime when sucessful")
    public void save_UpdateAnime_WhenSucessful()
    {
        Anime createAnimeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(createAnimeToBeSaved);
        Anime animeUpdated = this.animeRepository.save(animeSaved);
        animeUpdated.setName("86 —Eighty Six—");

        //log.info(animeUpdated.getName());
        Assertions.assertThat(animeUpdated).isNotNull();
        Assertions.assertThat(animeUpdated.getId()).isNotNull();
        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeSaved.getName());
    }

    @Test
    @DisplayName("Delete removes anime when sucessful")
    public void delete_RemovesAnime_WhenSucessful()
    {
        Anime createAnimeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(createAnimeToBeSaved);
        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeOptional).isEmpty();
    }

    @Test
    @DisplayName("Find By Name returns an anime list when sucessful")
    public void findByName_ReturnsAnAnimeList_WhenSucessful()
    {
        Anime createAnimeToBeSaved = AnimeCreator.createAnimeToBeSaved();
        Anime animeSaved = this.animeRepository.save(createAnimeToBeSaved);
        String name = animeSaved.getName();
        List<Anime> animes = this.animeRepository.findByName(name);

        Assertions.assertThat(animes)
                .isNotEmpty()
                .contains(animeSaved);
    }

    @Test
    @DisplayName("Find By Name returns an empty anime list when the specified anime is not found")
    public void findByName_ReturnsAnEmptyAnimeList_WhenSpecifiedAnimeIsNotFound()
    {
        List<Anime> animes = this.animeRepository.findByName("Not a Listed Anime");

        Assertions.assertThat(animes).isEmpty();
    }

    @Test
    @DisplayName("Save throws ConstraintInvalidationException when name is empty")
    public void save_ThrowsConstraintInvalidationException_WhenNameIsEmpty()
    {
        Anime anime = new Anime();
//        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
//                .isInstanceOf(ConstraintViolationException.class);

        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
                .isThrownBy(() -> this.animeRepository.save(anime))
                .withMessageContaining("Anime's name cannot be empty.");
    }
}