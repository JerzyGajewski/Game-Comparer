package pl.jerzygajewski.game.service.interfaces;

import org.jsoup.nodes.Document;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.model.ConfigurationModel;

import java.io.IOException;
import java.util.List;

public interface ScrapInterface {

    void startScrapingForAllConsoles() throws IOException;

    void startScrapping(ConfigurationModel configurationModel, List<Game> allScrapedGames) throws IOException;

    Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException;

    int getPageNumbers(ConfigurationModel configurationModel) throws IOException;

    List<Game> scrapGames(Document document, ConfigurationModel configurationModel) throws IOException;

    List<Game> addOrUpdate(List<Game> game);

    void saveAndAddToList(List<Game> games, List<Game> allScrapedGames);

    void removeGame(List<Game> allScrapedGames);


}
