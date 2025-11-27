package test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class FileClientBase {
    public static List<Ticket> readTicketsFromFile(String fileName) {
        List<Ticket> tickets = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                if (info.length >= 4) tickets.add(new Ticket(info));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tickets;
    }

    public static void sendTicketsFromFile(String fileName) {
        List<Ticket> tickets = readTicketsFromFile(fileName);
        for (Ticket t : tickets) {
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
    }
}
