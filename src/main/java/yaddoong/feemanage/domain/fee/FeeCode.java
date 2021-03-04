package yaddoong.feemanage.domain.fee;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FeeCode extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "code")
    private List<FeeDetailGubun> details = new ArrayList<>();

    public FeeCode(String name) {
        this.name = name;
    }
}
