import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;

/*
 * Класс для перенаправления стандартного потока ввода данных
 * System.in в консоль интерфейса пользователя
 */
class TextAreaStreamer extends InputStream implements KeyListener {

    /*
     * Поле, хранящее ссылку на консоль в интерфейсе пользователя
     */
    private Console console;

    /*
     * Поле, хранящее текст консоли в интерфейсе пользователя
     */
    private String str = null;

    /*
     * Поле, хранящее позицию, с которой необходимо будет начинать
     * возвращение данных
     */
    private int pos = 0;

    /*
     * Констуктор, инициализирующий консоль интерфейса
     * пользователя для перенаправления ввода данных
     * через неё
     */
    public TextAreaStreamer(Console console) {
        this.console = console;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /*
     * Слушатель, реагирующий на ввод данных в консоль
     */
    @Override
    public void keyPressed(KeyEvent e) {
        /*
         * Проверяем была ли нажата клавиша ENTER
         */
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            /*
             * Расчитываем позицию, с которой следует начинать
             * возвращение данных, запоминаем текст из консоли
             */
            int endpos = console.getCaret().getMark();
            int startpos = console.getText().substring(0, endpos-1).lastIndexOf('\n')+1;
            str = console.getText() + "\n";
            pos = startpos;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /*
     * Метод, возвращающий индекс символа
     */
    @Override
    public int read() {
        /*
         * Проверяем закончился ли ввод данных и заканчиваем
         * его
         */
        if (str != null && pos == str.length()) {
            str = null;
            return java.io.StreamTokenizer.TT_EOF;
        }

        while (str == null || pos >= str.length()) {
            try {
                synchronized (this) {
                    this.wait();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        /*
         * Читаем дополнительный символ, возвращаем его и увеличиваем
         * позицию
         */
        return str.charAt(pos++);
    }
}