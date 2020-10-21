package pl.jerzygajewski.game.utill;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.consoleTypeEnum.ConsoleEnum;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.ScrapInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NoGameKWGames implements ScrapInterface {
    private ShopRepository shopRepository;
    private GameRepository gameRepository;

    @Autowired
    public NoGameKWGames(ShopRepository shopRepository, GameRepository gameRepository) {
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
    }





//    public List<Ps4Games> getGameData() throws IOException {
//        List<Ps4Games> titles = new ArrayList<>();
//
//        Connection connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140");
//        Document document = connect.get();
//        Elements name = document.select(".products.viewphot .product .productname");
//        Elements price = document.select(".products.viewphot .product .price");
//        Element[] nameElement = new Element[name.size()];
//        name.toArray(nameElement);
//        Element[] priceElement = new Element[price.size()];
//        price.toArray(priceElement);
////        Elements picture = document.select(".products.viewphot .product .prodimage .img-wrap").select("src");
//
//
//        for (int i = 0; i < name.size(); i++) {
//            Ps4Games gameTitle = new Ps4Games();
//            gameTitle.setTitle(nameElement[i].text());
//            gameTitle.setPrice(priceElement[i].text());
////            gameTitle.setImg(picture.get(i).text());
//            titles.add(gameTitle);
//        }
//
//        return titles;
//    }
    @Override
    public void startScrapingForAllConsoles() throws IOException {
        for (int i = 0; i <ConsoleEnum.values().length; i++) {
            switch (ConsoleEnum.values()[i]){
                case PS3:
//                    String ps3 = "";
//                    startScrapping(ps3);
                    break;
                case PS4:
                    String ps4 = "https://www.nogame.pl/pl/c/PlayStation-4/140";
                    String[] elementValues = {".products.viewphot .product .productname", ".products.viewphot .product .price"};
                    startScrapping(ps4, ConsoleEnum.values()[i], elementValues);
                    break;
                case XBOX360:
//                    String xboxOne = "i co";
//                    startScrapping(xboxOne);
                    break;
                case XBOXONE:
                    break;
                case NINTENDOSWITCH:
                    break;
            }
        }
    }

@Override
    public void startScrapping(String url, ConsoleEnum consoleEnum, String[] elementValue) throws IOException {
       Document document = connectToSite(url);
       List<Game> games = scrapGames(document, consoleEnum, elementValue);
       saveGames(games);
    }


    @Override
    public Document connectToSite(String url) throws IOException {
        Connection connect = Jsoup.connect(url);
        return connect.get();
    }

    @Override
    public List<Game> scrapGames(Document document, ConsoleEnum consoleEnum, String[] elementValue) {
        Elements name = document.select(elementValue[0]);
        Element[] nameElement = new Element[name.size()];
        name.toArray(nameElement);

        Elements price = document.select(elementValue[1]);
        Element[] priceElement = new Element[price.size()];
        price.toArray(priceElement);

//        Elements pictures = document.getElementsByClass("prodimage f-row");
//        Element[] pictureElement = new Element[pictures.size()];
//        pictures.toArray(pictureElement);

        List<Game> titles = new ArrayList<>();

        for (int i = 0; i <name.size(); i++) {
            Game gameTitle = new Game();
            gameTitle.setTitle(nameElement[i].text());
            gameTitle.setPrice(priceElement[i].text());
            gameTitle.setConsoleType(consoleEnum.getName()); //enum - typy enumeryczne
//            gameTitle.setImg(pictureElement[i].attr("data-src")); //48
//            gameTitle.setImg(picture.get(i).text());
            titles.add(gameTitle);
        }
        return titles;
    }

    @Override
    public void saveGames(List<Game> games) {
        //repo get shop
        ShopInfo shopInfo = shopRepository.findById(1L).orElseGet(null);
        //loop over games && connect with shop
        for(Game name : games ){
            name.setShop(shopInfo);
        }
        //in loop save to db - add or update
        for(Game name : games){
            gameRepository.save(name);
        }
    }
}
//        for (int i = 2; i <= 31; i++) {
//            connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140/"+i);
//            document = connect.get();
//            name = document.select(".products.viewphot .product .productname");
//            price = document.select(".products.viewphot .product .price");
//            picture = document.select(".products.viewphot .product .prodimage .img-wrap img").tagName("img src");
//
//            for (Element names : name) {
//                titles.add(names.text());
//            }
//        }
