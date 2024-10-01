package sg.backend.common;

public interface ResponseCode {

    // HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String VALIDATION_FAILED = "VF";
    String DUPLICATE_EMAIL = "DE";
    String DUPLICATE_NICKNAME = "DN";
    String NOT_EXISTED_USER = "NU";

    String NOT_EXISTED_BOARD = "NB";
    String NOT_EXISTED_FILE = "NF";
    String NOT_EXISTED_DATA = "ND";
    String FULL_DATA = "FD";
    String NOT_EXISTED_FUNDING = "NF";
    String NOT_EXISTED_NOTIFICATION = "NN";
    String PASSWORD_SAME_AS_CURRENT = "PSC";
    String EMAIL_TOKEN_NOT_FOUND = "ETNF";
    String NOT_EXISTED_SCHOOLEMAIL = "NS";

    String NOT_EXISTED_INFO = "NEI";
    String NOT_EXISTED_STORY = "NES";
    String NOT_EXISTED_REWARD = "NER";
    String NOT_EXISTED_POLICY = "NEP";

    // HTTP Status 401
    String SIGN_IN_FAIL = "SF";
    String AUTHORIZATION_FAIL = "AF";

    // HTTP Status 403
    String NO_PERMISSTION = "NP";

    // HTTP Status 415
    String UNSUPPORTED_MEDIA_TYPE = "UM";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";

}
