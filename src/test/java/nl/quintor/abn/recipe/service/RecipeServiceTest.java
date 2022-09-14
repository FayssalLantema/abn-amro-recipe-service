package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.RecipeNotFoundException;
import nl.quintor.abn.recipe.model.Ingredient;
import nl.quintor.abn.recipe.model.Instruction;
import nl.quintor.abn.recipe.model.Person;
import nl.quintor.abn.recipe.model.Recipe;
import nl.quintor.abn.recipe.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    Person person;
    Recipe recipe;
    Instruction instruction;

    @Mock
    RecipeRepository mockRecipeRepository;

    @Mock
    PersonService mockPersonService;

    @Mock
    InstructionService mockInstructionService;

    @InjectMocks
    RecipeService recipeService;

    @BeforeEach
    void init() {
        person = new Person("Quintor", "Password");
        recipe = new Recipe("Chicken Teriyaki", 3, person);
        instruction = new Instruction("150G in the oven at 200 degrees for 10 minutes", new Ingredient("Cucumber", true), recipe);
    }

    @Test
    @DisplayName("Find recipe by ID")
    void GivenRecipeId_WhenFindingRecipe_ThenReturnRecipe() {
        //Given
        recipe.setId(1L);

        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        Recipe result = recipeService.findById(recipe.getId());

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServings()).isEqualTo(recipe.getNumberOfServings());
        assertThat(result.getCreatedBy().getId()).isEqualTo(person.getId());

        verify(mockRecipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find non-existing recipe by ID")
    void GivenNonExistingRecipeId_WhenFindingRecipe_ThenThrowException() {
        //Given
        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> recipeService.findById(1L)).isInstanceOf(RecipeNotFoundException.class);
        verify(mockRecipeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find all recipes of person")
    void GivenPersonId_WhenFindingAllRecipesOfPerson_ThenReturnListOfRecipes() {
        //Given
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Tomato salsa", 3, person));
        recipes.add(new Recipe("Potatoes with vegetables", 2, person));

        when(mockPersonService.getById(anyLong()))
                .thenReturn(person);

        lenient().when(mockRecipeRepository.searchAll(anyLong(), any(), any(), any(), any(), any()))
                .thenReturn(recipes);

        //When
        List<Recipe> result = recipeService.search(1L, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        //Then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(recipes);
    }

    @Test
    @DisplayName("Successfully create new recipe")
    void GivenNameNumberOfServingsAndPersonId_WhenCreatingRecipe_ThenReturnCreatedRecipe() {
        //Given
        when(mockRecipeRepository.save(any()))
                .thenReturn(recipe);

        when(mockPersonService.getById(anyLong()))
                .thenReturn(person);

        //When
        Recipe result = recipeService.createRecipe(recipe.getName(), recipe.getNumberOfServings(), 1L);

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServings()).isEqualTo(recipe.getNumberOfServings());
        assertThat(result.getCreatedBy().getId()).isEqualTo(person.getId());

        verify(mockRecipeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Modify recipe")
    void GivenUpdatedValues_WhenUpdatingRecipe_ThenReturnUpdatedRecipe() {
        //Given
        recipe.setId(1L);
        person.setId(1L);

        var map = new HashMap<>();
        map.put("numberOfServings", 8);
        map.put("name", "ModifiedRecipe");

        recipe.setNumberOfServings(8);
        recipe.setName("ModifiedRecipe");

        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        when(mockRecipeRepository.save(any()))
                .thenReturn(recipe);

        //When
        Recipe result = recipeService.modifyRecipe(1L, 1L, map);

        //Then
        assertThat(result.getName()).isEqualTo(recipe.getName());
        assertThat(result.getNumberOfServings()).isEqualTo(recipe.getNumberOfServings());
        assertThat(result.getCreatedBy().getId()).isEqualTo(person.getId());

        verify(mockRecipeRepository, times(1)).save(any());
    }


    @Test
    @DisplayName("Delete recipe")
    void GivenPersonAndRecipeId_WhenDeletingRecipe_ThenSucceed() {
        //Given
        recipe.setId(1L);
        person.setId(1L);

        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        recipeService.delete(1L, 1L);

        //Then
        verify(mockRecipeRepository, times(1)).delete(any());
        verify(mockRecipeRepository, times(2)).findById(anyLong());
    }

    @Test
    @DisplayName("Successfully create new instruction")
    void GivenVariablesForInstruction_WhenCreatingInstruction_ThenReturnCreatedInstruction() {
        //Given
        when(mockInstructionService.create(anyString(), anyString(), anyLong()))
                .thenReturn(instruction);

        recipe.setId(1L);
        person.setId(1L);

        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        Instruction result = recipeService.createInstruction(instruction.getWayOfPreperation(), instruction.getIngredient().getName(), 1L, 1L);

        //Then
        assertThat(result.getRecipe()).isEqualTo(recipe);
        assertThat(result.getIngredient()).isEqualTo(instruction.getIngredient());
        assertThat(result.getWayOfPreperation()).isEqualTo(instruction.getWayOfPreperation());

        verify(mockInstructionService, times(1)).create(anyString(), anyString(), anyLong());
    }

    @Test
    @DisplayName("Delete instruction")
    void GivenVariablesForInstruction_WhenDeletingInstruction_ThenSucceed() {
        //Given
        recipe.setId(1L);
        person.setId(1L);

        when(mockRecipeRepository.findById(anyLong()))
                .thenReturn(Optional.of(recipe));

        //When
        recipeService.deleteInstruction(1L, 1L, 1L);

        //Then
        verify(mockInstructionService, times(1)).delete(anyLong(), any());
    }
}