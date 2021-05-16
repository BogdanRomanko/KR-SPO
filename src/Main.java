import javax.swing.*;

/*
 * Главный класс для запуска интерфейса пользователя
 */
public class Main {

    /*
     * Основной метод программы, запускающий
     * интерфейс пользователя
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UI();
            }
        });
    }
}
