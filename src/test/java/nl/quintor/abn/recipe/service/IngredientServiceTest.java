package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.IngredientAlreadyExistException;
import nl.quintor.abn.recipe.exception.IngredientDoesNotExistException;
import nl.quintor.abn.recipe.model.Ingredient;
import nl.quintor.abn.recipe.repository.IngredientRepository;
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
class IngredientServiceTest {

    Ingredient ingredient;
    @Mock
    IngredientRepository mockIngredientRepository;

    @InjectMocks
    IngredientService ingredientService;

    @BeforeEach
    void init() {
        ingredient = new Ingredient("Cucumber", true);
    }

    @Test
    @DisplayName("Find ingredient by name")
    void GivenIngredientName_WhenFindingIngredient_ThenReturnIngredient() {
        //Given
        when(mockIngredientRepository.findByName(anyString()))
                .thenReturn(Optional.of(ingredient));

        //When
        Ingredient result = ingredientService.findByName(ingredient.getName());

        //Then
        assertThat(result.getName()).isEqualTo(ingredient.getName());
        assertThat(result.isVegetarian()).isEqualTo(ingredient.isVegetarian());

        verify(mockIngredientRepository, times(2)).findByName(anyString());
    }

    @Test
    @DisplayName("Find non-existing ingredient by name")
    void GivenNonExistingIngredientName_WhenFindingIngredient_ThenThrowException() {
        //Given
        when(mockIngredientRepository.findByName(anyString()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> ingredientService.findByName("NotExisting")).isInstanceOf(IngredientDoesNotExistException.class);
        verify(mockIngredientRepository, times(1)).findByName(anyString());
    }

    @Test
    @DisplayName("Successfully create new ingredient")
    void GivenAName_WhenCreatingIngredient_ThenReturnCreatedIngredient() {
        //Given
        when(mockIngredientRepository.save(any()))
                .thenReturn(ingredient);

        //When
        Ingredient result = ingredientService.createIngredient(ingredient.getName(), ingredient.isVegetarian());

        //Then
        assertThat(result.getName()).isEqualTo(ingredient.getName());
        assertThat(result.isVegetarian()).isEqualTo(ingredient.isVegetarian());
        verify(mockIngredientRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Throw exception on trying to create duplicate ingredient")
    void GivenADuplicateName_WhenCreatingIngredient_ThenThrowException() {
        //Given
        when(mockIngredientRepository.findByName(anyString()))
                .thenReturn(Optional.of(ingredient));

        //When / Then
        assertThatThrownBy(() -> ingredientService.createIngredient(ingredient.getName(), ingredient.isVegetarian())).isInstanceOf(IngredientAlreadyExistException.class);
        verify(mockIngredientRepository, times(0)).save(any());
    }


}