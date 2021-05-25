
/*
 * Класс, отвечающий за запуск лексического анализатора,
 * синтаксического анализатора, семантического анализатора, ПОЛИЗа
 * и интерпритатора на выполнение в отдельном потоке для корректной работы
 * с консолью интерфейса пользователя
 */
public class InterpreterThread extends Thread {

    /*
     * Поле, хранящее в себе код, передаваемый для анализа
     */
    private String code;

    /*
     * Поле, хранящее ссылку на пользовательский интерфейс для взаимодействия
     * с консолью
     */
    private UI ui;

    /*
     * Констуктор класса, инициализирующий поля класса
     */
    public InterpreterThread(String name, String text, UI ui) {
        super(name);
        this.code = text;
        this.ui = ui;
    }

    /*
     * Метод, запускающий в потоке лексический,
     * синтаксический и семантический анализаторы, а также
     * интерпритатор на выполнение кода программы
     */
    public void run() {
        Lexer lexer = new Lexer(code, ui);
        Syntax syntax = new Syntax(lexer, ui);

        if (!Errors.isErrors()) {
            Interpreter interpreter = new Interpreter(syntax.getPoliz(), ui);
            interpreter.start();
        }

        ui.console.write("--------------\nКонец программы");
    }

}
