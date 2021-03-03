package yaddoong.feemanage.domain.fee;

import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


@Getter
@NoArgsConstructor
@Entity
public class FeeCode extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
}
