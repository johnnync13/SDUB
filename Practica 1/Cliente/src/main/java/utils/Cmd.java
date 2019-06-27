package utils;

public enum Cmd {
    STRT, CASH, HITT, SHOW, BETT, SRND, RPLY, EXIT,INIT, IDCK, CARD, WINS, ERRO;

    /**
     * Devuelve el nombre correspondiente al comando
     * @return
     */
    @Override
    public String toString() {
        switch (this) {
            case STRT:
                return "STRT";
            case CASH:
                return "CASH";
            case HITT:
                return "HITT";
            case SHOW:
                return "SHOW";
            case BETT:
                return "BETT";
            case SRND:
                return "SRND";
            case RPLY:
                return "RPLY";
            case EXIT:
                return "EXIT";
            case INIT:
                return "INIT";
            case IDCK:
                return "IDCK";
            case CARD:
                return "CARD";
            case WINS:
                return "WINS";
            case ERRO:
                return "ERRO";
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Comprueba si el comando pasado por parámetro exite.
     * Si no devuelve null.
     * @param command
     * @return
     */
    public static Cmd getCommand(String command) {
        if ("STRT".equals(command)) {
            return STRT;
        } else if ("CASH".equals(command)) {
            return CASH;
        } else if ("HITT".equals(command)) {
            return HITT;
        } else if ("SHOW".equals(command)) {
            return SHOW;
        } else if ("BETT".equals(command)) {
            return BETT;
        } else if ("SRND".equals(command)) {
            return SRND;
        } else if ("RPLY".equals(command)) {
            return RPLY;
        } else if ("EXIT".equals(command)) {
            return EXIT;
        }else if ("INIT".equals(command)) {
            return INIT;
        } else if ("IDCK".equals(command)) {
            return IDCK;
        } else if ("CARD".equals(command)) {
            return CARD;
        }else if ("WINS".equals(command)) {
            return WINS;
        }else if ("ERRO".equals(command)) {
                return ERRO;
        } else {
            return null;
        }

    }
}
