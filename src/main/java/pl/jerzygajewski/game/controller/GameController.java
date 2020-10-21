package pl.jerzygajewski.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jerzygajewski.game.utill.NoGameKWGames;
import pl.jerzygajewski.game.utill.ShopGraczGames;

import java.io.IOException;

@Controller
public class GameController {
    private NoGameKWGames noGameKWGames;
    private ShopGraczGames shopGraczGames;

    @Autowired
    public GameController(NoGameKWGames noGameKWGames, ShopGraczGames shopGraczGames) {
        this.noGameKWGames = noGameKWGames;
        this.shopGraczGames = shopGraczGames;
    }




    @GetMapping("/start")
    @ResponseBody
    public String saveGamesToDb() {
        try {
               noGameKWGames.startScrapingForAllConsoles();
               shopGraczGames.startScrapingForAllConsoles();
            }
         catch (IOException e) {
            e.printStackTrace();
        }
        return "saved";
    }

}
