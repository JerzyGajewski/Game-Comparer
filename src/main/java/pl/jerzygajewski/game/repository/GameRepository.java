package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jerzygajewski.game.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
