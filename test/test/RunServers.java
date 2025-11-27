package test;

public class RunServers {
    public static void main(String[] args) {
        System.out.println("Starting Socket Server (8888)...");
        try {
            MultiThreadServer.main(new String[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}