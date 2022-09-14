package nl.quintor.abn.recipe.exception;

public class InstructionNotFoundException extends RuntimeException {

    public InstructionNotFoundException(long instructionId) {
        super("Instruction with id " + instructionId + " does not exist");
    }
}
