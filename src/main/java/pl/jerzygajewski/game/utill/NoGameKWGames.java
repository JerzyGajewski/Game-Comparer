package pl.jerzygajewski.game.utill;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.enums.ProxyEnum;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.model.ConfigurationModel;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.ScrapInterface;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NoGameKWGames implements ScrapInterface {
    static ConfigurationModel[] MODEL = {
            new ConfigurationModel(
                    "ps4",
                    "https://www.nogame.pl/pl/c/PlayStation-4/140",
                    "https://www.nogame.pl/pl/c/PlayStation-4/140/",
                    ".products.viewphot .product .productname",
                    ".products.viewphot .product .price",
                    ".products.viewphot .product .prodimage .img-wrap img",
                    ".floatcenterwrap .paginator li",
                    "#box_productfull .availability .second, #box_productfull .tax-additional-info .second",
                    ".products.viewphot .product")
    };


    private ShopRepository shopRepository;
    private GameRepository gameRepository;

    @Autowired
    public NoGameKWGames(ShopRepository shopRepository, GameRepository gameRepository) {
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
    }

    int currentProxy = 0;

    @Override
    public void startScrapingForAllConsoles() throws IOException {
        List<Game> allScrapedGames = new ArrayList<>();
        for (int i = 0; i < MODEL.length; i++) {

            if (currentProxy >= ProxyEnum.values().length) {
                startScrapping(MODEL[i], allScrapedGames);
                currentProxy = 0;
            }
            startScrapping(MODEL[i], allScrapedGames);
            currentProxy++;
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            wait time!
            // getconfig for enum
            //startScrapping(config);
        }
    }

    @Override
    public void startScrapping(ConfigurationModel configurationModel, List<Game> allScrapedGames) throws IOException {
        Document document = connectToSite(configurationModel);
        String number = getPageNumbers(document, configurationModel);
        if (number.equals(configurationModel.getFirstPageUrl())) {
            List<Game> games = scrapGames(document, configurationModel);
            saveAndAddToList(games, allScrapedGames);
        } else {
            int lastSiteNumber = Integer.parseInt(number);
            for (int i = 1; i < lastSiteNumber; i++) {
                List<Game> games = scrapGames(document, configurationModel);
                saveAndAddToList(games, allScrapedGames);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        removeGame(allScrapedGames);


    }

    @Override
    public Document connectToSite(ConfigurationModel configurationModel) throws IOException {
        System.setProperty("http.proxyHost", ProxyEnum.values()[currentProxy].getIp());
        System.setProperty("http.proxyPort", ProxyEnum.values()[currentProxy].getPort());
        Connection conn = Jsoup.connect(configurationModel.getFirstPageUrl());
        Document document = conn.get();

        return document;
    }

    @Override
    public Document connectToSiteBySiteNumber(ConfigurationModel configurationModel, int i) throws IOException {
        return null;
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
    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) throws IOException {
        Elements name = document.select(configurationModel.getTitleSelector());
        Element[] nameElement = new Element[name.size()];
        name.toArray(nameElement);

        Elements price = document.select(configurationModel.getPriceSelector());
        Element[] priceElement = new Element[price.size()];
        price.toArray(priceElement);

        Elements pictures = document.select(configurationModel.getImageSelector());
        List<String> imgElement = new ArrayList<>();
        for (int i = 0; i < pictures.size(); i++) {
            if (i % 2 == 0) {
                imgElement.add(pictures.get(i).absUrl("data-src"));
            }
        }
        String[] pictureElement = new String[imgElement.size()];
        imgElement.toArray(pictureElement);


//        Elements all = document.select(".js-product-miniature .img_block");
//        Element[] elements = new Element[all.size()];
//        all.toArray(elements);
//
//        Element avalable = document.select(configurationModel.getNotAvalable()).select("p").first();

        Elements games = document.select(configurationModel.getGameId());
        Element[] gameId = new Element[games.size()];
        games.toArray(gameId);

        Elements getAbsUrl = document.getElementsByClass("prodimage f-row");

        List<Game> titles = new ArrayList<>();
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.NOGAMEKW.getName());

        for (int i = 0; i < name.size(); i++) {

            Game gameTitle = new Game();
            gameTitle.setTitle(nameElement[i].text());
            gameTitle.setPrice(priceElement[i].text());
            gameTitle.setConsoleType(configurationModel.getConsole());
            Document document1 = Jsoup.connect(getAbsUrl.get(i).absUrl("href")).get();
            Elements availableElement = document1.select("#box_productfull .availability .second, #box_productfull .tax-additional-info .second");
            if(availableElement.text().contains("Brak")){
            gameTitle.setAvalable(availableElement.text());
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

    @Override
    public List<Game> addOrUpdate(List<Game> newList) {
        List<Game> gameList = new ArrayList<>();
        for (int i = 0; i < newList.size(); i++) {
            if (gameRepository.findGameByGameShopId(newList.get(i).getGameShopId(), newList.get(i).getShop().getName()) != null) {
                Game gameToUpdate = gameRepository.findGameByGameShopId(newList.get(i).getGameShopId(), newList.get(i).getShop().getName());
                gameToUpdate.setTitle(newList.get(i).getTitle());
                gameToUpdate.setImg(newList.get(i).getImg());
                gameToUpdate.setPrice(newList.get(i).getPrice());
                gameToUpdate.setAvalable(newList.get(i).getAvalable());
                gameList.add(gameToUpdate);
            } else {
                gameList.add(newList.get(i));
            }
        }
        return gameList;
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
            // czy nie wywali błędu
            for (int j = 0; j < shopIdOldGame.size(); j++) {
                Game gameToRemove = gameRepository.findGameByGameShopId(shopIdOldGame.get(j), oldGameList.get(j).getShop().getName());
                oldGameList.remove(gameToRemove);
            }
        }
    }

    @Override
    public void saveAndAddToList(List<Game> newList, List<Game> allScrapedGames) {
        List<Game> gamesToSave = addOrUpdate(newList);
        for (Game games : gamesToSave) {
            gameRepository.save(games);
        }
        removeGame(newList);
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.NOGAMEKW.getName());
        shopInfo.setScrapDate(LocalDateTime.now());
        shopRepository.save(shopInfo);
    }
}
