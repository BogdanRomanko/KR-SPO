import javax.swing.*;

/*
 * Статический класс консоли для работы с пользовательской консолью
 */
public class Console extends JTextArea {

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
        for (String s: text) {
            setText(getText() + s + "\n");
        }
    }

    /*
     * Метод, очищающий консоль
     */
    public void clear(){
        setText("");
    }

}
