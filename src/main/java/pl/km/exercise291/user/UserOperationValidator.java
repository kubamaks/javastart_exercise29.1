package pl.km.exercise291.user;

public class UserOperationValidator {
    private boolean isValidated;
    private String message;

    public boolean isValidated() {
        return isValidated;
    }

    public void setValidated(boolean validated) {
        isValidated = validated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
