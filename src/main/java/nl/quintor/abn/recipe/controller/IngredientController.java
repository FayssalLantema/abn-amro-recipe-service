package nl.quintor.abn.recipe.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import nl.quintor.abn.recipe.controller.dto.ingredient.CreateIngredientDto;
import nl.quintor.abn.recipe.controller.dto.ingredient.IngredientDto;
import nl.quintor.abn.recipe.controller.dto.ingredient.IngredientMapper;
import nl.quintor.abn.recipe.exception.IngredientAlreadyExistException;
import nl.quintor.abn.recipe.service.IngredientService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    /**
     * Method which creates a new ingredient based on the given values
     *
     * @param createIngredientDto the DTO that gives the params for creating an ingredient
     * @return a ResponseEntity with the created ingredient
     */
    @PostMapping
    @Operation(summary = "Create new ingredient")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created ingredient"),
            @ApiResponse(code = 409, message = "Ingredient with the given name already exist", response = IngredientAlreadyExistException.class),
    })
    public ResponseEntity<IngredientDto> createIngredient(@Valid @RequestBody CreateIngredientDto createIngredientDto) {

        var ingredientDto = IngredientMapper.INSTANCE.toIngredientDto(
                ingredientService.createIngredient(
                        createIngredientDto.getName(),
                        createIngredientDto.isVegetarian()
                )
        );

        return ResponseEntity.created(URI.create("/ingredients/" + ingredientDto.getId())).body(ingredientDto);
    }
}
