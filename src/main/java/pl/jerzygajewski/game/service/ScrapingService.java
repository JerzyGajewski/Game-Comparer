package pl.jerzygajewski.game.service;

import org.jsoup.nodes.Document;
import pl.jerzygajewski.game.consoleTypeEnum.ConsoleEnum;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.service.interfaces.ScrapInterface;

import java.io.IOException;
import java.util.List;

public class ScrapingService implements ScrapInterface{

    @Override
    public Document connectToSite(String url) throws IOException {
        return null;
    }

    @Override
    public List<Game> scrapGames(Document document, ConsoleEnum consoleEnum, String[] elementValue) {
        return null;
    }

    @Override
    public void saveGames(List<Game> games) {

    }

    @Override
    public void startScrapping(String url, ConsoleEnum consoleEnum, String[] elementValue) throws IOException {

    }

    @Override
    public void startScrapingForAllConsoles() throws IOException {

    }
}
