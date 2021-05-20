package kr.co.lunasoft.batchadmin.common;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class MongoIndex {
    public String getIndexDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM"));
    }
}
