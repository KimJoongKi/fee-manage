package yaddoong.feemanage.service.fee;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.web.dto.FeeLogDto;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PropertySource("classpath:application-param.properties")
@Service
@RequiredArgsConstructor
public class FeeService {

    @Value("${tmp.path}")
    private String path;
    @Value("sheet.name")
    private String sheetName;
    @Value("message.onefile")
    private String oneFileMessage;
    private final FeeLogRepository feeLogRepository;

    /**
     * 회비 내역이 담긴 엑셀파일의 정보를 등록한다.
     * @throws IOException
     * @throws ParseException
     */
    public void save() throws IOException, ParseException {

        // 파일이 저장된 디렉토리
        File dir = new File(path);
        // 해당 디렉토리에 있는 파일을 모두 가져온다.
        File[] files = dir.listFiles();

        // TODO: 2021-01-23 파일을 웹에서 등록하는 기능으로 변경
        if (files.length == 0) {
            System.out.println("파일이 첨부되지 않았을 때 메시지 추가");
            return;
        }

        for (File file : files) { // 파일
            FileInputStream fis = new FileInputStream(file);
            // xlsx 파일 로드
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            List<FeeLog> list = new ArrayList<>();
            feeLogRepository.saveAll(listObjectSet(sheet, list));

        }
    }

    /**
     * 파일에 있는 모든 행을 조회한다.
     * @param sheet
     * @param list
     * @throws ParseException
     */
    private List<FeeLog> listObjectSet(XSSFSheet sheet, List<FeeLog> list) throws ParseException {
        for (int i = 11; i <= sheet.getLastRowNum(); i++) { // 행
            FeeLogDto feeLogDto = new FeeLogDto();
            Row row = sheet.getRow(i);
            list.add(rowValueSet(feeLogDto, row)
                    .toEntity());
        }
        return list;
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
