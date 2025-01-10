package net.wensc.serverPassword.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogWriter {
    public static String newline = new String(System.lineSeparator().getBytes());

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void writeLog(String entityName, String message) {
        File file = new File("player-password.log");
        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append("[").append(simpleDateFormat.format((new Date()).getTime())).append("] [").append(entityName).append("]").append(message).append(newline);
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDimName(int dim) {
        return (dim == 0) ? "主世界" : ((dim == -2) ? "地下世界" : ((dim == -1) ? "地狱" : ((dim == 1) ? "末地" : "未知世界")));
    }
}
