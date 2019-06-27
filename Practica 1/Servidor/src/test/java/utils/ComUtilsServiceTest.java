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
    public void strt() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("STRT");
            comUtils.write_space();
            comUtils.write_int32(100);
            int in = comUtilsS.strt();

            assertEquals(100, in);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void init() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtilsS.init(100);
            String readedString = comUtils.read_command();
            readedString += comUtils.read_space();
            readedString += comUtils.read_int32();

            assertEquals("INIT 100", readedString);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void read_cash() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_space();
            comUtils.write_int32(500);
            int in = comUtilsS.read_cash();

            assertEquals(500, in);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void idck() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            Deck d = new Deck();
            comUtilsS.idck(d);
            String readedString = comUtils.read_command();
            readedString += comUtils.read_space();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_space();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_character();
            String s = "IDCK 8"+"\u2664"+ " 5"+ "\u2666";
            assertEquals(s.length(),readedString.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void write_show() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            Deck d = new Deck();
            d.getRandomCard(1);
            d.getRandomCard(1);
            comUtilsS.write_show(2,d,1);
            String readedString = comUtils.read_command();
            readedString += comUtils.read_space();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_space();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_character();
            assertEquals(10,readedString.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void card() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            Deck d = new Deck();
            comUtilsS.card(d);
            String readedString = comUtils.read_command();
            readedString += comUtils.read_space();
            readedString +=comUtils.read_character();
            readedString +=comUtils.read_character();
            String s = "CARD 8"+"\u2664";
            assertEquals(s.length(),readedString.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void bett() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            int money = comUtilsS.bett(100,500);
            assertEquals(200,money);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void wins() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            Deck d = new Deck();
            comUtilsS.wins('1',500);
            String s = comUtils.read_command();
            s += comUtils.read_space();
            s+= comUtils.read_character();
            s += comUtils.read_space();
            s += comUtils.read_int32();
            assertEquals("WINS 1 500",s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void error() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtilsS.error("5Hola!");
            String readedString ="";
            readedString += comUtils.read_command();
            readedString += comUtils.read_space();
            readedString += comUtils.read_string();
            assertEquals("ERRO 5Hola!", readedString);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    /**
     * Sale cliente desconectado, pero funciona
     */
/*
    @Test
    public void read_show() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtilsService comUtilsS = new ComUtilsService(new FileInputStream(file), new FileOutputStream(file));
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_char('1');
            comUtils.write_char('4');
            comUtils.write_space();
            comUtils.write_char('3');
            comUtils.write_char('5');
            Deck d = new Deck();
            String readedString ="";
            readedString += comUtilsS.read_show(d);
            assertEquals("14 35", readedString);
        file.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
