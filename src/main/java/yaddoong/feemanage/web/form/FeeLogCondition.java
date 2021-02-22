package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class FeeLogCondition {

    private Date startDate;
    private Date endDate;
    private String contents;

}
