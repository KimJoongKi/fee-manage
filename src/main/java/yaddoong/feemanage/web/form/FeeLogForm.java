package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class FeeLogForm {

    private String startDate;
    private String endDate;
    private String contents;

}
