package pl.jerzygajewski.game.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String title;
    String price;
    String img;
    String avalable;
    String gameShopId;

    String consoleType;

    @ManyToOne
    private ShopInfo shop;
}
