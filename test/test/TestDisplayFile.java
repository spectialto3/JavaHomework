package test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDisplayFile {
    private static void header() {
        System.out.printf("%-12s | %-20s | %-12s | %-12s%n", "姓名", "身份证号码", "出发城市", "到达城市");
        System.out.println("--------------------------------------------------------------------");
    }

    public static void showFile() {
        String file = "server_all_records.txt";
        header();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length < 4) continue;
                System.out.printf("%-12s | %-20s | %-12s | %-12s%n", p[0], p[1], p[2], p[3]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showDb() {
        header();
        try (Connection conn = DatabaseHelper.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT name,id_card,start_city,end_city FROM tickets ORDER BY id")) {
            while (rs.next()) {
                System.out.printf("%-12s | %-20s | %-12s | %-12s%n",
                        rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        showFile();
        showDb();
    }
}