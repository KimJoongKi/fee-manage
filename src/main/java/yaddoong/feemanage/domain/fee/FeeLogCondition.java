package yaddoong.feemanage.domain.fee;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class FeeLogCondition {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String contents;

}
