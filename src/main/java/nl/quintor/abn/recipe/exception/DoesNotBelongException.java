package nl.quintor.abn.recipe.exception;

public class DoesNotBelongException extends RuntimeException {

    public DoesNotBelongException(String message) {
        super(message);
    }
}
