package academy.devdojo.springboot2essentials.client;

import academy.devdojo.springboot2essentials.domain.Anime;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Log4j2
public class SpringClient
{
    public static void main(String[] args)
    {
        //retorna objeto em um "wrapper"
        ResponseEntity<Anime> entity = new RestTemplate().getForEntity("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(entity);

        //retorna o objeto diretamente
        Anime object = new RestTemplate().getForObject("http://localhost:8080/animes/{id}", Anime.class, 2);
        log.info(object);

        Anime[] animes = new RestTemplate().getForObject("http://localhost:8080/animes/all", Anime[].class);
        log.info(Arrays.toString(animes));

        //@formatter:off
        ResponseEntity<List<Anime>> exchange = new RestTemplate().exchange
        ("http://localhost:8080/animes/all",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Anime>>() { }
        );
        //@formatter:on
        log.info(exchange.getBody());

//        Anime aquatope = Anime.builder().name("Shiroi Suna no Aquatope").build();
//        Anime aquatopeSaved = new RestTemplate().postForObject("http://localhost:8080/animes/", aquatope, Anime.class);
//        log.info("saved anime {}", aquatopeSaved);

        //@formatter:off
        Anime toman = Anime.builder().name("Tokyo Manji Revengers").build();
        ResponseEntity<Anime> tomanSaved = new RestTemplate().exchange
        (
            "http://localhost:8080/animes/",
            HttpMethod.POST,
            new HttpEntity<>(toman, createJsonHeader()),
            Anime.class
        );
        log.info("saved anime {}", tomanSaved);
        //@formatter:on
    }

    private static HttpHeaders createJsonHeader()
    {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}