package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ProxyEnum {
    S("80.241.222.139", 80), U("180.250.12.10", 80), P("83.96.237.121", 80),
    E("140.227.229.208", 3128), R("96.113.165.182", 3128);

    String Ip;
    int port;

    ProxyEnum(String ip, int port) {
        Ip = ip;
        this.port = port;
    }
}
