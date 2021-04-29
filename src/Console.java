import javax.swing.*;
import java.awt.event.*;

/*
 * Статический класс консоли для работы с пользовательской консолью
 */
public class Console extends JTextArea {

    public String getUsersText() {
        return usersText;
    }

    /*
     * Текст, который будет возвращаться, если пользователь должен ввести какое-либо значение
     */
    private String usersText = "";

    public Console(){
        setEditable(false);
    }

    /*
     * Метод записи текста в консоль
     */
    public void write(String text){
        setText(getText() + text + "\n");
    }

    /*
     * Метод записи массива строк в консоль
     */
    public void write(String[] text) {
    }

    /*
     * Метод, разрешающий ввод данных в консоль и
     * считывающий значения, вводимые пользователем
     */
    public void read(){
        setEditable(true);
    }

    /*
     * Метод, очищающий консоль
     */
    public void clear(){
        setText("");
    }

}
