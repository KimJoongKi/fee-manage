package yaddoong.feemanage.service.fee;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import yaddoong.feemanage.domain.fee.*;
import yaddoong.feemanage.domain.user.UserRepository;
import yaddoong.feemanage.service.user.UserService;
import yaddoong.feemanage.web.dto.FeeLogDto;
import yaddoong.feemanage.web.form.FeeLogEtcUpdateForm;
import yaddoong.feemanage.web.form.UserFeeForm;

import javax.persistence.Id;
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

    static String osName = System.getProperty("os.name").toUpperCase();
    static String testExcelFileName = "2019년12월11일.xlsx";
    static String originalFilePath = System.getProperty("user.home") + "/study/fee/test";
    static String tmpPath = System.getProperty("user.dir") + "/tmp/";
    static String copyFilePath = "";

    @BeforeAll
    public static void 사전작업() {
        System.out.println("property = " + osName);
        copyFilePath = tmpPath + testExcelFileName;
        System.out.println("copyFilePath = " + copyFilePath);
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

    @Test
    public void 서비스호출() {
    }

//    @Test
//    public void 파일_옮기기() {
//        String originalFilePath = "C:\\tmp\\test\\2021년1월23일.xlsx";
//        String tmpPath = System.getProperty("user.dir") + "\\tmp";
//        String copyFilePath = tmpPath + "\\2021년1월23일.xlsx";
//
//        파일(originalFilePath, copyFilePath);
//    }

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
    
    @Test
    public void 회비비고수정() throws Exception {
        디렉토리생성_파일이동및등록();
        //given
        FeeLogEtcUpdateForm form = new FeeLogEtcUpdateForm();
        form.setId(1L);
        form.setMemo("실수");

        Long id = form.getId();
        String memo = form.getMemo();
        List<String> userNames = userService.findUserNames();
        List<FeeLog> findEtcLogs = feeService.findFeeLogEtc(userNames);
        List<FeeLogEtc> feeLogEtcs = feeService.putFeeLogEtc(findEtcLogs);
        feeLogEtcRepository.saveAll(feeLogEtcs);

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
        

    @Test
    public void 회비목록조회테스트() throws Exception {

        //given
        디렉토리생성_파일이동및등록();
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "name"));

        //when
        Page<FeeFileLog> all = feeFileLogRepository.findAll(pageable);
        String lastUpdateData = all.getContent()
                .get(0)
                .getName()
                .replaceAll(".xlsx","") + " 기준";
        if (all.getContent().size() == 0) {
            lastUpdateData = "업데이트 기록 없음";
        }

        //미납금 + (오늘 날짜 - 오늘 날짜가 15보다 적으면 저번 달 15일 or 이번달 15일)*
        List<FeeLogProjection> list = feeLogRepository.findGroupByName();
        List<UserFeeForm> feeFormList = new ArrayList<>();
        for (FeeLogProjection feeLogProjection : list) {
            UserFeeForm form = new UserFeeForm();
            form.setName(feeLogProjection.getName());
            form.setPrice(feeLogProjection.getPrice());
            form.setUnpaid(feeLogProjection.getUnpaid());
            form.toString();
            feeFormList.add(form);
        }

        //then
        assertThat(lastUpdateData).isEqualTo("2019년12월11일 기준");
        assertThat(feeFormList.get(feeFormList.size() - 1).getName()).isEqualTo("한성용");
        assertThat(feeFormList.get(feeFormList.size() - 1).getUnpaid()).isEqualTo(75000);
    }
    
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


    @Test
    public void 디렉토리생성_파일이동및등록() throws IOException, ParseException {

        디렉토리확인및생성();

        System.out.println("FeeServiceTest.디렉토리생성_파일이동및등록");

        boolean exists = new File(tmpPath).exists();
        // 디렉토리가 생성 됐는지
        assertThat(exists).isTrue();

        테스트엑셀파일복사(originalFilePath, copyFilePath);

        // 복사한 경로에 파일 확인
        File dir = new File(tmpPath);
        File[] files = dir.listFiles();
        assertThat(files.length).isEqualTo(1);

        for (File file : files) {
            XSSFSheet sheet = 엑셀시트읽기(file);
            List<FeeLog> list = new ArrayList<>();
            list = excelToDto(sheet, list);
            feeLogRepository.saveAll(list);
            String fileName = file.getName();
            feeFileLogRepository.save(FeeFileLog
                    .builder()
                    .name(fileName)
                    .build());
        }

        // 등록은 잘 됐을까요
        List<FeeLog> all = feeLogRepository.findAll();
        String contents = all.get(all.size() - 1)
                .getContents();
        LocalDateTime date = all.get(all.size() - 1)
                .getDate();

        assertThat(contents).isEqualTo("박지홍");
        assertThat(date.toString()).isEqualTo("2019-12-09T20:52:02");


        Optional<FeeFileLog> findFileLog = feeFileLogRepository.findById(1L);
        String name = findFileLog.get().getName();
        assertThat(name).isEqualTo(testExcelFileName);

    }

    private List<FeeLog> excelToDto(XSSFSheet sheet, List<FeeLog> list) throws ParseException {
        for (int i = 11; i <= sheet.getLastRowNum(); i++) { // 행
            FeeLogDto feeLogDto = new FeeLogDto();
            Row row = sheet.getRow(i);
            list.add(rowValueSet(feeLogDto, row)
                    .toEntity());
        }
        return list;
    }

    private XSSFSheet 엑셀시트읽기(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        // xlsx 파일 로드
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);
        return sheet;
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

    private void 디렉토리확인및생성() {
        if (!new File(tmpPath).exists()) {
            try {
                new File(tmpPath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
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

}