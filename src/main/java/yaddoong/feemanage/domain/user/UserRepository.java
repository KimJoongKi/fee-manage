package yaddoong.feemanage.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import yaddoong.feemanage.domain.fee.FeeLog;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    List<User> findAllBySecessionDateIsNull();

    List<User> findAllBySecessionDateIsNotNull();



}
