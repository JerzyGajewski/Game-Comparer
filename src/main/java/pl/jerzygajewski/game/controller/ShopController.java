package pl.jerzygajewski.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.service.interfaces.ShopService;

@Controller
public class ShopController {
    private ShopService shopService;

    @Autowired
    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping()
    public String addShop(Model model) {
        for (int i = 0; i < ShopEnum.values().length; i++) {
            ShopInfo shopInfo = new ShopInfo();
            shopInfo.setName(ShopEnum.values()[i].getName());
            shopService.saveShop(shopInfo);
            model.addAttribute("shop", shopInfo);
        }

        return "startPage";
    }
}
