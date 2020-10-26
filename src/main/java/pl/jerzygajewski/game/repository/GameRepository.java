package pl.jerzygajewski.game.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.jerzygajewski.game.entity.Game;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("select g from Game g where g.shop.name = :shopName")
    List<Game> findAllGamesFromShop(String shopName);

    @Query("select g from Game g where g.gameShopId = :id and g.shop.name = :shop")
    Game findGameByGameShopId(String id, String shop);

    @Query("select g from Game g where g.title like %?1% and g.consoleType like %?2%")
    List<Game> searchGames(String gameName, String console);
}
