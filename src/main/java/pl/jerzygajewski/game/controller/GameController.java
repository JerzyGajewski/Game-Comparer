package pl.jerzygajewski.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.MainScrappingServiceImpl;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class GameController {

    private ShopRepository shopRepository;
    private GameRepository gameRepository;
    private MainScrappingServiceImpl mainScrappingService;


    @Autowired
    public GameController(MainScrappingServiceImpl mainScrappingService, ShopRepository shopRepository,
                          GameRepository gameRepository) {
        this.mainScrappingService = mainScrappingService;
        this.shopRepository = shopRepository;
        this.gameRepository = gameRepository;
    }


    @GetMapping("/scrap")
    @ResponseBody
    public String scrap() {
        mainScrappingService.getShopDataToApp();
        mainScrappingService.editShopData();

        try {
            mainScrappingService.getServiceToScrap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "scraping";
    }


    @GetMapping("/start")
    public String saveGamesToDb(HttpSession session) {
        session.invalidate();
        //czyszczenie sesji
        return "search";
    }

    @ModelAttribute("shops")
    public List<ShopInfo> shopList() {
        return shopRepository.findAll();
    }

    // jak dodam shop dostaje null
    @GetMapping("/result")
    public String getResults(Model model,
                             @Param("gameName") String gameName,
                             @Param("console") String console,
                             @Param("shop") String shop,
                             HttpSession session
    ) {
        List<Game> gameList;

            if (session.getAttribute("result") != null) {
                gameList = (List<Game>) session.getAttribute("result");
            } else {
                if(shop.equals("all")){
                 gameList = gameRepository.findAll(gameName, console);
                } else {
                    gameList = gameRepository.searchGames(gameName, console, shop);
                }
                session.setAttribute("result", gameList);
            }

        if (gameList.isEmpty()) {
            return "notFound";
        }
        model.addAttribute("games", gameList);
        return "specyficResults";

    }

    // shopName dostaje null
    @PostMapping("/shopDetails")
    public String details(Model model, @Param("shopName") String shopName) {
        ShopInfo shop = shopRepository.findOneByName(shopName);
        model.addAttribute("shopInfo", shop);


        return "shopDetails";
    }

}
