package yaddoong.feemanage;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class JavaTest {

    @Test
    public void 운영체제_확인() {
        String property = System.getProperty("os.name").toUpperCase();
        System.out.println("property = " + property);

        if(property.indexOf("WIN") >= 0) {
            System.out.println("윈도우");
            return;
        }
        System.out.println("노윈도우");
    }

    @Test
    public void 맥경로() throws Exception {
        System.out.println(System.getProperty("user.home"));
    }


    @Test
    public void LocalDateTime() throws Exception {
        LocalDateTime parse = LocalDateTime.parse("2019-01-14 23:59:59", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("parse = " + parse);

        LocalDate startDate = LocalDate.now().minusMonths(1);
        LocalDate endDate = LocalDate.now();
        LocalDateTime startDateTime = LocalDate.now().minusMonths(1).atTime(LocalTime.MIN);
        LocalDateTime endDateTime = LocalDate.now().atTime(LocalTime.MAX);
        LocalDate localDate = startDateTime.toLocalDate();
        System.out.println("localDate = " + localDate);


        String format = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
        System.out.println("format = " + format);


        System.out.println("startDate = " + startDate.toString());
        System.out.println("endDate = " + endDate.toString());



        //when

        //then
    }

}
