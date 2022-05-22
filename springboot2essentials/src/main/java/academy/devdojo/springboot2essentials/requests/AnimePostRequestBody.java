package academy.devdojo.springboot2essentials.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody
{
    @NotEmpty(message = "Anime's name cannot be empty.")
    private String name;
}
