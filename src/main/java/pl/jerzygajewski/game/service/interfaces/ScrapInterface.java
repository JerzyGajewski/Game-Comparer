package pl.jerzygajewski.game.service.interfaces;

import org.jsoup.nodes.Document;
import pl.jerzygajewski.game.consoleTypeEnum.ConsoleEnum;
import pl.jerzygajewski.game.entity.Game;

import java.io.IOException;
import java.util.List;

public interface ScrapInterface {

    Document connectToSite(String url) throws IOException;

    List<Game> scrapGames(Document document, ConsoleEnum consoleEnum, String[] elementValue);

    void saveGames(List<Game> games);

    void startScrapping(String url, ConsoleEnum consoleEnum, String[] elementValue) throws IOException;

    void startScrapingForAllConsoles() throws IOException;

}
