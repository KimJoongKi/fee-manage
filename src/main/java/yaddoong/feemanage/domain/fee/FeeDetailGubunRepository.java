package yaddoong.feemanage.domain.fee;

import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeeDetailGubunRepository extends JpaRepository<FeeDetailGubun, Long>, FeeDetailGubunRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = "code")
    List<FeeDetailGubun> findAll();

    List<FeeDetailGubun> findAllByCodeIsNull();

}
