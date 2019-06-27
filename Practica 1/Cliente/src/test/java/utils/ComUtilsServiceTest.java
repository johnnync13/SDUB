package utils;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.*;

public class ComUtilsServiceTest {


    @Test
    public void read_init() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("INIT");
            comUtils.write_space();
            comUtils.write_int32(100);
            int readedString = comUtilsS.read_init();
            assertEquals(100, readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getPalo() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            String palo = comUtilsS.getPalo('5');
            assertEquals("\u2665",palo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read_idck() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("IDCK");
            comUtils.write_space();
            comUtils.write_char('8');
            comUtils.write_char('\04');
            comUtils.write_space();
            comUtils.write_char('5');
            comUtils.write_char('\06');
            Deck d = new Deck();
            String readedString = comUtilsS.read_idck(d);
            assertEquals("IDCK 8"+"\u2664"+ " 5"+ "\u2666",readedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read_card() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("CARD");
            comUtils.write_space();
            comUtils.write_char('5');
            comUtils.write_char('\04');
            Deck d = new Deck();
            String readedString = comUtilsS.read_card(d);
            assertEquals("CARD 5"+"\u2664",readedString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read_win() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("WINS");
            comUtils.write_space();
            comUtils.write_char('1');
            comUtils.write_space();
            comUtils.write_int32(500);
            String readedString = comUtilsS.read_win();
            assertEquals("WINS 1 500", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void case_error() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_space();
            comUtils.write_string("error");
            String readedString = comUtilsS.case_error();
            assertEquals(" error", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isStringUpperCase() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtils = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command(Cmd.STRT.toString());
            String readedString = comUtils.read_command();
            boolean bool = comUtils.isStringUpperCase(readedString);

            assertEquals(true, bool);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sirve para escribir cualquier comando sin parametros
     */

    @Test
    public void command_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtils = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command(Cmd.STRT.toString());
            String readedString = comUtils.read_command();

            assertEquals(Cmd.STRT.toString(), readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * STRT TEST
     */
    public void strt_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtilsS.strt("STRT 100");
            String readedString ="";
            readedString += comUtils.read_command();
            readedString += comUtils.read_space();
            readedString += comUtils.read_int32();
            assertEquals("STRT 100", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * CASH TEST
     */
    public void cash_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtilsS.cash("500");
            String readedString ="";
            readedString += comUtils.read_command();
            readedString += comUtils.read_space();
            readedString += comUtils.read_int32();
            assertEquals("CASH 500", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    /**
     * SHOW TEST
     */
    public void show_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            Deck d = new Deck();
            d.addCard('3', 4);
            d.addCard('2', 3);
            comUtilsS.show(d);
            String readedString ="";
            readedString += comUtilsS.read_show(d);
            assertEquals("SHOW 2 3"+"\u2664"+" "+"2"+"\u2663", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}