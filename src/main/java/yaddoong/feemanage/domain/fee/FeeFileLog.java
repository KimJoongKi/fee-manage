package yaddoong.feemanage.domain.fee;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class FeeFileLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Builder
    public FeeFileLog(String name) {
        this.name = name;
    }
}
