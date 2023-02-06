package pl.jerzygajewski.game.utill;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NoGameKWGames implements ScrapInterface {
    static ConfigurationModel[] MODEL = {
            new ConfigurationModel(
                    "ps4",
                    "https://www.nogame.pl/pl/c/PlayStation-4/140/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    ".products.viewphot .product .buttons",
                    ".products.viewphot .product"),
            new ConfigurationModel(
                    "ps3",
                    "https://www.nogame.pl/pl/c/PlayStation-3/60/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    ".products.viewphot .product .buttons",
                    ".products.viewphot .product"),
            new ConfigurationModel(
                    "xbox360",
                    "https://www.nogame.pl/pl/c/Xbox-360/59/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    ".products.viewphot .product .buttons",
                    ".products.viewphot .product"),
            new ConfigurationModel(
                    "xboxOne",
                    "https://www.nogame.pl/pl/c/XBOX-ONE/162/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    ".products.viewphot .product .buttons",
                    ".products.viewphot .product"),
            new ConfigurationModel(
                    "switch",
                    "https://www.nogame.pl/pl/c/Nintendo-Switch/221/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    ".products.viewphot .product .buttons",
                    ".products.viewphot .product")
    };


    private ShopRepository shopRepository;
    private GameRepository gameRepository;

    @Autowired
    public NoGameKWGames(ShopRepository shopRepository, GameRepository gameRepository) {
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
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.NOGAMEKW.getName());
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
        }

    }

    @Override
    public Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException {
        return Jsoup.connect(configurationModel.getUrlPage() + i).get();
    }

    @Override
    public int getPageNumbers(ConfigurationModel configurationModel) throws IOException {
        Document document = connectToSiteBySiteNumber(configurationModel, 1);
        Elements siteNumber = document.select(configurationModel.getLastPageSelector());

        return Integer.parseInt(siteNumber.get(siteNumber.size() - 2).text());
    }

    @Override
    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) throws IOException {

        Element[] nameElement = getTitle(document, configurationModel);

        Element[] priceElement = getPrice(document, configurationModel);

        String[] pictureElement = getPicture(document, configurationModel);

        Element[] gameId = getId(document, configurationModel);

        Elements avalable = document.select(configurationModel.getNotAvalable());

        List<Game> titles = new ArrayList<>();
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.NOGAMEKW.getName());

        for (int i = 0; i < priceElement.length; i++) {

            Game gameTitle = new Game();
            gameTitle.setTitle(nameElement[i].text());
            gameTitle.setPrice(priceElement[i].text());
            gameTitle.setConsoleType(configurationModel.getConsole());

            if (avalable.get(i).select("form").isEmpty()) {
                gameTitle.setAvalable("Niedostępny");
            } else {
                gameTitle.setAvalable("Dostępny");
            }
            gameTitle.setGameShopId(gameId[i].attr("data-product-id"));
            gameTitle.setImg(pictureElement[i]);
            gameTitle.setShop(shopInfo);

            titles.add(gameTitle);
        }

        return titles;
    }

    private Element[] getTitle(Document document, ConfigurationModel configurationModel) {
        Elements name = document.select(configurationModel.getTitleSelector());
        Element[] nameElement = new Element[name.size()];
        name.toArray(nameElement);
        return nameElement;
    }

    private Element[] getId(Document document, ConfigurationModel configurationModel) {
        Elements games = document.select(configurationModel.getGameId());
        Element[] gameId = new Element[games.size()];
        games.toArray(gameId);
        return gameId;
    }

    private String[] getPicture(Document document, ConfigurationModel configurationModel) {
        Elements pictures = document.select(configurationModel.getImageSelector());
        List<String> imgElement = new ArrayList<>();
        for (int i = 0; i < pictures.size(); i++) {
            if (i % 2 == 0) {
                imgElement.add(pictures.get(i).absUrl("data-src"));
            }
        }
        String[] pictureElement = new String[imgElement.size()];
        imgElement.toArray(pictureElement);
        return pictureElement;
    }

    private Element[] getPrice(Document document, ConfigurationModel configurationModel) {
        Elements price = document.select(configurationModel.getPriceSelector());
        Element[] priceElement = new Element[price.size()];
        price.toArray(priceElement);
        return priceElement;
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
    public void removeGame(List<Game> allScrapedGames) {
        List<String> shopIdNewList = new ArrayList<>();
        for (Game game : allScrapedGames) {
            shopIdNewList.add(game.getGameShopId());
        }
        List<Game> oldGameList = gameRepository.findAllGamesFromShop(allScrapedGames.get(0).getShop().getName());
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
