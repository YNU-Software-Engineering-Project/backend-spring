package sg.backend.common;

public interface ResponseMessage {
    // HTTP Status 200
    String SUCCESS = "Success.";

    // HTTP Status 400
    String VALIDATION_FAILED = "Validation Failed";
    String DUPLICATE_EMAIL = "Duplicate email";
    String DUPLICATE_NICKNAME = "Duplicate nickname";
    String NOT_EXISTED_USER = "This user does not exist";

    String NOT_EXISTED_BOARD = "This board does not exist";
    String NOT_EXISTED_FILE = "Not existed file";
    String NOT_EXISTED_DATA = "Not existed data";
    String FULL_DATA = "Full data";
    String NOT_EXISTED_FUNDING = "This board does not exist.";
    String NOT_EXISTED_NOTIFICATION = "This notification does not exist.";
    String PASSWORD_SAME_AS_CURRENT = "New password cannot be the same as the current password.";
    String EMAIL_TOKEN_NOT_FOUND = "Email token not found.";

    // HTTP Status 401
    String SIGN_IN_FAIL = "Login information mismatch.";
    String AUTHORIZATION_FAIL = "Authorization Failed.";

    // HTTP Status 403
    String NO_PERMISSTION = "Do not have permission.";

    // HTTP Status 415
    String UNSUPPORTED_MEDIA_TYPE = "This is unsupported meta type.";

    // HTTP Status 500
    String DATABASE_ERROR = "Datatbase error.";
}
