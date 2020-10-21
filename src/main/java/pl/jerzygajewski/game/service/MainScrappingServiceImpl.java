package pl.jerzygajewski.game.service;
//
//public class MainScrappingServiceImpl implements MainScrappingService {
//
//
//    @Override
//    public void startScrapping() {
//        ScrappingService scrappingService = getServiceToScrap();
//
//        scrappingService.scrapData();
//
//    }

//    Factoray Design Pattera
//    @Scheduled
//    @Override
//    public void getServiceToScrap() {
//        Get Data from db = which  shop game name
//        Shop shop = repo.findOneByLastDateDesc();
//        ScrappingService scrappingService = null;
//        switch (shop.getName()){
//            case 'NoGame-KW': new NoGameKwScrappingService().startScrapping(); break;
//            case 'NoGame-KW': new NoGameKwScrappingService().startScrapping(); break;
//            case 'NoGame-KW': new NoGameKwScrappingService().startScrapping(); break;
//        }
//    }
//}
