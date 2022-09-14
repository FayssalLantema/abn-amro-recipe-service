package nl.quintor.abn.recipe.repository;

import nl.quintor.abn.recipe.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query(value = "select distinct recipe from Recipe recipe where recipe.createdBy.id = :personId " +
            "and (:vegetarian is null or :vegetarian is false or (( select count(r) from Recipe r join Instruction instruction on r.id = instruction.recipe.id " +
            "join Ingredient ingredient on instruction.ingredient.id = ingredient.id " +
            "where r.id = recipe.id and vegetarian is not :vegetarian)  = 0)) " +

            "and (:servings is null or :servings = recipe.numberOfServings)" +

            "and (:includeIngredients is null or (( select count(r) from Recipe r join Instruction instruction on r.id = instruction.recipe.id " +
            "join Ingredient ingredient on instruction.ingredient.id = ingredient.id " +
            "where r.id = recipe.id and ingredient.name in :includeIngredients) >= 1)) " +

            "and (:excludeIngredients is null or (( select count(r) from Recipe r join Instruction instruction on r.id = instruction.recipe.id " +
            "join Ingredient ingredient on instruction.ingredient.id = ingredient.id " +
            "where r.id = recipe.id and ingredient.name in :excludeIngredients) = 0)) " +

            "and (:instruction is null or (( select count(r) from Recipe r join Instruction instruction on r.id = instruction.recipe.id " +
            "where r.id = recipe.id and instruction.wayOfPreperation like concat('%', :instruction, '%')) >= 1))")
    List<Recipe> searchAll(
            @Param("personId") long personId,
            @Param("vegetarian") Boolean vegetarian,
            @Param("servings") Integer servings,
            @Param("includeIngredients") List<String> includeIngredients,
            @Param("excludeIngredients") List<String> excludeIngredients,
            @Param("instruction") String instruction);

}
