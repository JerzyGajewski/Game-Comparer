package pl.jerzygajewski.game.utill;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.jerzygajewski.game.entity.GameTitle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GameTitleUtill {

    public List<String> getGameData() throws IOException {
        List<String> titles = new ArrayList<>();
        Connection connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140");
        Document document = connect.get();
        Elements name = document.select(".products.viewphot .product .productname");
        Elements price = document.select(".products.viewphot .product .price");
        Elements picture = document.select(".products.viewphot .product .prodimage .img-wrap img").tagName("img src");

        for (Element names: name){
        titles.add(names.text());
        }
        return titles;
    }
}
