package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("anime")
public class AnimeController {
    @Autowired
    private DateUtil dateUtil;
    @GetMapping(path = "list")
    public List<Anime> list()
    {
        return List.of(new Anime("86 —Eighty Six—"), new Anime("Vivy -Fluorite Eye's Song-"));
    }
}
