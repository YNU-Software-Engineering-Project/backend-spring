package sg.backend.exception;

import sg.backend.common.ResponseCode;
import sg.backend.common.ResponseMessage;

public class UnauthorizedAccessException extends CustomException {
    public UnauthorizedAccessException() {
        super(ResponseCode.NO_PERMISSTION, ResponseMessage.NO_PERMISSTION);
    }
}
