package be.juvo.paul.battleship.entities;

import com.vaadin.flow.component.grid.Grid;
import lombok.*;

import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PlayConditions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column
    private String name;
    @Column
    private int gridSize;
    @Column
    private Grid gamingGrid;
}
