package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ShopEnum {
    NOGAMEKW("NoGameKW", "", "", "", ""),
    GAMEOVER("GameOver", "", "", "", ""),
    SHOPGRACZ("ShopGracz", "", "Tuesday\n" + "11AM–8PM\n" + "\n" +
            "Wednesday\n" + "11AM–8PM\n" + "\n" + "Thursday\n" + "11AM–8PM\n" + "\n" +
            "Friday\n" + "11AM–8PM\n" + "\n" + "Saturday\n" + "11AM–2PM\n" + "\n" +
            "Sunday\n" + "(All Saints' Day)\n" + "Closed\n" + "Hours might differ\n" + "\n" +
            "Monday\n" + "11AM–8PM", "sklep@shopgracz.pl", "+48608195190");
    private String name;
    private String address;
    private String workingHours;
    private String email;
    private String phone;

    ShopEnum(String name, String address, String workingHours, String email, String phone) {
        this.name = name;
        this.address = address;
        this.workingHours = workingHours;
        this.email = email;
        this.phone = phone;
    }
}
