package nl.quintor.abn.recipe.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String wayOfPreperation;

    @ManyToOne
    private Ingredient ingredient;

    @ManyToOne
    private Recipe recipe;


    public Instruction(String wayOfPreperation, Ingredient ingredient, Recipe recipe) {
        this.wayOfPreperation = wayOfPreperation;
        this.ingredient = ingredient;
        this.recipe = recipe;
    }
}
