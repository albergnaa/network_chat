package client;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class History {
    private static PrintWriter out;

    private static String createFile(String username) {
        return "history/history_" + username + ".txt";
    }

    public static void start(String login){
        try {
            out = new PrintWriter(new FileWriter(createFile(login), true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        if (out != null){
            out.close();
        }
    }

    public static void writeLine(String message) {
        out.println(message);
    }

    public static String getLastMessages(String username) {
        if(!Files.exists(Paths.get(createFile(username)))){
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        try {
            List<String> historyLines = Files.readAllLines(Paths.get(createFile(username)));
            int startPos = 0;
            if (historyLines.size() > 100) {
                startPos = historyLines.size() - 100;
            }
            for (int i = startPos; i < historyLines.size(); i++) {
                stringBuilder.append(historyLines.get(i)).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
