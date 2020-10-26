package pl.jerzygajewski.game.service.interfaces;

import org.jsoup.nodes.Document;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.model.ConfigurationModel;

import java.io.IOException;
import java.util.List;

public interface ScrapInterface {

    void startScrapingForAllConsoles() throws IOException;

    void startScrapping(ConfigurationModel configurationModel) throws IOException;

    Document connectToSite(ConfigurationModel configurationModel) throws IOException;

    Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException;

    String getPageNumbers(Document document, ConfigurationModel configurationModel) throws IOException;

    List<Game> scrapGames(Document document, ConfigurationModel configurationModel) throws IOException;

    List<Game> addOrUpdate(List<Game> game);

    void removeGame(List<Game> games);

    void saveOrRemoveGames(List<Game> games);


}
