package pl.jerzygajewski.game.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "shops")
public class ShopInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime scrapDate;
    private String address;
    private String workingHours;
    private String email;
    private String phone;
    private String map;

@OneToMany(mappedBy = "shop")
    private List<Game> Game = new ArrayList<>();



}
