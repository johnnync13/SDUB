package utils;

public enum CmdServer {
    INIT, IDCK, CARD, SHOW, WINS, ERRO;

    CmdServer() {
    }

    @Override
    public String toString() {
        switch (this) {
            case INIT:
                return "INIT";
            case IDCK:
                return "IDCK";
            case CARD:
                return "CARD";
            case SHOW:
                return "SHOW";
            case WINS:
                return "WINS";
            case ERRO:
                return "ERRO";
            default:
                throw new IllegalArgumentException();
        }
    }

    public static CmdServer getCommand(String command) {
        if ("INIT".equals(command)) {
            return INIT;
        } else if ("IDCK".equals(command)) {
            return IDCK;
        } else if ("CARD".equals(command)) {
            return CARD;
        } else if ("SHOW".equals(command)) {
            return SHOW;
        } else if ("WINS".equals(command)) {
            return WINS;
        }else if ("ERRO".equals(command)){
            return ERRO;
        } else {
            return null;
        }

    }
}
