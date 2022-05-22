package academy.devdojo.springboot2essentials.repository;

import academy.devdojo.springboot2essentials.domain.Anime;
import academy.devdojo.springboot2essentials.domain.DevDojoUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long>
{
    DevDojoUser findByUsername(String username);
}
