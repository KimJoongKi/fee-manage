package yaddoong.feemanage.service.fee;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FeeServiceTest {

    @Autowired
    FeeService feeService;

//    @BeforeAll
    public void 사전작업() {

        String property = System.getProperty("os.name");

        System.out.println("property = " + property);

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

}