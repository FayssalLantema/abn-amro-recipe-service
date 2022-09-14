package nl.quintor.abn.recipe.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import nl.quintor.abn.recipe.controller.dto.instruction.CreateInstructionDto;
import nl.quintor.abn.recipe.controller.dto.instruction.InstructionDto;
import nl.quintor.abn.recipe.controller.dto.instruction.InstructionMapper;
import nl.quintor.abn.recipe.controller.dto.recipe.CreateRecipeDto;
import nl.quintor.abn.recipe.controller.dto.recipe.PatchRecipeDto;
import nl.quintor.abn.recipe.controller.dto.recipe.RecipeDto;
import nl.quintor.abn.recipe.controller.dto.recipe.RecipeMapper;
import nl.quintor.abn.recipe.exception.*;
import nl.quintor.abn.recipe.model.Recipe;
import nl.quintor.abn.recipe.service.RecipeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    /**
     * Method which fetches the specific recipes based on the persons input
     *
     * @param personId          the ID of the requesting person
     * @param vegetarian        boolean if the dish needs to be vegetarian
     * @param servings          the amount of servings the dish needs to have
     * @param includeIngredient include specific ingredients
     * @param excludeIngredient exclude specific ingredients
     * @param instruction       filter for specific text in instructions
     * @return the recipes based on the person wishes
     */
    @GetMapping
    @Operation(summary = "Get all recipes from a specific person based on the wishes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched recipes"),
            @ApiResponse(code = 404, message = "Person not found", response = PersonNotFoundException.class)
    })
    public ResponseEntity<List<RecipeDto>> searchForRecipes(
            @RequestHeader(HttpHeaders.AUTHORIZATION) long personId,
            @RequestParam(required = false) Optional<Boolean> vegetarian,
            @RequestParam(required = false) Optional<Integer> servings,
            @RequestParam(required = false) Optional<List<String>> includeIngredient,
            @RequestParam(required = false) Optional<List<String>> excludeIngredient,
            @RequestParam(required = false) Optional<String> instruction
    ) {

        var recipes = recipeService.search(personId,
                vegetarian,
                servings,
                includeIngredient,
                excludeIngredient,
                instruction
        );

        List<RecipeDto> recipeList = RecipeMapper.INSTANCE.toListRecipeDto(
                recipes
        );

        return ResponseEntity.ok().body(recipeList);
    }

    /**
     * Method which creates a new recipe based on the given values
     *
     * @param createRecipeDto the DTO that gives the params for creating a recipe
     * @param personId        the ID of the person who operates the action. The personID is based on the Authorization header
     * @return a ResponseEntity with the created recipe
     */
    @PostMapping
    @Operation(summary = "Create new recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created recipe"),
            @ApiResponse(code = 404, message = "Person with the given person id does not exist", response = PersonNotFoundException.class),
    })
    public ResponseEntity<RecipeDto> createRecipe(@Valid @RequestBody CreateRecipeDto createRecipeDto, @RequestHeader(HttpHeaders.AUTHORIZATION) long personId) {

        var recipeDto = RecipeMapper.INSTANCE.toRecipeDto(
                recipeService.createRecipe(
                        createRecipeDto.getName(),
                        createRecipeDto.getNumberOfServings(),
                        personId
                )
        );

        return ResponseEntity.created(URI.create("/recipes/" + recipeDto.getId())).body(recipeDto);
    }

    /**
     * Method which allows the user to modify a recipe
     *
     * @param recipeId       the recipe that needs to be modified
     * @param patchRecipeDto the DTO that gives the properties which need to be modified
     * @param personId       the ID of the person who operates the action. The personID is based on the Authorization header
     * @return the modified version of the recipe
     */
    @PatchMapping("/{recipeId}")
    @Operation(summary = "Modify recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully modified"),
            @ApiResponse(code = 404, message = "The recipe is not yours", response = DoesNotBelongException.class)
    })
    public ResponseEntity<RecipeDto> modifyRecipe(@PathVariable long recipeId, @Valid @RequestBody PatchRecipeDto patchRecipeDto, @RequestHeader(HttpHeaders.AUTHORIZATION) long personId) {

        var map = new HashMap<>();

        if (patchRecipeDto.getNumberOfServings() != null) {
            map.put("numberOfServings", patchRecipeDto.getNumberOfServings());
        }
        if (patchRecipeDto.getName() != null) {
            map.put("name", patchRecipeDto.getName());
        }

        Recipe recipe = recipeService.modifyRecipe(recipeId, personId, map);

        return ResponseEntity.ok(
                RecipeMapper.INSTANCE.toRecipeDto(
                        recipe
                )
        );
    }

    /**
     * Method which deletes a recipe
     *
     * @param personId the ID of the person who operates the action. The personID is based on the Authorization header
     * @param recipeId the recipe that needs to be deleted
     * @return a ResponseEntity that confirms the recipe is successfully deleted
     */
    @DeleteMapping("/{recipeId}")
    @Operation(summary = "Delete a specific recipe")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted recipe")
    })
    public ResponseEntity<?> deleteRecipe(@RequestHeader(HttpHeaders.AUTHORIZATION) long personId, @PathVariable long recipeId) {

        recipeService.delete(personId, recipeId);

        return ResponseEntity.ok().build();
    }

    /**
     * Method which creates a new instruction
     *
     * @param createInstructionDto the DTO that gives the params for creating an instruction
     * @param recipeId             the recipe where the instruction needs to be attached to
     * @param personId             the ID of the person who operates the action. The personID is based on the Authorization header
     * @return a ResponseEntity with the created instruction
     */
    @PostMapping("/{recipeId}")
    @Operation(summary = "Create new instruction")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created instruction"),
            @ApiResponse(code = 404, message = "Ingredient not found", response = IngredientDoesNotExistException.class),
            @ApiResponse(code = 404, message = "Recipe not found", response = RecipeNotFoundException.class)
    })
    public ResponseEntity<InstructionDto> createInstruction(@Valid @RequestBody CreateInstructionDto createInstructionDto, @PathVariable long recipeId, @RequestHeader(HttpHeaders.AUTHORIZATION) long personId) {

        var instructionDto = InstructionMapper.INSTANCE.toInstructionDto(
                recipeService.createInstruction(
                        createInstructionDto.getWayOfPreperation(),
                        createInstructionDto.getIngredientName(),
                        recipeId,
                        personId
                )
        );

        return ResponseEntity.created(URI.create("/instructions/" + instructionDto.getId())).body(instructionDto);
    }

    /**
     * Method which deletes an instruction
     *
     * @param recipeId      the recipe where the instruction belongs to
     * @param instructionId the ID of the instruction that needs to be deleted
     * @param personId      the ID of the person who operates the action. The personID is based on the Authorization header
     * @return a confirmation if the instruction is successfully deleted
     */
    @DeleteMapping("/{recipeId}/instructions/{instructionId}")
    @Operation(summary = "Delete instruction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted instruction"),
            @ApiResponse(code = 404, message = "Instruction not found", response = InstructionNotFoundException.class),
            @ApiResponse(code = 404, message = "Recipe", response = RecipeNotFoundException.class),
    })
    public ResponseEntity<?> deleteInstruction(@PathVariable long recipeId, @PathVariable long instructionId, @RequestHeader(HttpHeaders.AUTHORIZATION) long personId) {

        recipeService.deleteInstruction(recipeId, instructionId, personId);

        return ResponseEntity.ok().build();
    }


}
