package helper;

import models.App;
import play.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by a18996 on 12/13/16.
 */
public class CSVHelper {
    public static final String FILE = "apps.csv";

    public static List<App> read() {
        List<App> apps = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE));
            for(String line : lines) {
                String[] tokens = line.split(",");
                App a = new App(tokens[0], tokens[1]);
                apps.add(a);
            }
        } catch (NoSuchFileException e) {
            Logger.info("csv file doesn't exist yet");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return apps;
    }

    public static String getDeepLink(String id) {
        List<String> lines;

        try {
            lines = Files.readAllLines(Paths.get(FILE));

            for(String line : lines) {
                String[] tokens = line.split(",");
                if(tokens[0].equals(id))
                    return tokens[1];
            }
        } catch (NoSuchFileException e) {
            Logger.info("csv file doesn't exist yet");
        } catch (IOException e) {
            e.printStackTrace();
        }

        Logger.info("No record found for id: " + id);
        return "";
    }

    public static void writeCsv(App app) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILE, true);
            fw.append(app.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fw != null)
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
