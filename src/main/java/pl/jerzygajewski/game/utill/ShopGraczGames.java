package pl.jerzygajewski.game.utill;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.enums.ProxyEnum;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.model.ConfigurationModel;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.ScrapInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShopGraczGames implements ScrapInterface {
    static ConfigurationModel[] MODEL = {
//            new ConfigurationModel("ps4", "https://shopgracz.pl/s-1/menu-playstation_4",
//                    "https://shopgracz.pl/s-1/menu-playstation_4?order=product.name.asc&page=",
//                    ".js-product-miniature .product_desc .product_name",
//                    ".js-product-miniature .product-price-and-shipping .price",
//                    ".js-product-miniature .img_block img",
//                    ".pagination .page-list li"),
            new ConfigurationModel("switch", "https://shopgracz.pl/30-nintendo-switch",
                    "", ".js-product-miniature .product_desc .product_name",
                    ".js-product-miniature .product-price-and-shipping .price",
                    ".js-product-miniature .img_block img",
                    ".pagination .page-list li", ".js-product-miniature .img_block",
                    ".js-product-miniature")
    };

    int currentProxy = 0;
    private ShopRepository shopRepository;
    private GameRepository gameRepository;


    public ShopGraczGames(ShopRepository shopRepository, GameRepository gameRepository) {
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
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
    public List<Game> scrapGames(Document document, ConfigurationModel configurationModel) {
        Elements name = document.select(configurationModel.getTitleSelector());
        Element[] nameElement = new Element[name.size()];
        name.toArray(nameElement);

        Elements price = document.select(configurationModel.getPriceSelector());
        Element[] priceElement = new Element[price.size()];
        price.toArray(priceElement);

        Elements pictures = document.select(configurationModel.getImageSelector());
        Element[] pictureElement = new Element[pictures.size()];
        pictures.toArray(pictureElement);

        Elements all = document.select(".js-product-miniature .img_block");
        Element[] elements = new Element[all.size()];
        all.toArray(elements);

        Element avalable = document.select(configurationModel.getNotAvalable()).select("p").first();

        Elements games = document.select(configurationModel.getGameId());
        Element[] gameId = new Element[games.size()];
        games.toArray(gameId);

        List<Game> titles = new ArrayList<>();

        for (int i = 0; i < name.size(); i++) {
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
            titles.add(gameTitle);
        }
        return titles;
    }

    @Override
    public void saveGames(List<Game> games) {
        //repo get shop
        ShopInfo shopInfo = shopRepository.findByName(ShopEnum.SHOPGRACZ.getName());
        //loop over games && connect with shop
        for (Game name : games) {
            name.setShop(shopInfo);
        }
        //in loop save to db - add or update
        for (Game name : games) {
//        find by id from site from shop
            gameRepository.save(name);
        }
        shopInfo.setScrapDate(LocalDateTime.now());
        shopRepository.save(shopInfo);
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
    public void startScrapping(ConfigurationModel configurationModel) throws IOException {
        Document document = connectToSite(configurationModel);
        String number = getPageNumbers(document, configurationModel);
        if (number.equals(configurationModel.getFirstPageUrl())) {
            List<Game> games = scrapGames(document, configurationModel);
            saveGames(games);
        } else {
            int lastSiteNumber = Integer.parseInt(number);
            for (int i = 1; i < lastSiteNumber; i++) {
                List<Game> games = scrapGames(document, configurationModel);
                saveGames(games);
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//        wait time!
            }
        }
    }

    @Override
    public void startScrapingForAllConsoles() throws IOException {
        for (int i = 0; i < MODEL.length; i++) {

            if(currentProxy == ProxyEnum.values().length){
            startScrapping(MODEL[i]);

            }
            startScrapping(MODEL[i]);
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
}
