package utils;

public enum CmdClient {
    STRT, CASH, HITT, SHOW, BETT, SRND, RPLY, EXIT, ERRO;

    CmdClient() {
    }

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
            case ERRO:
                return "ERRO";

            default:
                throw new IllegalArgumentException();
        }
    }

    public static CmdClient getCommand(String command) {
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
        }else if ("ERRO".equals(command)){
            return ERRO;
        } else {
            return null;
        }

    }
}
