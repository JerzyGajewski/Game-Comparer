package pl.jerzygajewski.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jerzygajewski.game.entity.Game;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.repository.GameRepository;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.MainScrappingServiceImpl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/start")
    public String saveGamesToDb(Model model) {
        try {
            try {
             mainScrappingService.getServiceToScrap();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         catch (IOException e) {
            e.printStackTrace();
        }
        return "search";
    }
    @ModelAttribute("shops")
    public List<ShopInfo> shopList(){return shopRepository.findAll();}

    @PostMapping("/result")
    public String getResults(Model model){
//        model.getAttribute("gameName");
//        model.getAttribute("shopName");
//        model.getAttribute("selector1");
        List<Game> gameList = gameRepository.findAll();
        gameList.stream()
                .filter(game -> game.getTitle().toLowerCase().trim().equals(model.getAttribute("getName")))
                .filter(game -> game.getShop().equals(model.getAttribute("shopName")))
                .filter(game -> game.getConsoleType().equals(model.getAttribute("selector1")))
                .collect(Collectors.toList());

        model.addAttribute("games", gameList);
        return "specyficResults";
    }

}
