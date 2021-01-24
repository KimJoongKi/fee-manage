package yaddoong.feemanage.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import yaddoong.feemanage.domain.base.BaseTimeEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
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

}
