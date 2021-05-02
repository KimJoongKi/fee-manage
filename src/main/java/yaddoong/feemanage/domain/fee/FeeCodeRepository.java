package yaddoong.feemanage.domain.fee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeeCodeRepository extends JpaRepository<FeeCode, Long> {
    Optional<FeeCode> findAllByName(String name);

    Optional<FeeCode> findAllByNameAndGubun(String name, String gubun);

    @Query("select count(f) from FeeCode as f where f.name = :name and f.gubun = :gubun")
    int findCountAllByNameAAndGubun(@Param("name") String name,@Param("gubun") String gubun);
}
