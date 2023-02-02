package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ShopEnum {
    NOGAMENH("NoGameNH", "Osiedle Handlowe 5, 31-939 Kraków",
            "Monday\n" + "11AM–6PM\n" + "\n" + "Tuesday\n" + "11AM–6PM\n" +
                    "\n" + "Wednesday\n" + "11AM–6PM\n" + "\n" + "Thursday\n" + "11AM–6PM\n" +
                    "Friday\n" + "11AM–6PM\n" + "\n" + "Saturday\n" + "11AM–2PM\n" + "\n" +
                    "Sunday\n" + "(All Saints' Day)\n" + "Closed\n" + "Hours might differ",
            "sklepnh@nogame.pl",
            "+48796783979",
"!1m14!1m8!1m3!1d10242.345454253615!2d20.0307872!3d50.0753077!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xf5c3130b9f067105!2sNoGame.pl%20-%20Nowa%20Huta%20Gry%20konsole%20akcesoria!5e0!3m2!1sen!2spl!4v1604024372912!5m2!1sen!2spl"),
//            "https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d10242.345454253615!2d20.0307872!3d50.0753077!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0xf5c3130b9f067105!2sNoGame.pl%20-%20Nowa%20Huta%20Gry%20konsole%20akcesoria!5e0!3m2!1sen!2spl!4v1604024372912!5m2!1sen!2spl"),

    NOGAMEKW("NoGameKW", "Kazimierza Wielkiego 41, 30-074 Kraków",
            "Monday\n" + "11AM–6PM\n" + "\n" + "Tuesday\n" + "11AM–6PM\n" +
                    "\n" + "Wednesday\n" + "11AM–6PM\n" + "\n" + "Thursday\n" + "11AM–6PM\n" +
                    "Friday\n" + "11AM–6PM\n" + "\n" + "Saturday\n" + "11AM–2PM\n" + "\n" +
                    "Sunday\n" + "(All Saints' Day)\n" + "Closed\n" + "Hours might differ",
            "sklepkw@nogame.pl",
            "+48691399659",
"!1m14!1m8!1m3!1d10242.85236476424!2d19.9210905!3d50.0729346!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x81b03d66fc4fcd97!2sNoGame.pl%20-%20Jedyny%20s%C5%82uszny%20sklep%20z%20grami!5e0!3m2!1sen!2spl!4v1604024138105!5m2!1sen!2spl"
//            "https://www.google.com/maps/embed?pb=!1m14!1m8!1m3!1d10242.85236476424!2d19.9210905!3d50.0729346!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x81b03d66fc4fcd97!2sNoGame.pl%20-%20Jedyny%20s%C5%82uszny%20sklep%20z%20grami!5e0!3m2!1sen!2spl!4v1604024138105!5m2!1sen!2spl"
    ),

    GAMEOVER("GameOver", "Radziwiłłowska 26, 31-026 Kraków",
            "Monday\n" + "10AM–8PM\n" + "\n" + "Tuesday\n" + "10AM–8PM\n" +
                    "\n" + "Wednesday\n" + "10AM–8PM\n" + "\n" + "Thursday\n" + "10AM–8PM\n" +
                    "Friday\n" + "10AM–8PM\n" + "\n" + "Saturday\n" + "10AM–8PM\n" + "\n" +
                    "Sunday\n" + "(All Saints' Day)\n" + "Closed\n" + "Hours might differ\n",
            "krakow@gameover.pl",
            "+48507194536",
"!1m14!1m8!1m3!1d10244.786734069083!2d19.9465085!3d50.0638781!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x0%3A0x6b2a70fa99a9256a!2sGame%20Over!5e0!3m2!1sen!2spl!4v1604023827934!5m2!1sen!2spl"
    );
//            "https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2562.2127594934313!2d19.95047781568285!3d50.04484557942138!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x47165b442e8bbc6f%3A0xbc6417b1f2f072fc!2sShop%20Gracz!5e0!3m2!1sen!2spl!4v1603793169205!5m2!1sen!2spl");

    private String name;
    private String address;
    private String workingHours;
    private String email;
    private String phone;
    private String mapLink;

    ShopEnum(String name, String address, String workingHours, String email, String phone, String mapLink) {
        this.name = name;
        this.address = address;
        this.workingHours = workingHours;
        this.email = email;
        this.phone = phone;
        this.mapLink = mapLink;
    }
}
