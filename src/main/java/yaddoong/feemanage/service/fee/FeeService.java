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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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


    public void save() throws IOException {

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
                for (int i = 11; i <= sheet.getLastRowNum();i++) {
                    Row row = sheet.getRow(i);
                    System.out.println("행 = " + row.getRowNum());
                    for (int j = 1; j < row.getLastCellNum();j++) {

                        Cell cell = row.getCell(j);
                        if ("".equals(cell.getCellType())) {
                            continue;
                        } else {
                            switch (cell.getColumnIndex()) {
                                case 1:
                            }
                            System.out.println("cell.getStringCellValue() = " + cell.getStringCellValue());
                        }

                    }
                }
                XSSFRow row = sheet.getRow(10);

                System.out.println(row.getCell(1));
            }
        }
    }


}
