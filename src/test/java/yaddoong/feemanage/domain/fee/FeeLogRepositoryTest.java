package yaddoong.feemanage.domain.fee;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import yaddoong.feemanage.web.FeeController;

import javax.persistence.Id;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeeLogRepositoryTest {

    @Autowired
    FeeLogRepository feeLogRepository;

    @Autowired
    FeeCodeRepository feeCodeRepository;

    @Autowired
    FeeDetailGubunRepository feeDetailGubunRepository;

    @Transactional
    @Test
    public void 씨알유디() throws ParseException {

        LocalDateTime date = LocalDateTime.now();

        List<FeeLog> feeLogList = new ArrayList<>();
        feeLogList.add(FeeLog.builder()
                .date(date)
                .contents("홍길동")
                .division("입금")
                .price(15000)
                .afterBalance(165000)
                .memo("")
                .build());
        FeeLog feeLog = feeLogRepository.save(feeLogList.get(0));

        System.out.println("feeLogList = " + feeLogList);
        System.out.println("feeLog = " + feeLog);
        Optional<FeeLog> findFeeLog = feeLogRepository.findFeeLogByContentsAndDate(feeLog.getContents(), feeLog.getDate());
        System.out.println("findFeeLog = " + findFeeLog);
        assertThat(findFeeLog.get().getContents()).isEqualTo(feeLog.getContents());
        assertThat(findFeeLog.get().getDate()).isEqualTo(feeLog.getDate());

        feeLogRepository.delete(feeLog);
        Optional<FeeLog> afterDeleted = feeLogRepository.findFeeLogByContentsAndDate(feeLog.getContents(), feeLog.getDate());
        assertThat(afterDeleted).isEmpty();

    }

    @Transactional
    @Test
    public void 코드입력() throws IOException, ParseException {

//        디렉토리생성_파일이동및등록();
        FeeCode code = new FeeCode("경기장", "출금");
        FeeCode code1 = new FeeCode("음료", "출금");
        feeCodeRepository.save(code);
        feeCodeRepository.save(code1);
    }

    @Test
    public void 코드값입력_내역입력_조회테스트() {

        LocalDateTime localDateTime = LocalDateTime.now();
        FeeCode feeCode = FeeCode.builder()
                .gubun("입금")
                .name("경기장")
                .build();

        feeCodeRepository.save(feeCode);

        FeeDetailGubun feeDetailGubun1 = FeeDetailGubun.builder()
                .date(localDateTime)
                .contents("박룡철1")
                .division("입")
                .price(170000)
                .afterBalance(1170000)
                .memo("경기장")
                .code(feeCode)
                .build();

        FeeDetailGubun feeDetailGubun2 = FeeDetailGubun.builder()
                .date(localDateTime)
                .contents("박룡철")
                .division("입금")
                .price(170000)
                .afterBalance(1170000)
                .memo("회식")
                .code(null)
                .build();

        feeDetailGubunRepository.save(feeDetailGubun1);
        feeDetailGubunRepository.save(feeDetailGubun2);

        List<FeeDetailGubun> feeDetailGubuns = feeDetailGubunRepository.findAllByCodeIsNull();
        assertThat(feeDetailGubuns.get(0).getDate()).isEqualTo(localDateTime);
        assertThat(feeDetailGubuns.get(0).getContents()).isEqualTo("박룡철");

    }
    
    @Test
    public void 코드카운트조회() throws Exception {

        FeeCode feeCode = FeeCode.builder()
                .gubun("입금")
                .name("경기장")
                .build();

        //given
        feeCodeRepository.save(feeCode);
        int count = feeCodeRepository.findCountAllByNameAAndGubun("경기장", "입금");

        //when
        
        //then
        System.out.println("count = " + count);
    }
        
}