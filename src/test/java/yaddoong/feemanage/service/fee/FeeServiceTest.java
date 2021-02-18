package yaddoong.feemanage.service.fee;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import yaddoong.feemanage.domain.fee.FeeLogRepository;

import java.io.*;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeeServiceTest {

    @Autowired
    FeeService feeService;

    @Autowired
    FeeLogRepository feeLogRepository;
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
            copyFilePath = "\\" + testExcelFileName;
            tmpPath += "\\tmp\\";
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

    @Test
    public void 파일_옮기기() {

        String originalFilePath = "C:\\tmp\\test\\2021년1월23일.xlsx";
        String tmpPath = System.getProperty("user.dir") + "\\tmp";
        String copyFilePath = tmpPath + "\\2021년1월23일.xlsx";

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

    @Test
    public void 디렉토리생성_파일이동및등록() {

        if (!new File(tmpPath).exists()) {
            try {
                new File(tmpPath).mkdir();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }

        boolean exists = new File(tmpPath).exists();
        // 디렉토리가 생성 됐는지
        assertThat(exists).isTrue();

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

        File dir = new File(tmpPath);
        File[] files = dir.listFiles();

        // 파일이 정상적으로 옮겨 졌는지 확인
        assertThat(files.length).isEqualTo(1);



    }

}