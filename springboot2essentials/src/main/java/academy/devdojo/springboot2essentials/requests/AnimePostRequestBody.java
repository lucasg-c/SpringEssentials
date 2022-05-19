package academy.devdojo.springboot2essentials.requests;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;

@Data
public class AnimePostRequestBody
{
    @NotEmpty(message = "Anime's name cannot be empty.")
    private String name;
}
