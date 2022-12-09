package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeeCodeRepository extends JpaRepository<FeeCode, Long>, FeeCodeRepositoryCustom {
    Optional<FeeCode> findAllByName(String name);

    Optional<FeeCode> findAllByNameAndInsertFlag(String name, String insertFlag);

    @Query("select count(f) from FeeCode as f where f.name = :name and f.insertFlag = :insertFlag")
    int findCountAllByNameAAndInsertFlag(@Param("name") String name,@Param("insertFlag") String insertFlag);
}
