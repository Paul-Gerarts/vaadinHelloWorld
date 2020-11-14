package be.juvo.paul.battleship.entities;

import lombok.*;

import javax.persistence.*;

@Table(name = "computer_shot")
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ComputerShot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "computer_shot_id")
    private Long id;

    @Column(name = "grid_row")
    private String row;

    @Column
    private Integer column;

    @Column
    private Boolean designated;

    @Column
    private Boolean hit;
}
