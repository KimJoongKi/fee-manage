package yaddoong.feemanage.domain.fee;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.*;
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
    private String gubun;

    @OneToMany(mappedBy = "code")
    private List<FeeDetailGubun> details = new ArrayList<>();

    @Builder
    public FeeCode(String name, String gubun) {
        this.name = name;
        this.gubun = gubun;
    }

    public void updateCode(String name, String gubun) {
        this.name = name;
        this.gubun = gubun;
    }
}