package nl.quintor.abn.recipe.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private boolean vegetarian;

    public Ingredient(String name, boolean vegetarian) {
        this.name = name;
        this.vegetarian = vegetarian;
    }
}
