package client;
import java.io.*;
import java.net.*;
public class VehicleClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    public VehicleClient(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
    public String sendRequest(String message) throws IOException {
        out.println(message);
        return in.readLine();
    }
    public void close() throws IOException {
        socket.close();
    }
}
