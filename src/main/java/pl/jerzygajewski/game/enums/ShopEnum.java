package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ShopEnum {
    NOGAMEKW("NoGameKW"), GAMEOVER("GameOver"), SHOPGRACZ("ShopGracz");
    private String name;

    ShopEnum(String name) {
        this.name = name;
    }
}
