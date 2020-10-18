package pl.jerzygajewski.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jerzygajewski.game.entity.GameTitle;
import pl.jerzygajewski.game.repository.GameTitleRepository;
import pl.jerzygajewski.game.service.serviceInterfaces.GameTitleService;

@Service
public class GameTitleServiceDb implements GameTitleService {

    private GameTitleRepository gameTitleRepository;

    @Autowired
    public GameTitleServiceDb(GameTitleRepository gameTitleRepository) {
        this.gameTitleRepository = gameTitleRepository;
    }

    @Override
    public void save(GameTitle gameTitle) {
    this.gameTitleRepository.save(gameTitle);
    }
}
