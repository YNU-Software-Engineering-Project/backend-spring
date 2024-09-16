package sg.backend.common;

public interface ResponseCode {

    // HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String VALUDATION_FAILED = "VF";
    String DUPLICATE_EMAIL = "DE";
    String DUPLICATE_NICKNAME = "DN";
    String NOT_EXISTED_USER = "NU";
    String NOT_EXISTED_BOARD = "NB";
    String EMAIL_TOKEN_NOT_FOUND = "ETNF";
    String INVALID_UNIVERSITY_EMAIL = "IUE";

    // HTTP Status 401
    String SIGN_IN_FAIL = "SF";
    String AUTHORIZATION_FAIL = "AF";

    // HTTP Status 403
    String NO_PERMISSTION = "NP";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";

}
