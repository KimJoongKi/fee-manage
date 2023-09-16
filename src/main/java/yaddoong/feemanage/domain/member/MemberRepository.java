package yaddoong.feemanage.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findAllBySecessionDateIsNotNull();

}
