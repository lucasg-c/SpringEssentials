package academy.devdojo.springboot2essentials.util;

import academy.devdojo.springboot2essentials.domain.Anime;

public class AnimeCreator
{
    public static Anime createAnimeToBeSaved()
    {
        return Anime.builder()
                .name("Vivy -Fluorite Eye`s Song-")
                .build();
    }

    public static Anime createValidAnime()
    {
        return Anime.builder()
                .name("Vivy -Fluorite Eye`s Song-")
                .id(1L)
                .build();
    }

    public static Anime createValidUpdatedAnime()
    {
        return Anime.builder()
                .name("Vivy -Fluorite Eye`s Song-")
                .id(1L)
                .build();
    }
}
