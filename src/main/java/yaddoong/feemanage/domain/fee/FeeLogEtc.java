package yaddoong.feemanage.domain.fee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
public class FeeLogEtc {

    @Id
    private Long id;
    private Date date;
    private String contents;
    private String division;
    private int price;
    private int afterBalance;
    private String memo;

}
