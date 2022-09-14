package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.PersonAlreadyExistEception;
import nl.quintor.abn.recipe.exception.PersonNotFoundException;
import nl.quintor.abn.recipe.model.Person;
import nl.quintor.abn.recipe.repository.PersonRepository;
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
class PersonServiceTest {

    Person person;
    @Mock
    PersonRepository mockPersonRepository;

    @InjectMocks
    PersonService personService;

    @BeforeEach
    void init() {
        person = new Person("TestUsername", "TestPassword");
    }

    @Test
    @DisplayName("Get person by ID")
    void givenAPersonId_WhenGetById_ThenReturnUser() {
        //Given
        when(mockPersonRepository.findById(anyLong()))
                .thenReturn(Optional.of(person));

        //When
        Person result = personService.getById(1L);

        //Then
        assertThat(result.getUsername()).isEqualTo(person.getUsername());
        assertThat(result.getPassword()).isEqualTo(person.getPassword());
        verify(mockPersonRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Get non-existing person by ID")
    void givenANonExistingPersonId_WhenGetById_ThenReturnUser() {
        //Given
        when(mockPersonRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        //When / Then
        assertThatThrownBy(() -> personService.getById(10L)).isInstanceOf(PersonNotFoundException.class);
        verify(mockPersonRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Successfully create new person")
    void GivenAnUsernameAndPassword_WhenCreatingPerson_ThenReturnCreatedUser() {
        //Given
        when(mockPersonRepository.save(any()))
                .thenReturn(person);

        //When
        Person result = personService.createPerson(person.getUsername(), person.getPassword());

        //Then
        assertThat(result.getUsername()).isEqualTo(person.getUsername());
        assertThat(result.getPassword()).isEqualTo(person.getPassword());
        verify(mockPersonRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Throw exception on trying to create duplicate person")
    void GivenADuplicateUsername_WhenCreatingPerson_ThenThrowException() {
        //Given
        when(mockPersonRepository.findByUsername(anyString()))
                .thenReturn(Optional.of(person));

        //When / Then
        assertThatThrownBy(() -> personService.createPerson(person.getUsername(), person.getPassword())).isInstanceOf(PersonAlreadyExistEception.class);
        verify(mockPersonRepository, times(0)).save(any());
    }
}