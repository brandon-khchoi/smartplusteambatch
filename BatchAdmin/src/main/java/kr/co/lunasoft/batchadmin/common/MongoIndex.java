package kr.co.lunasoft.batchadmin.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MongoIndex {
    public static String getIndexDate() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM"));
    }
}
