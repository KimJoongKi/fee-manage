package yaddoong.feemanage.web.form;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserForm {
    private Long id;
    private String secessionDate;
    private String rejoinDate;

    @Override
    public String toString() {
        return "UserForm{" +
                "id=" + id +
                ", secessionDate='" + secessionDate + '\'' +
                '}';
    }
}
