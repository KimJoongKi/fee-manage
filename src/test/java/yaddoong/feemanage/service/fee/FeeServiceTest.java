package yaddoong.feemanage.service.fee;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import yaddoong.feemanage.domain.fee.*;
import yaddoong.feemanage.domain.user.UserRepository;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.dto.FeeLogDto;
import yaddoong.feemanage.web.form.FeeLogEtcUpdateForm;
import yaddoong.feemanage.web.form.UserFeeForm;

import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.swing.text.html.Option;
import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@PropertySource("classpath:application-testparam.properties")
@SpringBootTest
class FeeServiceTest {

    @Autowired
    FeeService feeService;
    @Autowired
    FeeLogRepository feeLogRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FeeFileLogRepository feeFileLogRepository;
    @Autowired
    FeeLogEtcRepository feeLogEtcRepository;
    @Autowired
    UserService userService;
    @Value("${start.date}")
    String startDateStr;
    @Value("${fee.price}")
    int feePrice;
    @Autowired
    FeeCodeRepository feeCodeRepository;
    @Autowired
    FeeDetailGubunRepository feeDetailGubunRepository;

    static String osName = System.getProperty("os.name").toUpperCase();
    static String testExcelFileName = "2019년12월11일.xlsx";
    static String originalFilePath = System.getProperty("user.home") + "/study/fee/test";
    static String tmpPath = System.getProperty("user.dir") + "/tmp/";
    static String copyFilePath = "";

    @BeforeAll
    public static void 사전작업() {
        copyFilePath = tmpPath + testExcelFileName;
        if(osName.indexOf("WIN") >= 0) {
            originalFilePath = "C:\\tmp\\test";
            copyFilePath = tmpPath + "/" + testExcelFileName;
        }
        originalFilePath += "/" + testExcelFileName;

        File deleteFolder = new File(tmpPath);
        File[] deleteFiles = deleteFolder.listFiles();
        if (deleteFolder.listFiles()!=null) {
            Arrays.stream(deleteFiles)
                    .forEach(file -> file.delete());
        }
    }

    private void 디렉토리생성() {
        if (!new File(tmpPath).exists()) {
            try {
                new File(tmpPath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    private void 테스트엑셀파일복사(String originalFilePath, String copyFilePath) {
        File originalFile = new File(originalFilePath);
        File copyFile = new File(copyFilePath);

        try {
            FileInputStream fis = new FileInputStream(originalFile);
            FileOutputStream fos = new FileOutputStream(copyFile);

            int fileByte = 0;
            while ((fileByte = fis.read()) != -1) {
                fos.write(fileByte);
            }
            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private XSSFSheet 엑셀시트읽기(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        // xlsx 파일 로드
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        return sheet;
    }

    private List<FeeLog> 엑셀파일TODTO(XSSFSheet sheet, List<FeeLog> list) throws ParseException {
        for (int i = 11; i <= sheet.getLastRowNum(); i++) { // 행
            FeeLogDto feeLogDto = new FeeLogDto();
            Row row = sheet.getRow(i);
            list.add(rowValueSet(feeLogDto, row)
                    .toEntity());
        }
        return list;
    }

    @Test
    public void 디렉토리생성_파일이동및등록() throws IOException, ParseException {

        디렉토리생성();

        boolean exists = new File(tmpPath).exists();
        // 디렉토리가 생성 됐는지 확인
        assertThat(exists).isTrue();

        // 테스트파일 경로로 복사
        테스트엑셀파일복사(originalFilePath, copyFilePath);

        // 복사한 경로에 파일이 있는지 확인
        File dir = new File(tmpPath);
        File[] files = dir.listFiles();
        assertThat(files.length).isEqualTo(1);

        for (File file : files) {
            XSSFSheet sheet = 엑셀시트읽기(file);
            List<FeeLog> list = new ArrayList<>();
            list = 엑셀파일TODTO(sheet, list);
            feeLogRepository.saveAll(list);
            String fileName = file.getName();
            feeFileLogRepository.save(FeeFileLog
                    .builder()
                    .name(fileName)
                    .build());
        }

        // 등록은 잘 됐을까요
        List<FeeLog> feeLogs = feeLogRepository.findAll();
        String contents = feeLogs.get(feeLogs.size() - 1)
                .getContents();
        LocalDateTime date = feeLogs.get(feeLogs.size() - 1)
                .getDate();

        assertThat(contents).isEqualTo("박지홍");
        assertThat(date.toString()).isEqualTo("2019-12-09T20:52:02");

        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));
        Page<FeeFileLog> feeFileLogs = feeFileLogRepository.findAll(pageable);
        String name = feeFileLogs.getContent().get(0).getName();
        assertThat(name).isEqualTo(testExcelFileName);

    }

    @Transactional
    @Test
    public void 로그목록조회테스트() throws IOException, ParseException {

        디렉토리생성_파일이동및등록();

        // 테스트용
        String formStartDate = "2018-12-14";
        String formEndDate = "2019-01-14";

        LocalDateTime startDate = LocalDate.parse(formStartDate, DateTimeFormatter.ISO_DATE).atTime(LocalTime.MIN);
        LocalDateTime endDate = LocalDate.parse(formEndDate, DateTimeFormatter.ISO_DATE).atTime(LocalTime.MAX);
        String contents = "%김중기%";

        List<FeeLog> all = feeLogRepository.findFeeLogsByDateBetweenAndContentsLikeOrderByDateAsc(startDate, endDate, contents);
        assertThat(all.get(0).getContents()).isEqualTo("김중기");
        assertThat(all.get(0).getDate()).isEqualTo("2018-12-24T08:09:56");
    }

    @Transactional
    @Test
    public void 회비비고수정() throws Exception {

        디렉토리생성_파일이동및등록();

        //given
        FeeLogEtcUpdateForm form = new FeeLogEtcUpdateForm();
        form.setId(1L);
        form.setMemo("실수");

        Long id = form.getId();
        String memo = form.getMemo();
        feeService.feeLogEtcsSaveAll();

        //when
        Optional<FeeLogEtc> feeLogEtc = feeLogEtcRepository.findById(id);
        LocalDateTime date = feeLogEtc.get().getDate();
        String contents = feeLogEtc.get().getContents();
        Optional<FeeLog> findFeeLog = feeLogRepository.findFeeLogByContentsAndDate(contents, date);
        findFeeLog.get().updateMemo(memo);
        FeeLog save = feeLogRepository.save(findFeeLog.get());

        //then
        assertThat(feeLogEtc.get().getId()).isEqualTo(1L);
        assertThat(date).isEqualTo("2018-12-14T17:59:14");
        assertThat(contents).isEqualTo("잔액이체");
        assertThat(save.getMemo()).isEqualTo("실수");
        assertThat(save.getDate()).isEqualTo(findFeeLog.get().getDate());
        assertThat(save.getContents()).isEqualTo(findFeeLog.get().getContents());

    }

    @Transactional
    @Test
    public void 기타내역적용() throws Exception {

        // 테스트 데이터 and 기타내역 입력
        디렉토리생성_파일이동및등록();
        feeService.feeLogEtcsSaveAll();

        // 기타내역 수정 데이터 세팅
        Long id = 1L;
        String memo = "김중기얼짱";

        // 기타내역 수정
        Optional<FeeLogEtc> feeLogEtc = feeLogEtcRepository.findById(id);
        feeLogEtc.get().updateMemo(memo);
        feeService.feeLogAndEtcSave(feeLogEtc.get());

        // 기타내역 모두 삭제
        feeLogEtcRepository.deleteAll();

        // 기타내역 삭제 테스트
        List<FeeLogEtc> findFeeLogEtcs = feeLogEtcRepository.findAll();
        int findFeeLogEtcsSize = findFeeLogEtcs.size();
        assertThat(findFeeLogEtcsSize).isEqualTo(0);

        // 기타내역 재 입력
        feeService.feeLogEtcsSaveAll();

        // 기타내역 재조회
        Optional<FeeLogEtc> reFeeLogEtc = feeLogEtcRepository.findById(50L);
        assertThat(reFeeLogEtc.get().getMemo()).isNotEqualTo("김중기얼짱");

    }

    @Transactional
    @Test
    public void 유저가아닌데이터인서트() throws Exception {

        //given
        디렉토리생성_파일이동및등록();

        //when
        List<String> usersNames = userRepository.findUsersNames();
        List<FeeLog> findEtcList = feeLogRepository.findFeeLogEtc(usersNames);
        List<FeeLogEtc> feeLogEtcs = new ArrayList<>();

        findEtcList.forEach(etc ->
                {
                    FeeLogEtc feeLogEtc = FeeLogEtc.builder()
                            .date(etc.getDate())
                            .contents(etc.getContents())
                            .division(etc.getDivision())
                            .price(etc.getPrice())
                            .afterBalance(etc.getAfterBalance())
                            .memo(etc.getMemo())
                            .build();
                    feeLogEtcs.add(feeLogEtc);
                }
                );

        feeLogEtcRepository.saveAll(feeLogEtcs);

        //then
        assertThat(usersNames.size()).isEqualTo(23);
        assertThat(findEtcList.get(0).getPrice()).isEqualTo(1070021);
        assertThat(findEtcList.get(0).getContents()).isEqualTo("잔액이체");
        assertThat(findEtcList.get(findEtcList.size()-1).getPrice()).isEqualTo(-79000);
        assertThat(findEtcList.get(findEtcList.size()-1).getContents()).isEqualTo("박지홍");

    }

    /**
     * 한 행에 있는 데이터를 가져온다.
     * @param feeLogDto
     * @param row
     * @return
     * @throws ParseException
     */
    private FeeLogDto rowValueSet(FeeLogDto feeLogDto, Row row) throws ParseException {
        for (int j = 1; j < row.getLastCellNum(); j++) { // 열
            feeLogDto = cellValueSet(feeLogDto, row.getCell(j));
        }
        return feeLogDto;
    }

    /**
     * 한 cell에 있는 데이터를 가져온다.
     * @param cell
     */
    public FeeLogDto cellValueSet(FeeLogDto feeLogDto, Cell cell) throws ParseException {
        switch (cell.getColumnIndex()) { // 셀
            case 1:
                feeLogDto.setDate(
                        LocalDateTime.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")));
                break;
            case 2:
                feeLogDto.setDivision(cell
                        .getStringCellValue());
                break;
            case 3:
                feeLogDto.setPrice(
                        Integer.parseInt(
                                cell.getStringCellValue()
                                        .replace(",","")));
                break;
            case 4:
                feeLogDto.setAfterBalance(Integer
                        .parseInt(cell
                                .getStringCellValue()
                                .replace(",","")));
                break;
            case 6:
                feeLogDto.setContents(cell
                        .getStringCellValue());
                break;
            case 7:
                feeLogDto.setMemo(cell
                        .getStringCellValue());
                break;
        }
        return feeLogDto;
    }

    @Transactional
    @Test
    public void 새로고침기능() throws IOException, ParseException {
        디렉토리생성_파일이동및등록();

        // 메모가 있는 항목 조회
        List<FeeLog> feeLogs = feeLogRepository.findAllByMemoNot("");
        feeLogs.forEach(log -> {
            Optional<FeeCode> code = feeCodeRepository.findAllByName(log.getMemo());
            FeeCode findFeeCode = null;

            if (code.isPresent()) {
                findFeeCode = code.get();
            }

            FeeDetailGubun detailGubun = FeeDetailGubun.builder()
                    .date(log.getDate())
                    .contents(log.getContents())
                    .division(log.getDivision())
                    .price(log.getPrice())
                    .afterBalance(log.getAfterBalance())
                    .memo(log.getMemo())
                    .code(findFeeCode)
                    .build();

            feeDetailGubunRepository.save(detailGubun);
        });

        List<FeeDetailGubun> feeDetailGubuns = feeDetailGubunRepository.findAll();
        assertThat(feeDetailGubuns.size()).isEqualTo(18);

    }

    @Transactional
    @Test
    public void 메모없는이력조회() throws Exception {

        디렉토리생성_파일이동및등록();

        List<FeeLog> feeLogs = feeLogRepository.findAllByMemoNot("");

        assertThat(feeLogs.get(0).getMemo()).isNotEqualTo("");

    }



}