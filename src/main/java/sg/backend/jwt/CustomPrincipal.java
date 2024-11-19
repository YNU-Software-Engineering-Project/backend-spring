package sg.backend.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomPrincipal {
    private final String email;
    private final String provider;
}
