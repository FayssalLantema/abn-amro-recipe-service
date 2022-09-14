package nl.quintor.abn.recipe.exception;

public class PersonAlreadyExistEception extends RuntimeException {

    public PersonAlreadyExistEception(String username) {
        super("Person with username " + username + " already exist");
    }
}