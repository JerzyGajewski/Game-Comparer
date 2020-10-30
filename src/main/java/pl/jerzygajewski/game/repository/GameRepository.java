package pl.jerzygajewski.game.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.jerzygajewski.game.entity.Game;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select g from Game g where g.shop.name like %?1%")
    List<Game> findAllGamesFromShop(String shopName);

    @Query("select g from Game g where g.gameShopId = :id and g.shop.name = :shop")
    Game findGameByGameShopId(String id, String shop);

    @Query("select g from Game g where g.title like %?1% and g.consoleType like %?2% and g.shop.name like %?3%")
    List<Game> searchGames(String gameName, String console, String shop);

    @Query("select g from Game g where g.title like %?1% and g.consoleType like %?2%")
    List<Game> findAll(String gameName, String Console);

}
