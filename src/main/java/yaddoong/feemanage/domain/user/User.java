package yaddoong.feemanage.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yaddoong.feemanage.domain.base.BaseTimeEntity;
import yaddoong.feemanage.domain.fee.FeeLog;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private LocalDate birth;
    private char sex;
    private LocalDate joinDate;
    private LocalDate secessionDate;
    private int unpaid;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public void updateSecessionDate(String secessionDate) {
        if (secessionDate == null) {
            this.secessionDate = null;
            return;
        }
        this.secessionDate = LocalDate.parse(secessionDate);
    }

    public void updateJoinDate(String rejoinDate) {
        this.joinDate = LocalDate.parse(rejoinDate);
    }

}
