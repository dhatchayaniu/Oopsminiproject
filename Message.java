package util;
import java.io.Serializable;
public class Message implements Serializable {
    private String command;
    private String[] data;
    public Message(String command, String... data) {
        this.command = command;
        this.data = data;
    }
    public String getCommand() { return command; }
    public String[] getData() { return data; }
}

