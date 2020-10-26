package pl.jerzygajewski.game.service.serviceInterfaces;

import org.springframework.ui.Model;
import pl.jerzygajewski.game.entity.ShopInfo;

import java.io.IOException;

public interface MainScrappingService{

    ShopInfo getShopDataToApp();

void getServiceToScrap() throws IOException, InterruptedException;

}
