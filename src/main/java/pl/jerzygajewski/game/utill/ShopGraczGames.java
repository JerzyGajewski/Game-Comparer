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
public class ShopGraczGames implements ScrapInterface {
    static ConfigurationModel[] MODEL = {
            new ConfigurationModel(
                    "ps4",
                    "https://shopgracz.pl/s-1/menu-playstation_4?order=product.name.asc&page=1",
                    "https://shopgracz.pl/s-1/menu-playstation_4?order=product.name.asc&page=",
                    ".js-product-miniature .product_desc .product_name",
                    ".js-product-miniature .product-price-and-shipping .price",
                    ".js-product-miniature .img_block img",
                    ".pagination .page-list li",
                    ".js-product-miniature .img_block",
                    ".js-product-miniature"),
            new ConfigurationModel(
                    "switch",
                    "https://shopgracz.pl/30-nintendo-switch",
                    "https://shopgracz.pl/30-nintendo-switch?page=",
                    ".js-product-miniature .product_desc .product_name",
                    ".js-product-miniature .product-price-and-shipping .price",
                    ".js-product-miniature .img_block img",
                    ".pagination .page-list li",
                    ".js-product-miniature .img_block",
                    ".js-product-miniature")
    };

    private ShopRepository shopRepository;
    private GameRepository gameRepository;


    public ShopGraczGames(ShopRepository shopRepository, GameRepository gameRepository) {
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
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.SHOPGRACZ.getName());
        shopInfo.setScrapDate(LocalDateTime.now());
        shopRepository.save(shopInfo);
    }

    @Override
    public void startScrapping(ConfigurationModel configurationModel, List<Game> allScrapedGames) throws IOException {
        Random random = new Random();
        int rand = random.nextInt(15)+1;
        Document document = connectToSite(configurationModel);
        String number = getPageNumbers(document, configurationModel);
        if (number.equals(configurationModel.getFirstPageUrl())) {
            List<Game> games = scrapGames(document, configurationModel);
            saveAndAddToList(games, allScrapedGames);
            try {
                TimeUnit.SECONDS.sleep(rand);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            int lastSiteNumber = Integer.parseInt(number);
            for (int i = 1; i <= lastSiteNumber; i++) {
                rand = random.nextInt(15)+1;
                Document document1 = connectToSiteBySiteNumber(configurationModel, i);
                List<Game> games = scrapGames(document1, configurationModel);
                saveAndAddToList(games, allScrapedGames);
                try {
                    TimeUnit.SECONDS.sleep(rand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public Document connectToSite(ConfigurationModel configurationModel) throws IOException {

                Document document = Jsoup.connect(configurationModel.getFirstPageUrl()).get();
                return document;
    }


    @Override
    public Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException {
                Document document = Jsoup.connect(configurationModel.getGameListUrl() + i).get();
                return document;
    }



    @Override
    public String getPageNumbers(Document document, ConfigurationModel configurationModel) throws IOException {
        Elements siteNumber = document.select(configurationModel.getLastPageSelector());
        if (siteNumber.size() > 0) {
            String number = siteNumber.get(siteNumber.size() - 2).text();
            return number;
        } else {
            return configurationModel.getFirstPageUrl();
        }
    }

    @Override
    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) {
        Element[] nameElement = getName(document, configurationModel);

        Element[] priceElement = getPrice(document, configurationModel);

        Element[] pictureElement = getImage(document, configurationModel);

        Element[] elements = getAllGames(document);

        Element avalable = document.select(configurationModel.getNotAvalable()).select("p").first();

        Element[] gameId = getGameId(document, configurationModel);

        List<Game> newList = new ArrayList<>();
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.SHOPGRACZ.getName());

        for (int i = 0; i < priceElement.length; i++) {

            Game gameTitle = new Game();
            gameTitle.setTitle(nameElement[i].text());
            gameTitle.setPrice(priceElement[i].text());
            gameTitle.setConsoleType(configurationModel.getConsole());
            if (elements[i].text().toLowerCase().trim().contains("chwilowo niedostępny")) {
                gameTitle.setAvalable(avalable.text());
            } else {
                gameTitle.setAvalable("Dostępny");
            }
            gameTitle.setGameShopId(gameId[i].attr("data-id-product"));
            gameTitle.setImg(pictureElement[i].attr("src"));
            gameTitle.setShop(shopInfo);

            newList.add(gameTitle);
        }

        return newList;
    }

    private Element[] getGameId(Document document, ConfigurationModel configurationModel) {
        Elements games = document.select(configurationModel.getGameId());
        Element[] gameId = new Element[games.size()];
        games.toArray(gameId);
        return gameId;
    }

    private Element[] getAllGames(Document document) {
        Elements all = document.select(".js-product-miniature .img_block");
        Element[] elements = new Element[all.size()];
        all.toArray(elements);
        return elements;
    }

    private Element[] getImage(Document document, ConfigurationModel configurationModel) {
        Elements pictures = document.select(configurationModel.getImageSelector());
        Element[] pictureElement = new Element[pictures.size()];
        pictures.toArray(pictureElement);
        return pictureElement;
    }

    private Element[] getName(Document document, ConfigurationModel configurationModel) {
        Elements name = document.select(configurationModel.getTitleSelector());
        Element[] nameElement = new Element[name.size()];
        name.toArray(nameElement);
        return nameElement;
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
