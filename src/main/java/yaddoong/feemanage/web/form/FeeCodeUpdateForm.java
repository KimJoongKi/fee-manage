package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeeCodeUpdateForm {
    private Long id;
    private Long codeId;
    private String name;
    private String insertFlag;
}
