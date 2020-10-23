package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ConsoleEnum {
    PS4("ps4"), PS3("ps3"), XBOXONE("xboxOne"), XBOX360("xbox360"), NINTENDOSWITCH("switch");
    private String name;

    ConsoleEnum(String name) {
        this.name = name;
    }


}
