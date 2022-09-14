package nl.quintor.abn.recipe.service;

import nl.quintor.abn.recipe.exception.PersonAlreadyExistEception;
import nl.quintor.abn.recipe.exception.PersonNotFoundException;
import nl.quintor.abn.recipe.model.Person;
import nl.quintor.abn.recipe.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private static final Logger LOG = LoggerFactory.getLogger(PersonService.class);

    private final PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Method which gets a person by ID
     *
     * @param personId the ID of the wanted person
     * @return the found person by ID
     */
    public Person getById(long personId) {
        LOG.info("Finding person with id " + personId);

        Optional<Person> person = personRepository.findById(personId);

        if (person.isPresent())
            return person.get();
        else {
            LOG.error("Person with id " + personId + " has not been found");
            throw new PersonNotFoundException(personId);
        }
    }

    /**
     * Method which creates a new person
     *
     * @param username the given username
     * @param password the given password
     * @return the created person object
     */
    public Person createPerson(String username, String password) {
        if (!doesPersonExist(username)) {
            LOG.info("Creating a person with the username " + username);
            return personRepository.save(new Person(username, password));
        } else {
            LOG.error("The user " + username + " already exists");
            throw new PersonAlreadyExistEception(username);
        }
    }

    /**
     * Private method that checks if the user exists by username
     *
     * @param username the username of the person
     * @return a boolean if the person exist
     */
    private boolean doesPersonExist(String username) {
        LOG.info("Checking if user with usename " + username + " exist");
        return personRepository.findByUsername(username).isPresent();
    }
}
