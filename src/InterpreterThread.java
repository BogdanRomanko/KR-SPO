public class InterpreterThread extends Thread {

    private String code;
    private UI ui;

    public InterpreterThread(String name, String text, UI ui) {
        super(name);
        this.code = text;
        this.ui = ui;
    }

    public void run() {
        Lexer lexer = new Lexer(code, ui);
        Syntax syntax = new Syntax(lexer, ui);

        if (!Errors.isErrors()) {
            Interpreter interpreter = new Interpreter(syntax.getPoliz(), ui);
            interpreter.start();
        }
    }

}
