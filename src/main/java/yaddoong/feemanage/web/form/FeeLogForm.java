package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class FeeLogForm {

    private String startDate;
    private String endDate;
    private String contents;

    public FeeLogForm(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        // LocalDateTime to String
        this.startDate = startDateTime.toLocalDate().format(DateTimeFormatter.ISO_DATE);
        this.endDate = endDateTime.toLocalDate().format(DateTimeFormatter.ISO_DATE);
    }
}
