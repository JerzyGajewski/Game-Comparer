package pl.jerzygajewski.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jerzygajewski.game.service.MainScrappingServiceImpl;

@Controller
public class ShopController {
    private MainScrappingServiceImpl mainScrappingService;

    @Autowired
    public ShopController(MainScrappingServiceImpl mainScrappingService) {
        this.mainScrappingService = mainScrappingService;
    }

    @GetMapping()
    public String addShop() {
        return "startPage";
    }
}
