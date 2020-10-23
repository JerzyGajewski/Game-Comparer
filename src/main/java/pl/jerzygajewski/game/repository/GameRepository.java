package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jerzygajewski.game.entity.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

}
