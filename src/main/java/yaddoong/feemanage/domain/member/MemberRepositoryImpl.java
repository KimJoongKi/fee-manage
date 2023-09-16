package yaddoong.feemanage.domain.member;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.member.QMember.member;

public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<String> findMembersNames() {
        List<String> results = queryFactory.select(member.name)
                .from(member)
                .fetch();

        return results;
    }
}
