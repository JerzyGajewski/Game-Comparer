package pl.jerzygajewski.game.utill.noGame.kw.ps4;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NoGameKWps4Games {

    public List<String> getGameData() throws IOException {
        List<String> titles = new ArrayList<>();

        Connection connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/140");
        Document document = connect.get();
        Elements name = document.select(".products.viewphot .product .productname");
        Elements price = document.select(".products.viewphot .product .price");
        Elements picture = document.select(".products.viewphot .product .prodimage .img-wrap img").tagName("img src");

        for (Element names : name) {
            titles.add(names.text());
        }
        for (int i = 2; i <= 31; i++) {
            connect = Jsoup.connect("https://www.nogame.pl/pl/c/PlayStation-4/"+i);
            document = connect.get();
            name = document.select(".products.viewphot .product .productname");
            price = document.select(".products.viewphot .product .price");
            picture = document.select(".products.viewphot .product .prodimage .img-wrap img").tagName("img src");

            for (Element names : name) {
                titles.add(names.text());
            }
        }
        return titles;
    }
}
