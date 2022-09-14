package nl.quintor.abn.recipe.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import nl.quintor.abn.recipe.controller.dto.person.CreatePersonDto;
import nl.quintor.abn.recipe.controller.dto.person.PersonDto;
import nl.quintor.abn.recipe.controller.dto.person.PersonMapper;
import nl.quintor.abn.recipe.exception.PersonAlreadyExistEception;
import nl.quintor.abn.recipe.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.net.URI;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Method which creates a new person based on the given values
     *
     * @param createPersonDto the DTO that gives the params for creating a person
     * @return a ResponseEntity with the created person
     */
    @PostMapping
    @Operation(summary = "Create new person")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully created person"),
            @ApiResponse(code = 409, message = "Person with the given username already exist", response = PersonAlreadyExistEception.class)
    })
    public ResponseEntity<PersonDto> createPerson(@Valid @RequestBody CreatePersonDto createPersonDto) {

        var personDto = PersonMapper.INSTANCE.toPersonDto(
                personService.createPerson(
                        createPersonDto.getUsername(),
                        createPersonDto.getPassword()
                )
        );

        return ResponseEntity.created(URI.create("/persons/" + personDto.getId())).body(personDto);
    }
}
