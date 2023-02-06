package pl.jerzygajewski.game.utill;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.model.ConfigurationModel;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.ScrapInterface;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class GameOverGames implements ScrapInterface {
    static ConfigurationModel[] MODEL = {new ConfigurationModel(
            "ps4",
            "https://www.krakow.gameover.pl/sklep/index.php?k68,playstation-4-gry,",
            "product__name",
            "product__price",
            "product__thumb",
            "pagination-box",
            "product__aval",
            "input[name]"
    ),
            new ConfigurationModel(
                    "ps3",
                    "https://www.krakow.gameover.pl/sklep/index.php?k3,playstation-3-gry,",
                    "product__name",
                    "product__price",
                    "product__thumb",
                    "pagination-box",
                    "product__aval",
                    "input[name]"
            ),
            new ConfigurationModel(
                    "xbox360",
                    "https://www.krakow.gameover.pl/sklep/index.php?k5,xbox-360-gry,",
                    "product__name",
                    "product__price",
                    "product__thumb",
                    "pagination-box",
                    "product__aval",
                    "input[name]"
            ),
            new ConfigurationModel(
                    "xboxOne",
                    "https://www.krakow.gameover.pl/sklep/index.php?k102,xbox-one-gry,",
                    "product__name",
                    "product__price",
                    "product__thumb",
                    "pagination-box",
                    "product__aval",
                    "input[name]"
            ),
            new ConfigurationModel(
                    "switch",
                    "https://www.krakow.gameover.pl/sklep/index.php?k84,nintendo-switch-gry,",
                    "product__name",
                    "product__price",
                    "product__thumb",
                    "pagination-box",
                    "product__aval",
                    "input[name]"
            )};

    private final ShopRepository shopRepository;
    private final GameRepository gameRepository;


    public GameOverGames(ShopRepository shopRepository, GameRepository gameRepository) {
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void startScrapingForAllConsoles() throws IOException {
        List<Game> allScrapedGames = new ArrayList<>();
        for (int i = 0; i < MODEL.length; i++) {
            startScrapping(MODEL[i], allScrapedGames);
        }
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        removeGame(allScrapedGames);
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.GAMEOVER.getName());
        shopInfo.setScrapDate(LocalDateTime.now());
        shopRepository.save(shopInfo);
    }

    @Override
    public void startScrapping(ConfigurationModel configurationModel, List<Game> allScrapedGames) throws IOException {
        Random random = new Random();
        int rand = random.nextInt(15) + 1;
        int lastSiteNumber = getPageNumbers(configurationModel);
        for (int i = 1; i <= lastSiteNumber; i++) {
            rand = random.nextInt(15) + 1;
            Document document = connectToSiteBySiteNumber(configurationModel, i);
            List<Game> games = scrapGames(document, configurationModel);
            saveAndAddToList(games, allScrapedGames);
            try {
                TimeUnit.SECONDS.sleep(rand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        wait time!
        }

    }

    @Override
    public Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException {
        return Jsoup.connect(configurationModel.getUrlPage() + i).get();
    }


    public int getPageNumbers(ConfigurationModel configurationModel) throws IOException {
        Document document = connectToSiteBySiteNumber(configurationModel, 1);
        Elements siteNumber = document.getElementsByClass(configurationModel.getLastPageSelector()).select("a");
        return Integer.parseInt(siteNumber.last().text());
    }


    @Override
    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) throws IOException {

        Elements name = document.select("input[name]");

        Element names = name.get(4);

        String val = names.attr("value");
        String[] id = val.split(",");

        Element[] gameTitle = getTitle(document, configurationModel, id);

        Element[] gameImg = getPicture(document, configurationModel, id);

        Element[] gamePrice = getPrice(document, configurationModel, id);

        List<String> singleLinkList = getGamesLinks(document, configurationModel);

        Element[] availableGames = getAvailable(document, configurationModel, id);

        List<Game> titles = new ArrayList<>();
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.GAMEOVER.getName());

        for (int i = 0; i < singleLinkList.size(); i++) {
            Game gameInfo = new Game();
            gameInfo.setTitle(gameTitle[i].text());
            gameInfo.setGameShopId(id[i]);
            gameInfo.setImg(gameImg[i].absUrl("src"));
            gameInfo.setPrice(gamePrice[i].text());
            gameInfo.setAvalable(availableGames[i].text());
            gameInfo.setConsoleType(configurationModel.getConsole());
            gameInfo.setShop(shopInfo);

            titles.add(gameInfo);
        }

        return titles;
    }


    private Element[] getAvailable(Document document, ConfigurationModel configurationModel, String[] id) {
        Elements ava = document.getElementsByClass(configurationModel.getNotAvalable());

        Element[] aval = new Element[id.length];
        ava.toArray(aval);
        return aval;
    }


    private List<String> getGamesLinks(Document document, ConfigurationModel configurationModel) {
        Elements linkList = document.getElementsByClass(configurationModel.getTitleSelector()).select("a");
        List<String> singleLinkList = new ArrayList<>();
        for (int i = 0; i < linkList.size(); i++) {
            if (i % 2 == 0) {
                String singleLink = linkList.get(i).absUrl("href");
                singleLinkList.add(singleLink);
            }
        }
        String[] linkArray = new String[singleLinkList.size()];
        singleLinkList.toArray(linkArray);
        return singleLinkList;
    }


    private Element[] getPrice(Document document, ConfigurationModel configurationModel, String[] id) {
        Elements price = document.getElementsByClass(configurationModel.getPriceSelector()).select("span");
        price.remove(0);
        price.remove(price.size() - 1);

        Element[] gamePrice = new Element[id.length];
        price.toArray(gamePrice);
        return gamePrice;
    }

    private Element[] getPicture(Document document, ConfigurationModel configurationModel, String[] id) {
        Elements img = document.getElementsByClass(configurationModel.getImageSelector()).select("a").select("img");
        Element[] gameImg = new Element[id.length];
        img.toArray(gameImg);
        return gameImg;
    }

    private Element[] getTitle(Document document, ConfigurationModel configurationModel, String[] id) {
        Elements names = document.getElementsByClass(configurationModel.getTitleSelector()).select("a");
        Element[] gameTitle = new Element[id.length];
        names.toArray(gameTitle);
        return gameTitle;
    }

    @Override
    public List<Game> addOrUpdate(List<Game> newList) {
        List<Game> gameList = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) {
            if (gameRepository.findGameByGameShopId(newList.get(i).getGameShopId(), newList.get(i).getShop().getName()) != null) {
                updateGameData(newList, gameList, i);
            } else {
                gameList.add(newList.get(i));
            }
        }
        return gameList;
    }

    private void updateGameData(List<Game> newList, List<Game> gameList, int i) {
        Game gameToUpdate = gameRepository.findGameByGameShopId(newList.get(i).getGameShopId(), newList.get(i).getShop().getName());
        gameToUpdate.setTitle(newList.get(i).getTitle());
        gameToUpdate.setImg(newList.get(i).getImg());
        gameToUpdate.setPrice(newList.get(i).getPrice());
        gameToUpdate.setAvalable(newList.get(i).getAvalable());
        gameList.add(gameToUpdate);
    }

    @Override
    public void saveAndAddToList(List<Game> newList, List<Game> allScrapedGames) {
        List<Game> gamesToSave = addOrUpdate(newList);
        for (Game games : gamesToSave) {
            gameRepository.save(games);
            allScrapedGames.add(games);
        }
    }

    @Override
    public void removeGame(List<Game> allScrappedGames) {
        List<String> shopIdNewList = new ArrayList<>();
        for (Game game : allScrappedGames) {
            shopIdNewList.add(game.getGameShopId());
        }
        List<Game> oldGameList = gameRepository.findAllGamesFromShop(allScrappedGames.get(0).getShop().getName());
        List<String> shopIdOldGame = new ArrayList<>();
        for (Game game : oldGameList) {
            shopIdOldGame.add(game.getGameShopId());
        }
        shopIdOldGame.removeAll(shopIdNewList);
        if (shopIdOldGame.size() != 0) {
            for (int j = 0; j < shopIdOldGame.size(); j++) {
                Game gameToRemove = gameRepository.findGameByGameShopId(shopIdOldGame.get(j), oldGameList.get(j).getShop().getName());
                oldGameList.remove(gameToRemove);
            }
        }
    }

}
