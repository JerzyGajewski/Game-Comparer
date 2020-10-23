package pl.jerzygajewski.game.enums;

import lombok.Getter;

@Getter
public enum ProxyEnum {
    S("163.153.214.50", "8080"), U("216.250.236.10", "3128"), P("165.22.36.75", "8888");

    String Ip;
    String port;

    ProxyEnum(String ip, String port) {
        this.Ip = ip;
        this.port = port;
    }
}
