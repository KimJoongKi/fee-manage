package yaddoong.feemanage.service.fee;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import yaddoong.feemanage.domain.fee.FeeLog;
import yaddoong.feemanage.domain.fee.FeeLogRepository;
import yaddoong.feemanage.web.dto.FeeLogSaveDto;

import javax.persistence.Id;
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


    public void save() throws IOException, ParseException {

        // 파일이 저장된 디렉토리
        File dir = new File(path);
        // 해당 디렉토리에 있는 파일을 모두 가져온다.
        File[] files = dir.listFiles();

        if (files.length == 0) {
            System.out.println(oneFileMessage);
        } else {
            for (File file : files) {
                FileInputStream fis = new FileInputStream(file);
                // xlsx 파일 로드
                XSSFWorkbook wb = new XSSFWorkbook(fis);
                XSSFSheet sheet = wb.getSheetAt(0);
                List<FeeLog> list = new ArrayList<>();
                for (int i = 11; i <= sheet.getLastRowNum();i++) {
                    Row row = sheet.getRow(i);
                    Date dealDate = null;
                    String dealContents = null;
                    String division = null;
                    int dealPrice = 0;
                    int dealAfterBalance = 0;
                    String memo = null;
                    for (int j = 1; j < row.getLastCellNum();j++) {
                        Cell cell = row.getCell(j);
                        switch (cell.getColumnIndex()) {
                            case 1:
                                SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                                dealDate = format.parse(cell.getStringCellValue());
                                break;
                            case 2:
                                division = cell.getStringCellValue();
                                break;
                            case 3:
                                dealPrice = Integer.parseInt(cell.getStringCellValue().replace(",",""));
                                break;
                            case 4:
                                dealAfterBalance = Integer.parseInt(cell.getStringCellValue().replace(",",""));
                                break;
                            case 6:
                                dealContents = cell.getStringCellValue();
                                break;
                            case 7:
                                memo = cell.getStringCellValue();
                                break;
                        }
                    }
                    list.add(FeeLogSaveDto.builder()
                            .dealDate(dealDate)
                            .dealContents(dealContents)
                            .division(division)
                            .dealPrice(dealPrice)
                            .dealAfterBalance(dealAfterBalance)
                            .memo(memo)
                            .build().toEntity());
                }
                feeLogRepository.saveAll(list);
            }
        }
    }


}
