package pl.jerzygajewski.game.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.serviceInterfaces.MainScrappingService;
import pl.jerzygajewski.game.utill.ShopGraczGames;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class MainScrappingServiceImpl implements MainScrappingService {
        ShopRepository shopRepository;
        ShopGraczGames shopGraczGames;


    public MainScrappingServiceImpl(ShopRepository shopRepository, ShopGraczGames shopGraczGames) {
        this.shopRepository = shopRepository;
        this.shopGraczGames = shopGraczGames;
    }

    //    Factoray Design Pattera
//    @Scheduled(fixedDelay = 10000)
    @Override
    public void getServiceToScrap() throws IOException, InterruptedException {
        System.out.println("scraping");
//        Get Data from db = which  shop game name

        List<ShopInfo> shop = shopRepository.findFirstByOrderByScrapDateAsc();
        for (int i = 0; i < shop.size(); i++) {
////            porownanie z gra, jeśli dane są takie same to shop update date
////            jesli rozne to update game and shop date

            shopGraczGames.startScrapingForAllConsoles();
            TimeUnit.SECONDS.sleep(10);
        }
    }
}
