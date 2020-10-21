//package pl.jerzygajewski.game.service;
//
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import pl.jerzygajewski.game.entity.Ps4Games;
//import pl.jerzygajewski.game.entity.ShopInfo;
//import pl.jerzygajewski.game.repository.GameRepository;
//import pl.jerzygajewski.game.service.interfaces.ScrapInterface;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ScrapService implements ScrapInterface {
//    private GameRepository gameRepository;
//
//    @Autowired
//    public ScrapService(GameRepository gameRepository) {
//        this.gameRepository = gameRepository;
//    }
//
//
//    @Override
//    public Document connectToSite(String url) throws IOException {
//        Connection connect = Jsoup.connect(url);
//        Document document = connect.get();
//        return document;
//    }
//
//    @Override
//    public Element[] scrapGameName(String url, String nameCssQuery) throws IOException {
//        Document document = connectToSite(url);
//        Elements name = document.select(nameCssQuery);
//        Element[] nameElement = new Element[name.size()];
//        name.toArray(nameElement);
//        return nameElement;
//    }
//
//    @Override
//    public Element[] scrapGamePrice(String url, String priceCssQuery) throws IOException {
//        Document document = connectToSite(url);
//        Elements price = document.select(priceCssQuery);
//        Element[] priceElement = new Element[price.size()];
//        price.toArray(priceElement);
//        return priceElement;
//    }
//
//    @Override
//    public List<Ps4Games> gameList(String url, String nameCssQuery, String priceCssQuery) throws IOException {
//        List<Ps4Games> consoleName = new ArrayList<>();
//        Element[] name = scrapGameName(url, nameCssQuery);
//        for (int i = 0; i < scrapGameName(url, nameCssQuery).length; i++) {
//            Ps4Games gt = new Ps4Games();
//            gt.setTitle(name[i].text());
//            gt.setPrice(scrapGamePrice(url, priceCssQuery)[i].text());
//            consoleName.add(gt);
//        }
//        return consoleName;
//    }
//
//    @Override
//    public void saveToTable(List<Ps4Games> gameList, ShopInfo shopInfo) throws IOException {
//        for (Ps4Games gameInfo : gameList) {
//            gameInfo.setShop(shopInfo);
//        }
//        for (Ps4Games games : gameList) {
//            this.gameRepository.save(games);
//        }
//    }
//}
