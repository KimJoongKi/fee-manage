package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class FeeLogEtcUpdateForm {
    private Long id;
    private String memo;
}
