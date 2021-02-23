package yaddoong.feemanage.domain.fee;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@NoArgsConstructor
public class FeeLogId implements Serializable {

    @Id
    private LocalDateTime date;
    @Id
    private String contents;

}
