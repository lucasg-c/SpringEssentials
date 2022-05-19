package academy.devdojo.springboot2essentials.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class AnimePostRequestBody
{
    @NotEmpty(message = "Anime's name cannot be empty.")
    private String name;
}
