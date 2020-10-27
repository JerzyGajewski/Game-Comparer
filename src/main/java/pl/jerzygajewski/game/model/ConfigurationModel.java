package pl.jerzygajewski.game.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigurationModel {

    private String console;
    private String firstPageUrl;
    private String gameListUrl;
    private String titleSelector;
    private String priceSelector;
    private String imageSelector;
    private String lastPageSelector;
    private String notAvalable;
    private String gameId;

    public ConfigurationModel(String console, String firstPageUrl, String gameListUrl, String titleSelector,
                              String priceSelector, String imageSelector, String lastPageSelector,
                              String notAvalable, String gameId) {
        this.console = console;
        this.firstPageUrl = firstPageUrl;
        this.gameListUrl = gameListUrl;
        this.titleSelector = titleSelector;
        this.priceSelector = priceSelector;
        this.imageSelector = imageSelector;
        this.lastPageSelector = lastPageSelector;
        this.notAvalable = notAvalable;
        this.gameId = gameId;
    }
}
