import org.junit.Test;
import utils.ComUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class ComUtilsTest {


  @Test
    public void int_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_int32(2);
            int readedInt = comUtils.read_int32();

            assertEquals(2, readedInt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void char_test (){
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_char('a');
            int readedChar = comUtils.read_character();

            assertEquals('a', readedChar);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void space_test (){
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_char(' ');
            int readedChar = comUtils.read_character();

            assertEquals(' ', readedChar);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void command_test() {
        File file = new File("test");
        try {
            file.createNewFile();
            ComUtils comUtils = new ComUtils(new FileInputStream(file), new FileOutputStream(file));
            comUtils.write_command("HOLA");
            String readedInt = comUtils.read_command();

            assertEquals("HOLA", readedInt);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
