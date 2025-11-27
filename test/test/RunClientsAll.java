package test;

public class RunClientsAll {
    public static void main(String[] args) {
        System.out.println("若未运行服务器，请先运行 RunServers 再继续...");

        String[] files = {"client1_tickets.txt","client2_tickets.txt","client3_tickets.txt","client4_tickets.txt"};
        for (String f : files) {
            java.io.File file = new java.io.File(f);
            if (!file.exists()) {
                System.out.println("缺少数据文件: " + f);
                return;
            }
        }

        Thread t1 = new Thread(() -> FileClientBase.sendTicketsFromFile("client1_tickets.txt"), "client1");
        Thread t2 = new Thread(() -> FileClientBase.sendTicketsFromFile("client2_tickets.txt"), "client2");
        Thread t3 = new Thread(() -> FileClientBase.sendTicketsFromFile("client3_tickets.txt"), "client3");
        Thread t4 = new Thread(() -> FileClientBase.sendTicketsFromFile("client4_tickets.txt"), "client4");
        t1.start(); t2.start(); t3.start(); t4.start();
        try { t1.join(); t2.join(); t3.join(); t4.join(); } catch (InterruptedException ignored) {}
    }
}