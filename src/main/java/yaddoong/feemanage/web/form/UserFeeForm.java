package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFeeForm {
    private String name;
    private int price;
    private int unpaid;
}
