package test;

import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TestCommunication {
    private static void send(Ticket t) {
        try (Socket socket = new Socket("localhost", 8888)) {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(t);
            oos.flush();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            String msg = dis.readUTF();
            System.out.println(msg + " -> " + t.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("若未运行服务器，请先运行 RunServers 再继续...");
        Thread a = new Thread(() -> send(new Ticket("张三", "11010119900101001X", "北京", "上海")), "T-A");
        Thread b = new Thread(() -> send(new Ticket("李四", "320101199202021234", "南京", "杭州")), "T-B");
        Thread c = new Thread(() -> send(new Ticket("王五", "440101198807073333", "广州", "深圳")), "T-C");
        Thread d = new Thread(() -> send(new Ticket("赵六", "120101199512126666", "天津", "北京")), "T-D");
        a.start(); b.start(); c.start(); d.start();
        try { a.join(); b.join(); c.join(); d.join(); } catch (InterruptedException ignored) {}
    }
}