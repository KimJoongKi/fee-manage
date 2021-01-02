package yaddoong.feemanage.domain.fee;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@IdClass(FeeLogId.class)
@Getter
@NoArgsConstructor
@Entity
public class FeeLog {

    @Id
    private Date dealDate;
    @Id
    private String dealContents;
    private String division;
    private int dealPrice;
    private int dealAfterBalance;
    private String memo;


}
