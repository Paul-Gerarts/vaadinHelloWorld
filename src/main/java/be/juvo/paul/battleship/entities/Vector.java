package be.juvo.paul.battleship.entities;

import lombok.*;

import javax.persistence.*;

@Table(name = "boat_position")
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Vector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boat_position_id")
    private Long id;

    // mind to re-define database keywords
    @Column(name = "grid_row")
    private String row;

    @Column
    private Integer column;

    @Column
    private Boolean hit;

    @Column
    private Boolean myBoats;

    @Column
    private Boolean containsBoat;

    @Column
    private String boatName;
}
