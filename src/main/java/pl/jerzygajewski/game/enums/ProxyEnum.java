package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ProxyEnum {
    S("163.153.214.50", "8080"), U("216.250.236.10", "3128"), P("165.22.36.75", "8888"),
    E("168.169.96.2", "8080"), R("64.235.204.107", "8080"), PRO("104.149.139.206", "3128"),
    XY("51.75.147.41", "3128");

    String Ip;
    String port;

    ProxyEnum(String ip, String port) {
        this.Ip = ip;
        this.port = port;
    }
}
