package nl.quintor.abn.recipe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int numberOfServings;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Instruction> instructionList;

    @ManyToOne
    private Person createdBy;

    public Recipe(String name, int numberOfServings, Person createdBy) {
        this.name = name;
        this.numberOfServings = numberOfServings;
        this.instructionList = new ArrayList<>();
        this.createdBy = createdBy;
    }
}
