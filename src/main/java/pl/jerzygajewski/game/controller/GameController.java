package pl.jerzygajewski.game.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.jerzygajewski.game.entity.GameTitle;
import pl.jerzygajewski.game.service.serviceInterfaces.GameTitleService;
import pl.jerzygajewski.game.utill.noGame.kw.ps4.NoGameKWps4Games;

import java.io.IOException;
import java.util.List;

@Controller
public class GameController {
    private GameTitleService gameTitleService;

    @Autowired
    public GameController(GameTitleService gameTitleService) {
        this.gameTitleService = gameTitleService;
    }

    @GetMapping()
    @ResponseBody
    public String saveTitle() {
        try {
            NoGameKWps4Games gtu = new NoGameKWps4Games();
            List<String> titleList = gtu.getGameData();
            GameTitle gameTitle = new GameTitle();
            for(String elem : titleList)
                gameTitle.setTitle(elem);
                gameTitleService.save(gameTitle);
            }
         catch (IOException e) {
            e.printStackTrace();
        }
        return "saved";
    }

}
