package pl.jerzygajewski.game.utill;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import pl.jerzygajewski.game.consoleTypeEnum.ShopEnum;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
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
public class NoGameKWGames {
//    static ConfigurationModel[] MODEL = {};
//
    private ShopRepository shopRepository;
    private GameRepository gameRepository;

    @Autowired
    public NoGameKWGames(ShopRepository shopRepository, GameRepository gameRepository) {
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
    }
}
//
//    @Override
//    public Document connectToSite(ConfigurationModel configurationModel) throws IOException {
//        //set proxy
//        Connection connect = Jsoup.connect(configurationModel.getFirstPageUrl());
//        return connect.get();
//    }
//
//
//    @Override
//    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) {
//        Elements name = document.select(configurationModel.getTitleSelector());
//        Element[] nameElement = new Element[name.size()];
//        name.toArray(nameElement);
//
//        Elements price = document.select(configurationModel.getPriceSelector());
//        Element[] priceElement = new Element[price.size()];
//        price.toArray(priceElement);
//
//        Elements pictures = document.select(configurationModel.getImageSelector());
//        Element[] pictureElement = new Element[pictures.size()];
//        pictures.toArray(pictureElement);
//
//        List<Game> titles = new ArrayList<>();
//        for (int i = 0; i < name.size(); i++) {
//            Game gameTitle = new Game();
//            gameTitle.setTitle(nameElement[i].text());
//            gameTitle.setPrice(priceElement[i].text());
//            gameTitle.setConsoleType(configurationModel.getConsole());
////            gameTitle.setImg(pictureElement[i].attr("src"));
//            titles.add(gameTitle);
//        }
//        return titles;
//    }
//
//    @Override
//    public void saveGames(List<Game> games) {
//        //repo get shop
//        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.GAMEOVER.getName());
//        //loop over games && connect with shop
//        for (Game name : games) {
//            name.setShop(shopInfo);
//        }
//        //in loop save to db - add or update
//        for (Game name : games) {
//            gameRepository.save(name);
//        }
//        shopInfo.setScrapDate(LocalDateTime.now());
//        shopRepository.save(shopInfo);
//    }
//
//    @Override
//    public String getPageNumbers(ConfigurationModel configurationModel) throws IOException {
//        Document document = connectToSite(configurationModel);
//        Elements siteNumber = document.select(configurationModel.getLastPageSelector());
//        if (siteNumber.size() > 0) {
//            String number = "";
////            siteNumber.get(siteNumber.size() - 2).text();
//            return number;
//        } else {
//            return configurationModel.getFirstPageUrl();
//        }
//    }
//
//    @Override
//    public void startScrapping(ConfigurationModel configurationModel) throws IOException {
//        Document document = connectToSite(configurationModel);
//        String number = getPageNumbers(configurationModel);
//        if (number.equals(configurationModel.getFirstPageUrl())) {
//            List<Game> games = scrapGames(document, configurationModel);
//            saveGames(games);
//        } else {
//            int lastSiteNumber = Integer.parseInt(number);
//            for (int i = 1; i < lastSiteNumber; i++) {
//                List<Game> games = scrapGames(document, configurationModel);
//                saveGames(games);
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
////        wait time!
//            }
//        }
//    }
//
//    @Override
//    public void startScrapingForAllConsoles() throws IOException {
//        for (int i = 0; i < MODEL.length; i++) {
//            startScrapping(MODEL[i]);
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
////    public List<Ps4Games> getGameData() throws IOException {
////        List<Ps4Games> titles = new ArrayList<>();
////
////        Connection connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140");
////        Document document = connect.get();
////        Elements name = document.select(".products.viewphot .product .productname");
////        Elements price = document.select(".products.viewphot .product .price");
////        Element[] nameElement = new Element[name.size()];
////        name.toArray(nameElement);
////        Element[] priceElement = new Element[price.size()];
////        price.toArray(priceElement);
//////        Elements picture = document.select(".products.viewphot .product .prodimage .img-wrap").select("src");
////
////
////        for (int i = 0; i < name.size(); i++) {
////            Ps4Games gameTitle = new Ps4Games();
////            gameTitle.setTitle(nameElement[i].text());
////            gameTitle.setPrice(priceElement[i].text());
//////            gameTitle.setImg(picture.get(i).text());
////            titles.add(gameTitle);
////        }
////
////        return titles;
////    }
//
//
////                    String ps4 = "https://www.nogame.pl/pl/c/PlayStation-4/140";
////                    String[] elementValues = {".products.viewphot .product .productname", ".products.viewphot .product .price"};
////                    startScrapping(ps4, ConsoleEnum.values()[i], elementValues);
////        Elements pictures = document.getElementsByClass("prodimage f-row");
////        Element[] pictureElement = new Element[pictures.size()];
////        pictures.toArray(pictureElement);
//
////        for (int i = 2; i <= 31; i++) {
////            connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140/"+i);
////            document = connect.get();
////            name = document.select(".products.viewphot .product .productname");
////            price = document.select(".products.viewphot .product .price");
////            picture = document.select(".products.viewphot .product .prodimage .img-wrap img").tagName("img src");
////
////            for (Element names : name) {
////                titles.add(names.text());
////            }
////        }
//        }
//    }
//}