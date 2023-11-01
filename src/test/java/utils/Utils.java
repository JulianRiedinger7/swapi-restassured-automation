package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Utils {
  public static boolean isValidDate(String date) {
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setLenient(false);

    try {
      formatter.parse(date);
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
