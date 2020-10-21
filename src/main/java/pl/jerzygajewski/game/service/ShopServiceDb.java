package pl.jerzygajewski.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.ShopInfo;
import pl.jerzygajewski.game.repository.ShopRepository;
import pl.jerzygajewski.game.service.interfaces.ShopService;

@Service
public class ShopServiceDb implements ShopService {

    private ShopRepository shopRepository;

    @Autowired
    public ShopServiceDb(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public void saveShop(ShopInfo shopInfo) {
        this.shopRepository.save(shopInfo);
    }

    @Override
    public ShopInfo findById(Long id) {
        return this.shopRepository.findById(id).orElseGet(null);
    }
}
