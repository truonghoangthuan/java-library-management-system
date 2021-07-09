package settings;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Preferences {
    public static final String CONFIG_FILE = "config.txt";

    int numberOfDaysWithoutFine;
    float finePerDay;
    String username;
    String password;

    public Preferences() {
        numberOfDaysWithoutFine = 14;
        finePerDay = 2;
        username = "admin";
        password = "admin";
    }

    public void setNumberOfDaysWithoutFine(int numberOfDaysWithoutFine) {
        this.numberOfDaysWithoutFine = numberOfDaysWithoutFine;
    }

    public void setFinePerDay(float finePerDay) {
        this.finePerDay = finePerDay;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumberOfDaysWithoutFine() {
        return numberOfDaysWithoutFine;
    }

    public float getFinePerDay() {
        return finePerDay;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public static void initConfig() {
        Writer writer = null;
        try {
            Preferences preferences = new Preferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences, writer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
