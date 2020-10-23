package pl.jerzygajewski.game.service.interfaces;

import org.jsoup.nodes.Document;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.model.ConfigurationModel;

import java.io.IOException;
import java.util.List;

public interface ScrapInterface {

    Document connectToSite(ConfigurationModel configurationModel) throws IOException;

    List<Game> scrapGames(Document document, ConfigurationModel configurationModel);

    void saveGames(List<Game> games);

    String getPageNumbers(Document document, ConfigurationModel configurationModel) throws IOException;

    void startScrapping(ConfigurationModel configurationModel) throws IOException;

    void startScrapingForAllConsoles() throws IOException;

}
