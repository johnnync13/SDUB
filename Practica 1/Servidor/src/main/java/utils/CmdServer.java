package utils;

public enum CmdServer {
    INIT, IDCK, CARD, SHOW, WINS, ERRO;

    CmdServer() {
    }

    /**
     * Método que devuelve el nombre correspondiente al comando
     * @return String
     */
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
}
