package yaddoong.feemanage.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<Member, Long>, UserRepositoryCustom {

    List<Member> findAllBySecessionDateIsNull();

    List<Member> findAllBySecessionDateIsNotNull();


}
