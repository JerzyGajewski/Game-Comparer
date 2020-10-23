package pl.jerzygajewski.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.service.MainScrappingServiceImpl;
import pl.jerzygajewski.game.service.interfaces.ShopService;
import pl.jerzygajewski.game.utill.NoGameKWGames;
import pl.jerzygajewski.game.utill.ShopGraczGames;

import java.io.IOException;

@Controller
public class GameController {
    private NoGameKWGames noGameKWGames;
    private ShopGraczGames shopGraczGames;
    private ShopService shopService;
    private MainScrappingServiceImpl mainScrappingService;

    @Autowired
    public GameController(MainScrappingServiceImpl mainScrappingService) {
        this.mainScrappingService = mainScrappingService;
    }


    public GameController(NoGameKWGames noGameKWGames, ShopGraczGames shopGraczGames, ShopService shopService) {
        this.noGameKWGames = noGameKWGames;
        this.shopGraczGames = shopGraczGames;
        this.shopService=shopService;
    }




    @GetMapping("/start")
    public String saveGamesToDb(Model model) {
        try {
//               noGameKWGames.startScrapingForAllConsoles();
//               shopGraczGames.startScrapingForAllConsoles();
            mainScrappingService.getServiceToScrap();
            }
         catch (IOException e) {
            e.printStackTrace();
        }

        return "search";
    }

    @PostMapping("/result")
    public String getResults(Model model){


        return "specyficResults";
    }

}
