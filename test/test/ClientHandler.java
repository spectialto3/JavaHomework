package test;

import java.net.Socket;
import java.util.List;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.sql.SQLException;

// 实现 Runnable 接口，这就代表它是一个可以被线程执行的“任务”
public class ClientHandler implements Runnable {
    private Socket socket; // 这个线程负责的那个连接
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
             ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream())
        ) {
            // 1. 读取 Client 发来的对象
            Ticket t = (Ticket) ois.readObject();
            
            // 2. 显示一下
            System.out.println("【线程 " + Thread.currentThread().getName() + "】收到数据: " + t);
            
            // 3. 调用安全写入方法，保存到文件
            ServerFileHelper.saveRecord("server_all_records.txt", List.of(t));
            try {
                DatabaseHelper.insertTicket(t);
            } catch (Throwable se) {
                se.printStackTrace();
            }
            
            // 4. 回复 Client
            dos.writeUTF("Server已保存您的数据！");
            dos.flush();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (Exception e) {}
        }
    }
}
