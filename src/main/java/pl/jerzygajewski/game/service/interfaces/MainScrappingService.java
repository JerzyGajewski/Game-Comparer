package pl.jerzygajewski.game.service.interfaces;

import pl.jerzygajewski.game.entity.ShopInfo;

import java.io.IOException;

public interface MainScrappingService {

    void getShopDataToApp();

    void getServiceToScrap() throws InterruptedException;

}
