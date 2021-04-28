import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/*
 * Статический класс консоли для работы с консолью
 */
public class Console extends JTextArea {
    private String usersText = "";

    public Console(){
        setEditable(false);
    }

    public void write(String text){
        setText(getText() + text + "\n");
    }

    public void write(String[] text) {
    }

    public String read(){
        setEditable(true);
        addKeyListener(readAction());
        return usersText;
    }

    public void deleteListener(){
        setEditable(false);
        removeKeyListener(readAction());
    }

    public void clear(){
        setText("");
    }

    private KeyListener readAction(){
        return new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                while (e.getKeyCode() != KeyEvent.VK_ENTER){
                    usersText += e.getKeyChar();
                }
            }
        };
    }

}
