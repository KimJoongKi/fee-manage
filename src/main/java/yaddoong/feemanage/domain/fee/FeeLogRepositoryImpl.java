package yaddoong.feemanage.domain.fee;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import yaddoong.feemanage.domain.user.QUser;
import yaddoong.feemanage.web.dto.QUserFeeStatusDto;
import yaddoong.feemanage.web.dto.UserFeeStatusDto;

import javax.persistence.EntityManager;
import java.util.List;

import static yaddoong.feemanage.domain.fee.QFeeLog.feeLog;
import static yaddoong.feemanage.domain.user.QUser.user;

public class FeeLogRepositoryImpl implements FeeLogRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public FeeLogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<UserFeeStatusDto> findGroupByName() {
        //    @Query(value = "select u.name, COALESCE(sum(f.price),0) AS price, u.unpaid from user as u left outer join fee_log as f on u.name = f.contents " +
//            "where u.secession_date is null and (f.memo = '' or f.memo is null) group by u.name order by u.name", nativeQuery = true)
        List<UserFeeStatusDto> results = queryFactory
                .select(new QUserFeeStatusDto(
                        user.name,
                        feeLog.price.sum().coalesce(0).as("price"),
                        user.unpaid.as("unpaid")
                ))
                .from(user)
                .leftJoin(feeLog, feeLog)
                .on(user.name.eq(feeLog.contents))
                .where(
                        user.secessionDate.isNull(),
                        (feeLog.memo.eq("").or(feeLog.memo.isNull()))
                )
                .groupBy(user.name)
                .fetch();


        return results;
    }
}
