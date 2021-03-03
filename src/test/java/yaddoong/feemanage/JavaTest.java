package yaddoong.feemanage;

import org.apache.tomcat.jni.Local;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import javax.jdo.listener.LoadLifecycleListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

@PropertySource("classpath:application-testparam.properties")
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

    @Test
    public void 회비기준테스트() throws Exception {

        LocalDate today = LocalDate.now();
//        LocalDate today = LocalDate.parse("2021-03-23");
        LocalDate startDay = LocalDate.parse("2021-01-23");
        int todayMonth = today.getMonthValue();
        int startMonth = startDay.getMonthValue()+1;
        int todayDay = today.getDayOfMonth();
        int todayYear = today.getYear();
        int startYear = startDay.getYear();

        int cnt = todayDay >= 15 ? 1 : 0;
        if (todayYear == startYear) {
            cnt += todayMonth - startMonth;
        }
        System.out.println("cnt = " + cnt);


        // (카운터 * 15000원) + 해당사용자 현재미납액 - 섬(해당사용자 입금금액) = 미납금액
//        @Query(
//                value = "SELECT * FROM Users u WHERE u.status = ?1",
//                nativeQuery = true)



        
        //when
        
        //then
    }

    @Test
    public void 문자열자르기() {
        String etc = "경기장,음료(110000,12450)";
        System.out.println(etc.substring(etc.indexOf("(")));
        System.out.println(etc.substring(0,etc.indexOf("(")));
    }
        

}
