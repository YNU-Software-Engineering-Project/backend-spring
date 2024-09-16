package sg.backend.common;

public interface ResponseMessage {
    // HTTP Status 200
    String SUCCESS = "Success.";

    // HTTP Status 400
    String VALUDATION_FAILED = "Validation Failed";
    String DUPLICATE_EMAIL = "Duplicate email";
    String DUPLICATE_NICKNAME = "Duplicate nickname";
    String NOT_EXISTED_USER = "This user does not exist";
    String NOT_EXISTED_BOARD = "This board does not exist.";
    String EMAIL_TOKEN_NOT_FOUND = "Email token not found.";
    String INVALID_UNIVERSITY_EMAIL = "Not a valid university email.";

    // HTTP Status 401
    String SIGN_IN_FAIL = "Login information mismatch.";
    String AUTHORIZATION_FAIL = "Authorization Failed.";

    // HTTP Status 403
    String NO_PERMISSTION = "Do not have permission.";

    // HTTP Status 500
    String DATABASE_ERROR = "Datatbase error.";
}
