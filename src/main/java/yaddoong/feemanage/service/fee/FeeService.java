package yaddoong.feemanage.service.fee;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yaddoong.feemanage.domain.fee.*;
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

    private final FeeLogRepository feeLogRepository;
    private final FeeFileLogRepository feeFileLogRepository;

    /**
     * 회비 내역이 담긴 엑셀파일의 정보를 등록한다.
     * @throws IOException
     * @throws ParseException
     * @param uploadFile
     */
    @Transactional
    public void save(MultipartFile uploadFile) throws IOException, ParseException {

        String filename = uploadFile.getOriginalFilename();
        String tmpPath = System.getProperty("user.dir") + "\\tmp";

        // 디렉토리가 존재하지 않으면 디렉토리 생성
        if (!new File(tmpPath).exists()) {
            try {
                new File(tmpPath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        // 파일업로드 디렉토리 하위 파일을 모두 지운다.
        File deleteFolder = new File(tmpPath);
        File[] deleteFiles = deleteFolder.listFiles();

        for (File deleteFile : deleteFiles) {
            deleteFile.delete();
        }

        // 업로드 디렉토리로 파일 복사
        String filePath = tmpPath + "\\" + filename;
        uploadFile.transferTo(new File(filePath));

        // 파일이 저장된 디렉토리
        File dir = new File(tmpPath);
        // 해당 디렉토리에 있는 파일을 모두 가져온다.
        File[] files = dir.listFiles();

        // TODO: 2021-01-23 업로드 된 파일이 존재하지 않을 때 메시지 추가
        if (files.length == 0) {
            System.out.println("파일이 첨부되지 않았을 때 메시지 추가");
            return;
        }

        uploadFileInsertDb(files);
    }

    private void uploadFileInsertDb(File[] files) throws IOException, ParseException {
        for (File file : files) {
            FileInputStream fis = new FileInputStream(file);
            // xlsx 파일 로드
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            List<FeeLog> list = new ArrayList<>();
            list = listObjectSet(sheet, list);
            feeLogRepository.saveAll(list);
            String fileName = file.getName();
            feeFileLogRepository.save(FeeFileLog
                            .builder()
                            .name(fileName)
                            .build());
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
