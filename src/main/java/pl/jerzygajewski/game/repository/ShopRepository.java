package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jerzygajewski.game.entity.ShopInfo;

public interface ShopRepository extends JpaRepository<ShopInfo, Long> {
}
