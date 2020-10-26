package pl.jerzygajewski.game.service;

import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.serviceInterfaces.MainScrappingService;
import pl.jerzygajewski.game.utill.NoGameKWGames;
import pl.jerzygajewski.game.utill.ShopGraczGames;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MainScrappingServiceImpl implements MainScrappingService {
    ShopRepository shopRepository;
    ShopGraczGames shopGraczGames;
    NoGameKWGames noGameKWGames;


    public MainScrappingServiceImpl(ShopRepository shopRepository, ShopGraczGames shopGraczGames,
                                    NoGameKWGames noGameKWGames) {
        this.shopRepository = shopRepository;
        this.shopGraczGames = shopGraczGames;
        this.noGameKWGames = noGameKWGames;
    }

//    Pytanie odnosnie autoupdate?
//    @Scheduled
    @Override
    public ShopInfo getShopDataToApp() {
        if (shopRepository.findAll().isEmpty()) {
            for (int i = 0; i < ShopEnum.values().length; i++) {
                ShopInfo shopInfo = new ShopInfo();
                if (shopRepository.findByName(ShopEnum.values()[i].getName()) == null) {
                    shopInfo.setName(ShopEnum.values()[i].getName());
                    shopRepository.save(shopInfo);
                } else {
                    shopInfo.setName(ShopEnum.values()[i].getName());
                    shopRepository.save(shopInfo);
                    return shopInfo;
                }
            }
        }
        return null;
    }
// zabezpieczenie przed odświeżeniem strony!!!
//    scheduled jak dokładnie działa
//    dodanie wątków!
    //    Factory Design Pattern
//    @Scheduled(fixedDelay = 10000)
    @Override
    public void getServiceToScrap() throws IOException, InterruptedException {
        System.out.println("scraping");
//        Get Data from db = which  shop game name

        List<ShopInfo> shop = shopRepository.findFirstByOrderByScrapDateAsc();
        for (int i = 0; i < shop.size(); i++) {
////            porownanie z gra, jeśli dane są takie same to shop update date
////            jesli rozne to update game and shop date
//            noGameKWGames.startScrapingForAllConsoles();
            shopGraczGames.startScrapingForAllConsoles();
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
