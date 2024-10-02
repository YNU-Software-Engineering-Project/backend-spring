package sg.backend.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final String code;
    private final String message;
}
