import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Класс интерпритатора, принимающий ПОЛИЗ программы
 * и запускающий работу программы
 */
public class Interpreter {

    /*
     * Поле, хранящее ПОЛИЗ для интерпритации
     */
    private Poliz poliz;

    /*
     * Списки с переменными
     */
    private ArrayList<String> varName = new ArrayList <String>();
    private ArrayList<String> varType = new ArrayList <String>();
    private ArrayList<Object> varValue = new ArrayList <Object>();

    /*
     * Поле со сканером для работы внутри всего интерпритатора
     */
    private Scanner scanner;

    /*
     * Счётчик для главного цикла
     */
    private int count = 0;

    /*
     * Поле с ссылкой на интерфейс
     */
    private UI ui;

    /*
     * Конструктор интерпритатора, принимающий полиз и ссылку
     * на интерфейс пользователя для работы с консолью и возвращающий
     * основной экземпляр интерпритатора
     */
    public Interpreter(Poliz poliz, UI ui) {
        this.ui = ui;
        this.poliz = poliz;

        scanner = new Scanner(System.in);
    }

    /*
     * Конструктор дополнительного интерпритатора, вызываемого в циклах или условиях
     */
    public Interpreter(Poliz poliz, ArrayList<String> varName, ArrayList<String> varType, ArrayList<Object> varValue){
        this.poliz = poliz;
        this.varName = varName;
        this.varType = varType;
        this.varValue = varValue;
    }

    /*
     * Главный метод интепритатора, запускающий интерпритацию
     */
    public void start(){
        for (count = 0; count < poliz.getSize() - 1; count++){
            if (poliz.get(count).split(" - ")[0].charAt(0) == 'V')
                variable(count);
            else if (poliz.get(count).split(" - ")[0].equals("W"))
                print(count);
            else
                other(count);
        }

        ui.console.write("--------------\nКонец программы");
    }

    /*
     * Метод создания переменных для интепритатора
     */
    private void variable(int index) {
        //если переменная типа int
        if (poliz.get(index + 1).split(" - ")[0].equals("0")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains(",") && !poliz.get(index + 3).split(" - ")[0].contains("R")) {
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(Integer.parseInt(poliz.get(index + 3).split(" - ")[0]));
                varType.add("int");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //если внутри считаемого значения есть другие переменные
                for (int i = 0; i < varName.size(); i++){
                    //если встречаем имя существующей переменной
                    if (poliz.get(index + 3).split(" - ")[0].contains(varName.get(i))){
                        //проверяем является ли переменная нужного нам типа
                        if (!varType.get(i).equals("int"))
                            Errors.errors(7);
                        //заменяем имя переменной на её значение
                        poliz.replace(index + 3, poliz.get(index + 3).replaceAll(varName.get(i), varValue.get(i).toString()));
                    }
                }

                List<String> expression = StrToListStr(poliz.get(index + 3));
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add((int) Double.parseDouble(Ideone.calc(expression).toString()));
                varType.add("int");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной необходимо считать с консоли
            else {
                ui.console.write("[INTERPRETER] READ:");
                ui.console.setEditable(true);
                int temp = scanner.nextInt();
                ui.console.setEditable(false);
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("int");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
        }
        //если переменная типа double
        else if (poliz.get(index + 1).split(" - ")[0].equals("1")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains(",") && !poliz.get(index + 3).split(" - ")[0].contains("R")){
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(Double.parseDouble(poliz.get(index + 3).split(" - ")[0]));
                varType.add("double");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //если внутри считаемого значения есть другие переменные
                for (int i = 0; i < varName.size(); i++){
                    //если встречаем имя существующей переменной
                    if (poliz.get(index + 3).split(" - ")[0].contains(varName.get(i))){
                        //заменяем имя переменной на её значение
                        poliz.replace(index + 3, poliz.get(index + 3).replaceAll(varName.get(i), varValue.get(i).toString()));
                    }
                }

                List<String> expression = StrToListStr(poliz.get(index + 3));
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(Double.parseDouble(Ideone.calc(expression).toString()));
                varType.add("double");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной необходимо считать с консоли
            else {
                ui.console.write("[INTERPRETER] READ:");
                ui.console.setEditable(true);
                double temp = scanner.nextDouble();
                ui.console.setEditable(false);
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("double");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
        }
        //если переменная типа string
        else if (poliz.get(index + 1).split(" - ")[0].equals("2")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains("R") && poliz.get(index + 3).split(" - ")[0].charAt(0) != 'M') {
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(poliz.get(index + 3).split(" - ")[0].substring(1));
                varType.add("string");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                if (poliz.get(index + 3).split(" - ")[0].charAt(0) == 'M' && poliz.get(index + 3).split(" - ")[0].charAt(1) == 'A') {

                    poliz.replace(index + 3, poliz.get(index + 3).replaceAll(" \\+ ", " "));

                    //если внутри считаемого значения есть другие переменные
                    for (int i = 0; i < varName.size(); i++) {
                        //если встречаем имя существующей переменной
                        if (poliz.get(index + 3).split(" - ")[0].contains(varName.get(i))) {
                            //заменяем имя переменной на её значение
                            poliz.replace(index + 3, poliz.get(index + 3).replaceAll(varName.get(i), varValue.get(i).toString()));
                        }
                    }

                    //ui.console.write("ЭТО БЛЯТЬ КОНКАНТЕНАЦИЯ");
                    varName.add(poliz.get(index).split(" - ")[0].substring(1));
                    varValue.add(poliz.get(index + 3).split(" - ")[0].substring(2));
                    varType.add("string");
                    ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                    count += 3;
                }
            }
            //если значение переменной необходимо считать с консоли
            else {
                ui.console.write("[INTERPRETER] READ:");
                ui.console.setEditable(true);
                String temp = scanner.nextLine();
                ui.console.setEditable(false);
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("string");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
        }
    }

    /*
     * Метод, выводящий на консоль сообщение при
     * вызове метода cout() в программе
     */
    private void print(int index){
        String print = poliz.get(index + 1).split(" - ")[0];
        ui.console.write("[INTERPRETER] - print: " + print);
        count += 1;
    }

    /*
     * Метод, который проверяет всё остальное, что не попало в
     * основные ветки проверок, например, изменение значений
     * переменных
     */
    private void other(int index){
        //если происходит мутации переменных
        for (int i = 0; i < varName.size(); i++){
            if (poliz.get(index).split(" - ")[0].equals(varName.get(i))){
                //если значение переменной заранее известно
                if (poliz.get(index + 1).split(" - ")[0].matches("\\d{1,}")){
                    varValue.set(i, poliz.get(index + 1).split(" - ")[0]);
                    ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                }
                //если значение переменной необходимо вычислить
                else if (poliz.get(index + 1).split(" - ")[0].contains("[")) {
                    /*//в зависимости от типа переменной, изменяем её значение
                    if (varType.get(i).equals("int")){
                    todo пиздец, подумай над мутациями
                    varValue.set(i, );
                    } else if (varType.get(i).equals("double")){

                        varValue.set(i, );
                    }
                    else if (varType.get(i).equals("string")){

                        varValue.set(i, );
                    }
                    System.out.println("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));*/
                }
                //если значение переменной необходимо считать с консоли
                else if (poliz.get(index + 1).split(" - ")[0].equals("R")){
                    //в зависимости от типа переменной, изменяем её значение
                    if (varType.get(i).equals("int")){
                        ui.console.write("[INTERPRETER] READ:");
                        ui.console.setEditable(true);
                        int temp = scanner.nextInt();
                        ui.console.setEditable(false);
                        varValue.set(i, temp);
                    } else if (varType.get(i).equals("double")){
                        ui.console.write("[INTERPRETER] READ:");
                        ui.console.setEditable(true);
                        double temp = scanner.nextDouble();
                        ui.console.setEditable(false);
                        varValue.set(i, temp);
                    }
                    else if (varType.get(i).equals("string")){
                        ui.console.write("[INTERPRETER] READ:");
                        ui.console.setEditable(true);
                        String temp = scanner.nextLine();
                        ui.console.setEditable(false);
                        varValue.set(i, temp);
                    }
                    ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                }
            }
        }
    }

    /*
     * Метод, конвертирующий строку ПОЛИЗа в
     * List для обработки её классом математических вычислений Ideone
     */
    private List<String> StrToListStr(String text){
        List<String> result =  new ArrayList<String>();

        String math = text.split(" - ")[0];
        math = math.substring(1, math.length() - 1);

        String[] tokens = math.split(", ");

        for (String s: tokens) {
            result.add(s);
        }

        return result;
    }
/*
todo действия над переменными
todo - мат операции
todo условия
todo циклы
todo goto
 */


}
