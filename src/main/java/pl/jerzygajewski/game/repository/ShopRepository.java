package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jerzygajewski.game.entity.ShopInfo;

import java.util.List;

public interface ShopRepository extends JpaRepository<ShopInfo, Long> {
    ShopInfo findByName(String shopName);

//    @Query("select s from ShopInfo s order by s.scrapDate asc ")
    List<ShopInfo> findFirstByOrderByScrapDateAsc();
}
