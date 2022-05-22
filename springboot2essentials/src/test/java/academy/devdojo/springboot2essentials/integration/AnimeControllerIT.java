package academy.devdojo.springboot2essentials.integration;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.domain.DevDojoUser;
import academy.devdojo.springboot2essentials.repository.AnimeRepository;
import academy.devdojo.springboot2essentials.repository.DevDojoUserRepository;
import academy.devdojo.springboot2essentials.requests.AnimePostRequestBody;
import academy.devdojo.springboot2essentials.util.AnimeCreator;
import academy.devdojo.springboot2essentials.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2essentials.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AnimeControllerIT
{
    @Autowired
    @Qualifier(value = "testRestTemplateUser")
    private TestRestTemplate testRestTemplateRoleUser;
    @Autowired
    @Qualifier(value = "testRestTemplateAdmin")
    private TestRestTemplate testRestTemplateRoleAdmin;
    @Autowired
    private AnimeRepository animeRepository;
    @Autowired
    private DevDojoUserRepository devDojoUserRepository;
    private static final DevDojoUser USER = DevDojoUser.builder()
            .name("DevDojo Academy")
            .password("{bcrypt}$2a$10$kDcih71oJMRMHpdRwm5QJ.tj7HDMDNRrP8Uq0oSrH6AdMY7ttfNCy")
            .username("devdojo")
            .authorities("ROLE_USER")
            .build();
    private static final DevDojoUser ADMIN = DevDojoUser.builder()
            .name("Lucas")
            .password("{bcrypt}$2a$10$kDcih71oJMRMHpdRwm5QJ.tj7HDMDNRrP8Uq0oSrH6AdMY7ttfNCy")
            .username("lucas")
            .authorities("ROLE_USER,ROLE_ADMIN")
            .build();

    @TestConfiguration
    @Lazy
    static class Config
    {
        @Bean(name = "testRestTemplateUser")
        public TestRestTemplate testRestTemplateRoleUserCreator(@Value("${local.server.port}") int port)
        {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("devdojo", "academy");

            return new TestRestTemplate(restTemplateBuilder);
        }
        @Bean(name = "testRestTemplateAdmin")
        public TestRestTemplate testRestTemplateRoleAdminCreator(@Value("${local.server.port}") int port)
        {
            RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder()
                    .rootUri("http://localhost:"+port)
                    .basicAuthentication("lucas", "academy");

            return new TestRestTemplate(restTemplateBuilder);
        }
    }

    @Test
    @DisplayName("List returns anime list inside page object when successful")
    void list_ReturnsAnimeListInsidePageObject_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        PageableResponse<Anime> animePage = testRestTemplateRoleUser.exchange
                (
                        "/animes",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<PageableResponse<Anime>>() { }
                ).getBody();

        Assertions.assertThat(animePage).isNotNull();
        Assertions.assertThat(animePage.toList())
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animePage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll returns anime list when successful")
    void listAll_ReturnsAnimeList_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        List<Anime> animes = testRestTemplateRoleUser.exchange
                (
                        "/animes/all",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Anime>>() { }
                ).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById returns anime when successful")
    void findById_ReturnsAnime_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        Long expectedId = savedAnime.getId();
        Anime anime = testRestTemplateRoleUser.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isNotNull().isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName returns anime list when successful")
    void findByName_ReturnsAnimeList_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplateRoleUser.exchange
                (
                        url,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Anime>>() { }
                ).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName returns an empty anime list when anime is not found")
    void findByName_ReturnsAnEmptyAnimeList_WhenAnimeIsNotFound()
    {
        devDojoUserRepository.save(USER);
        List<Anime> animes = testRestTemplateRoleUser.exchange
                (
                        "/animes/find?name=NotAnExistingAnime",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Anime>>() { }
                ).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("Save returns anime when successful")
    void save_ReturnsAnime_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();
        ResponseEntity<Anime> animeResponseEntity = testRestTemplateRoleUser.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("Replace updates anime when successful")
    void replace_UpdatesAnime_WhenSuccessful()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        savedAnime.setName("new name");
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes", HttpMethod.PUT, new HttpEntity<>(savedAnime), Void.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("Delete removes anime when successful")
    void delete_RemovesAnime_WhenSuccessful()
    {
        devDojoUserRepository.save(ADMIN);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleAdmin.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    @Test
    @DisplayName("Delete returns 483 when user is not admin")
    void delete_Returns483_WhenUserIsNotAdmin()
    {
        devDojoUserRepository.save(USER);
        Anime savedAnime = animeRepository.save(AnimeCreator.createAnimeToBeSaved());
        ResponseEntity<Void> animeResponseEntity = testRestTemplateRoleUser.exchange("/animes/admin/{id}", HttpMethod.DELETE, null, Void.class, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
