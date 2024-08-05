package navarrovmn.github.io.runners.run;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

public record Run (
        @Id
        int id,
        @NotEmpty
        String location,
        LocalDateTime startedOn,
        LocalDateTime completedOn,
        @Version
        Integer version
){
    public Run {
        if(!completedOn.isAfter(startedOn)) {
            throw new IllegalArgumentException("Completed On must be after Started On");
        }
    }
}
