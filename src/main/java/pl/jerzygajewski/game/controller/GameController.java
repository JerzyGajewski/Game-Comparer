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

import java.io.IOException;
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

        try {
            mainScrappingService.getServiceToScrap();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "scraping";
    }


    @GetMapping("/start")
    public String saveGamesToDb() {

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
                             @Param("shop") String shop) {

        List<Game> gameList = gameRepository.searchGames(gameName, console, shop);
        if (gameList.size() <= 0) {
            return "notFound";
        }
//        List<Game> filteredList =
//        gameList.stream()
//                .filter(game -> request.get("gameName").toLowerCase().trim().contains(game.getTitle()))
////                .filter(game -> request.get("shopName").toLowerCase().trim().equals(game.getShop().getName()))
////                .filter(game -> request.get("selector1").toLowerCase().trim().equals(game.getConsoleType()))
//                .collect(Collectors.toList());

        model.addAttribute("games", gameList);
        return "specyficResults";
    }

    // shopName dostaje null
    @GetMapping("/shopDetails")
    public String details(Model model, @Param("shopName") String shopName) {
        ShopInfo shop = shopRepository.findOneByName(shopName);
        model.addAttribute("shopName", shop);
        return "shopDetails";
    }

}
