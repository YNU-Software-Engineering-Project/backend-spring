package sg.backend.dto.object;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDataDto {
    private String message;
    private LocalDateTime createdAt;
}
