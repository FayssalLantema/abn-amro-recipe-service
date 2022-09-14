package nl.quintor.abn.recipe.controller;

import nl.quintor.abn.recipe.controller.dto.ingredient.CreateIngredientDto;
import nl.quintor.abn.recipe.controller.dto.ingredient.IngredientDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IngredientControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Create new ingredient")
    void givenNameAndVegetarian_whenCallingCreateIngredient_thenReturnCreatedIngredient() {
        //Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto();
        createIngredientDto.setName("Cucumber");
        createIngredientDto.setVegetarian(true);
        HttpEntity<CreateIngredientDto> requestEntity = new HttpEntity<>(createIngredientDto);

        //When
        ResponseEntity<IngredientDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/ingredients",
                HttpMethod.POST,
                requestEntity,
                IngredientDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Cucumber");
        assertThat(Objects.requireNonNull(response.getBody()).isVegetarian()).isEqualTo(true);
    }

    @Test
    @DisplayName("Try to create new duplicate ingredient")
    void givenDuplicateName_whenCallingCreateIngredient_thenReturnException() {
        //Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto();
        createIngredientDto.setName("Cucumber");
        createIngredientDto.setVegetarian(false);
        HttpEntity<CreateIngredientDto> requestEntity = new HttpEntity<>(createIngredientDto);


        testRestTemplate.exchange(
                "http://localhost:" + port + "/api/ingredients",
                HttpMethod.POST,
                requestEntity,
                IngredientDto.class
        );

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/ingredients",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("message");
        assertThat(response.getBody()).contains("Ingredient with name Cucumber already exist");

        assertThat(response.getBody()).contains("timestamp");
    }

    @Test
    @DisplayName("Try to create new ingredient with invalid body")
    void givenTooLittleCharacters_whenCallingCreateIngredient_thenReturnException() {
        //Given
        CreateIngredientDto createIngredientDto = new CreateIngredientDto();
        createIngredientDto.setName("a");
        createIngredientDto.setVegetarian(true);
        HttpEntity<CreateIngredientDto> requestEntity = new HttpEntity<>(createIngredientDto);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/ingredients",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("name");
    }
}