package pl.jerzygajewski.game.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationModel {

    private String console;
    private String urlPage;
    private String titleSelector;
    private String priceSelector;
    private String imageSelector;
    private String lastPageSelector;
    private String notAvalable;
    private String gameId;

    public ConfigurationModel(String console, String urlPage, String titleSelector,
                              String priceSelector, String imageSelector, String lastPageSelector,
                              String notAvalable, String gameId) {
        this.console = console;
        this.urlPage = urlPage;
        this.titleSelector = titleSelector;
        this.priceSelector = priceSelector;
        this.imageSelector = imageSelector;
        this.lastPageSelector = lastPageSelector;
        this.notAvalable = notAvalable;
        this.gameId = gameId;
    }
}
