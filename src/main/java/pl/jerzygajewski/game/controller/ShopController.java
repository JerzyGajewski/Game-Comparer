package pl.jerzygajewski.game.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
    public String addShop() {
        ShopInfo shopInfo = new ShopInfo();
        shopInfo.setName("NoGame");
        shopService.saveShop(shopInfo);
        ShopInfo shopInfo1 = new ShopInfo();
        shopInfo1.setName("ShopGracz");
        shopService.saveShop(shopInfo1);
        return "startPage";
    }
}
