package yaddoong.feemanage.domain.fee;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.Optional;

import static yaddoong.feemanage.domain.fee.QFeeCode.feeCode;

public class FeeCodeRepositoryImpl implements FeeCodeRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FeeCodeRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    @Override
    public Optional<FeeCode> findFeeCodeMemoAndDivision(String memo, String division) {
        Optional<FeeCode> feeCodeOptional = Optional.ofNullable(queryFactory
                .selectFrom(feeCode)
                .where(feeCode.name.eq(memo).and(feeCode.gubun.eq(division)))
                .fetchOne());
        return feeCodeOptional;
    }
}
