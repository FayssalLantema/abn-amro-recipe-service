package nl.quintor.abn.recipe.controller;

import nl.quintor.abn.recipe.controller.dto.person.CreatePersonDto;
import nl.quintor.abn.recipe.controller.dto.person.PersonDto;
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

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("Create new person")
    void givenUsernameAndPassword_whenCallingCreateUser_thenReturnCreatedPerson() {
        //Given
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setUsername("fayssal");
        createPersonDto.setPassword("password");
        HttpEntity<CreatePersonDto> requestEntity = new HttpEntity<>(createPersonDto);

        //When
        ResponseEntity<PersonDto> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/persons",
                HttpMethod.POST,
                requestEntity,
                PersonDto.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Objects.requireNonNull(response.getBody()).getUsername()).isEqualTo("fayssal");
        assertThat(Objects.requireNonNull(response.getBody()).getCreatedRecipes()).isEqualTo(List.of());
    }

    @Test
    @DisplayName("Try to create new duplicate person")
    void givenDuplicateUsername_whenCallingCreateUser_thenReturnException() {
        //Given
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setUsername("duplicate");
        createPersonDto.setPassword("password");
        HttpEntity<CreatePersonDto> requestEntity = new HttpEntity<>(createPersonDto);

        testRestTemplate.exchange(
                "http://localhost:" + port + "/api/persons",
                HttpMethod.POST,
                requestEntity,
                PersonDto.class
        );

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/persons",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).contains("message");
        assertThat(response.getBody()).contains("Person with username duplicate already exist");

        assertThat(response.getBody()).contains("timestamp");
    }

    @Test
    @DisplayName("Try to create new person with invalid body")
    void givenTooLittleCharacters_whenCallingCreateUser_thenReturnException() {
        //Given
        CreatePersonDto createPersonDto = new CreatePersonDto();
        createPersonDto.setUsername("wr");
        createPersonDto.setPassword("wrong");
        HttpEntity<CreatePersonDto> requestEntity = new HttpEntity<>(createPersonDto);

        //When
        ResponseEntity<String> response = testRestTemplate.exchange(
                "http://localhost:" + port + "/api/persons",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).contains("username");
        assertThat(response.getBody()).contains("password");
    }
}