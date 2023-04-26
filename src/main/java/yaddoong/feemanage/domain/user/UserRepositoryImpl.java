package yaddoong.feemanage.domain.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import yaddoong.feemanage.domain.fee.FeeLogEtc;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.user.QMember.member;

public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findUsersNames() {

        List<String> results = queryFactory.select(member.name)
                .from(member)
                .fetch();

        return results;

    }


}

