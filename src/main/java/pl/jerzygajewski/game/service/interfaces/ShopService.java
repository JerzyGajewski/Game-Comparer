package pl.jerzygajewski.game.service.interfaces;

import pl.jerzygajewski.game.entity.ShopInfo;

public interface ShopService {

    void saveShop(ShopInfo shopInfo);

    ShopInfo findById(Long id);
}
