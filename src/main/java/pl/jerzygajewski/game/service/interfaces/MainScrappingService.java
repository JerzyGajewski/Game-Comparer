package pl.jerzygajewski.game.service.interfaces;

public interface MainScrappingService {

    void getShopDataToApp();

    void editShopData();

    void getServiceToScrap() throws InterruptedException;

}
