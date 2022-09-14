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
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String username;

    @Column
    private String password;

    @OneToMany(mappedBy = "createdBy")
    private List<Recipe> createdRecipes;

    public Person(String username, String password) {
        this.username = username;
        this.password = password;
        this.createdRecipes = new ArrayList<>();
    }
}
