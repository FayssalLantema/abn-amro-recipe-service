package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.DoesNotBelongException;
import nl.quintor.abn.recipe.exception.InstructionNotFoundException;
import nl.quintor.abn.recipe.model.Ingredient;
import nl.quintor.abn.recipe.model.Instruction;
import nl.quintor.abn.recipe.model.Person;
import nl.quintor.abn.recipe.model.Recipe;
import nl.quintor.abn.recipe.repository.InstructionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructionServiceTest {

    Ingredient ingredient;
    Recipe recipe;
    Instruction instruction;

    @Mock
    InstructionRepository mockInstructionRepository;

    @Mock
    RecipeService mockRecipeService;

    @Mock
    IngredientService mockIngredientService;

    @InjectMocks
    InstructionService instructionService;

    @BeforeEach
    void init() {
        ingredient = new Ingredient("Cucumber", true);
        recipe = new Recipe("Chicken Teriyaki", 3, new Person("Quintor", "Password"));
        recipe.setId(1L);

        instruction = new Instruction("150G in the oven at 200 degrees for 10 minutes", ingredient, recipe);
    }

    @Test
    @DisplayName("Find instruction by ID")
    void GivenInstructionId_WhenFetchingInstructionById_ThenReturnInstruction() {
        //Given
        when(mockInstructionRepository.findById(anyLong()))
                .thenReturn(Optional.of(instruction));

        //When
        Instruction result = instructionService.findById(anyLong());

        //Then
        assertThat(result.getWayOfPreperation()).isEqualTo(instruction.getWayOfPreperation());
        assertThat(result.getIngredient()).isEqualTo(ingredient);
        assertThat(result.getRecipe()).isEqualTo(recipe);

        verify(mockInstructionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find instruction by non-existing ID")
    void GivenNonExistingInstructionId_WhenFetchingInstructionById_ThenThrowError() {
        //Given
        when(mockInstructionRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> instructionService.findById(1L)).isInstanceOf(InstructionNotFoundException.class);
        verify(mockInstructionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successfully create new instruction")
    void GivenWayOfPreperationIngredientNameAndRecipeId_WhenCreatingInstruction_ThenReturnCreatedInstruction() {
        //Given
        when(mockIngredientService.findByName(anyString()))
                .thenReturn(ingredient);

        when(mockRecipeService.findById(anyLong()))
                .thenReturn(recipe);

        when(mockInstructionRepository.save(any()))
                .thenReturn(instruction);

        //When
        Instruction result = instructionService.create(instruction.getWayOfPreperation(), ingredient.getName(), recipe.getId());

        //Then
        assertThat(result.getWayOfPreperation()).isEqualTo(instruction.getWayOfPreperation());
        assertThat(result.getIngredient()).isEqualTo(ingredient);
        assertThat(result.getRecipe()).isEqualTo(recipe);

        verify(mockRecipeService, times(1)).findById(anyLong());
        verify(mockIngredientService, times(1)).findByName(anyString());
        verify(mockInstructionRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Delete instruction")
    void GivenInstructionIdAndRecipe_WhenDeletingInstruction_ThenSucceed() {
        //Given
        when(mockInstructionRepository.findById(anyLong()))
                .thenReturn(Optional.of(instruction));

        //When
        instructionService.delete(1L, recipe);

        //Then
        verify(mockInstructionRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Delete wrong instruction")
    void GivenWrongInstructionIdAndRecipe_WhenDeletingInstruction_ThenThrowError() {
        //Given
        when(mockInstructionRepository.findById(anyLong()))
                .thenReturn(Optional.of(instruction));

        //When / Then
        assertThatThrownBy(() -> instructionService.delete(1L, new Recipe())).isInstanceOf(DoesNotBelongException.class);
        verify(mockInstructionRepository, times(1)).findById(anyLong());
    }

}