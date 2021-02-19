package yaddoong.feemanage.service.fee;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yaddoong.feemanage.domain.fee.FeeFileLog;
import yaddoong.feemanage.domain.fee.FeeFileLogRepository;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.web.dto.FeeLogDto;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FeeServiceTest {

    @Autowired
    FeeService feeService;

    @Autowired
    FeeLogRepository feeLogRepository;
    @Autowired
    FeeFileLogRepository feeFileLogRepository;


    static String osName = System.getProperty("os.name").toUpperCase();
    static String testExcelFileName = "2019년12월11일.xlsx";
    static String originalFilePath = "/Users/yaddoong/study/fee/test";
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
    public void 디렉토리생성_파일이동및등록() throws IOException, ParseException {

        디렉토리확인및생성();

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
                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                feeLogDto.setDate(format.parse(cell
                        .getStringCellValue()));
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