package utils;

import java.io.IOException;

public class DetectErrors {
    ComUtils comUtils;

    public DetectErrors(ComUtils com) {
        this.comUtils = com;
    }

    public int rightInstruction(String command) {
        int id = -3;
        try {

            String s = comUtils.read_command();
            String space = comUtils.readChar();
            id = comUtils.read_int32();
            if (command != "CASH") {
                if (!s.equals(command))
                    return -1;
            }
            if (!space.equals(" "))
                return -2;
            if (id < 0)
                return -4;
            return id;
        } catch (IOException e) {
            System.out.println("wrong command");
        }
        return id;
    }

}
