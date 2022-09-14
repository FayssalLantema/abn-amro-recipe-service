package nl.quintor.abn.recipe.controller;

import nl.quintor.abn.recipe.controller.dto.instruction.CreateInstructionDto;
import nl.quintor.abn.recipe.controller.dto.instruction.InstructionDto;
import nl.quintor.abn.recipe.controller.dto.recipe.CreateRecipeDto;
import nl.quintor.abn.recipe.controller.dto.recipe.PatchRecipeDto;
import nl.quintor.abn.recipe.controller.dto.recipe.RecipeDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RecipeControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Get all recipes from a specific person")
    void givenPersonId_whenCallingGetAllRecipes_thenReturnRecipes() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "2");

        //When
        ResponseEntity<RecipeDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RecipeDto[].class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("Get all recipes from a specific person based on search paramaters vegetarian")
    void givenPersonIdandParameters_whenCallingGetAllRecipes_thenReturnRecipes() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");
        boolean vegetarian = true;

        //When
        ResponseEntity<RecipeDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes?vegetarian=" + vegetarian,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RecipeDto[].class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Get all recipes from a specific person based on multiple search paramaters")
    void givenPersonIdandMultipleParameters_whenCallingGetAllRecipes_thenReturnRecipes() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");
        boolean vegetarian = true;
        int amountOfServings = 3;

        //When
        ResponseEntity<RecipeDto[]> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes?vegetarian=" + vegetarian + "&amountOfServings=" + amountOfServings,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                RecipeDto[].class);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("Create new recipe")
    void givenNameNumberOfServingsAndPersonId_whenCallingCreateRecipe_thenReturnCreatedRecipe() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Chicken Teriyaki");
        createRecipeDto.setNumberOfServings(3);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<RecipeDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Chicken Teriyaki");
        assertThat(Objects.requireNonNull(response.getBody()).getNumberOfServings()).isEqualTo(3);
    }

    @Test
    @DisplayName("Try to create new recipe with a non-existing person")
    void givenNonExistingPerson_whenCallingCreateRecipe_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "4");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Rice with Chicken and Red Sauce");
        createRecipeDto.setNumberOfServings(2);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("message");
        assertThat(response.getBody()).contains("Person with id 4 is not found");

        assertThat(response.getBody()).contains("timestamp");
    }

    @Test
    @DisplayName("Try to create new recipe with invalid body")
    void givenWrongBody_whenCallingCreateRecipe_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("a");
        createRecipeDto.setNumberOfServings(0);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("numberOfServings");
        assertThat(response.getBody()).contains("name");
    }

    @Test
    @DisplayName("Modify recipe")
    void GivenNewRecipeInformation_WhenModifyingRecipe_ThenReturnUpdatedRecipe() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Chicken Teriyaki");
        createRecipeDto.setNumberOfServings(3);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        var patchRecipeDto = new PatchRecipeDto();
        patchRecipeDto.setName("Chicken Tonight");
        patchRecipeDto.setNumberOfServings(5);

        //When
        ResponseEntity<RecipeDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(patchRecipeDto, headers),
                RecipeDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(patchRecipeDto.getName());
        assertThat(response.getBody().getNumberOfServings()).isEqualTo(patchRecipeDto.getNumberOfServings());
    }

    @Test
    @DisplayName("Modify recipe that is not yours")
    void GivenNewRecipeInformation_WhenModifyingOthersRecipe_ThenThrowError() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Chicken Teriyaki");
        createRecipeDto.setNumberOfServings(3);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        var patchRecipeDto = new PatchRecipeDto();
        patchRecipeDto.setName("Chicken Tonight");
        patchRecipeDto.setNumberOfServings(5);

        var newHeaders = new HttpHeaders();
        newHeaders.set("Authorization", "4");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(patchRecipeDto, newHeaders),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(Objects.requireNonNull(response.getBody())).contains("Not the owner of the recipe");
    }

    @Test
    @DisplayName("Delete recipe")
    void GivenRecipeID_WhenCallingDeleteRecipe_ThenSucceed() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateRecipeDto createRecipeDto = new CreateRecipeDto();
        createRecipeDto.setName("Chicken Teriyaki");
        createRecipeDto.setNumberOfServings(3);
        HttpEntity<CreateRecipeDto> requestEntity = new HttpEntity<>(createRecipeDto, headers);

        ResponseEntity<RecipeDto> initResponse = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes",
                HttpMethod.POST,
                requestEntity,
                RecipeDto.class
        );

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/" + Objects.requireNonNull(initResponse.getBody()).getId(),
                HttpMethod.DELETE,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Create new instruction")
    void givenIngredientNameAndWayOfPreperation_whenCallingCreateInstruction_thenReturnCreatedInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("Spinach");
        createInstructionDto.setWayOfPreperation("150G in the oven at 200 degrees for 10 minutes");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<InstructionDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1",
                HttpMethod.POST,
                requestEntity,
                InstructionDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getWayOfPreperation()).isEqualTo(createInstructionDto.getWayOfPreperation());
        assertThat(Objects.requireNonNull(response.getBody()).getIngredient().getName()).isEqualTo(createInstructionDto.getIngredientName());
    }

    @Test
    @DisplayName("Try to create new instruction with invalid body")
    void givenWrongBody_whenCallingCreateInstruction_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("a");
        createInstructionDto.setWayOfPreperation("short");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("wayOfPreperation");
        assertThat(response.getBody()).contains("ingredientName");
    }

    @Test
    @DisplayName("Try to create new instruction with non existing ingredient")
    void givenNonExistingIngredientName_whenCallingCreateInstruction_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("DoesNotExist");
        createInstructionDto.setWayOfPreperation("300G cooking for 5 minutes");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Ingredient with name DoesNotExist does not exist");
    }

    @Test
    @DisplayName("Try to create new instruction with non existing recipe")
    void givenNonExistingRecipe_whenCallingCreateInstruction_thenReturnException() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        CreateInstructionDto createInstructionDto = new CreateInstructionDto();
        createInstructionDto.setIngredientName("Spinach");
        createInstructionDto.setWayOfPreperation("300G cooking for 5 minutes");
        HttpEntity<CreateInstructionDto> requestEntity = new HttpEntity<>(createInstructionDto, headers);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1200",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Recipe with id 1200 is not found");
    }

    @Test
    @DisplayName("Delete instruction")
    void givenInstructionAndRecipeId_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1/instructions/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("Delete instruction but non-existing instruction")
    void givenNonExistingInstruction_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/1/instructions/100",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Delete instruction but non-existing recipe")
    void givenNonExistingRecipe_whenDeletingInstruction_thenDeleteInstruction() {
        //Given
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "1");

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/recipes/100/instructions/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}