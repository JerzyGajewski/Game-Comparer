package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jerzygajewski.game.entity.GameTitle;

public interface GameTitleRepository extends JpaRepository<GameTitle, Long> {
}
