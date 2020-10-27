package pl.jerzygajewski.game.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.enums.ShopEnum;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.MainScrappingService;
import pl.jerzygajewski.game.utill.NoGameKWGames;
import pl.jerzygajewski.game.utill.ShopGraczGames;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    //  jeden watek - 67636
//    dwa watki - 56.2
//    10 watkow 56.4
//     10 watkow  10?
//    jeden watek  --- wg czasu 47.5
//    Pytanie odnosnie autoupdate?
//    @Scheduled
    @Override
    public void getShopDataToApp() {
        if (shopRepository.findAll().isEmpty()) {
            for (int i = 0; i < ShopEnum.values().length; i++) {
                ShopInfo shopInfo = new ShopInfo();
                shopDetails(i, shopInfo);
            }
        } else
            for (int i = 0; i < ShopEnum.values().length; i++) {
                if (shopRepository.findByName(ShopEnum.values()[i].getName()) == null) {
                    ShopInfo shopInfo = new ShopInfo();
                    shopDetails(i, shopInfo);
                }
            }
    }

    private void shopDetails(int i, ShopInfo shopInfo) {
        shopInfo.setName(ShopEnum.values()[i].getName());
        shopInfo.setPhone(ShopEnum.values()[i].getPhone());
        shopInfo.setEmail(ShopEnum.values()[i].getEmail());
        shopInfo.setAddress(ShopEnum.values()[i].getAddress());
        shopInfo.setWorkingHours(ShopEnum.values()[i].getWorkingHours());
        shopRepository.save(shopInfo);
    }

    // zabezpieczenie przed odświeżeniem strony!!!
//    scheduled jak dokładnie działa
//    dodanie wątków!
    //    Factory Design Pattern
    @Scheduled(cron = "0 20 10 * * *")
    @Override
    public void getServiceToScrap() throws InterruptedException {
        System.out.println("scraping");
        long start = System.currentTimeMillis();
//        Get Data from db = which  shop game name

        List<ShopInfo> shop = shopRepository.findFirstByOrderByScrapDateAsc();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < shop.size(); i++) {
            executorService.submit(() -> {
////            porownanie z gra, jeśli dane są takie same to shop update date
////            jesli rozne to update game and shop date
//            noGameKWGames.startScrapingForAllConsoles();
                try {
                    shopGraczGames.startScrapingForAllConsoles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            TimeUnit.SECONDS.sleep(10);
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
