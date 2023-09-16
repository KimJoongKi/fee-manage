package yaddoong.feemanage.domain.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import yaddoong.feemanage.domain.base.BaseTimeEntity;
import yaddoong.feemanage.domain.member.Role;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public void updateUnpaid(int unpaid) {
        this.unpaid = unpaid;
    }

}
