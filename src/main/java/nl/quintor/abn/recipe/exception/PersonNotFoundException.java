package nl.quintor.abn.recipe.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(long personId) {
        super("Person with id " + personId + " is not found");
    }
}
