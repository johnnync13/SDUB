package utils;

import java.io.IOException;

public class DetectErrors {
    ComUtils comUtils;

    /**
     * Constructor de la clase DetectErrors
     * @param com
     */
    public DetectErrors(ComUtils com) {
        this.comUtils = com;
    }

    /**
     * Método que comprueba que un comando esté correctamente escrito.
     * retorna -1,-2,-3 o -4.
     * -1: el  comando recibido no es el esperado
     * -2:el espacio enviado no está en el formato adecuado
     * -4: el identificador/cash es un integer no positivo
     * -3: Comando correcto
     * @param command
     * @return int
     */
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
