import java.util.*;

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
     * Список меток
     */
    private ArrayList<String> labelName = new ArrayList<String>();
    private ArrayList<Integer> labelCoord = new ArrayList<Integer>();

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

    private boolean isInside = false;

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
    public Interpreter(Poliz poliz, ArrayList<String> varName, ArrayList<String> varType, ArrayList<Object> varValue, UI ui){
        this.poliz = poliz;
        this.varName = varName;
        this.varType = varType;
        this.varValue = varValue;
        this.ui = ui;
        isInside = true;
    }

    /*
     * Главный метод интепритатора, запускающий интерпритацию
     */
    public void start(){
        for (count = 0; count < poliz.getSize() - 1; count++){
            first:
            if (poliz.get(count).split(" - ")[0].charAt(0) == 'V')
                variable(count);
            else if (poliz.get(count).split(" - ")[0].equals("W"))
                print(count);
            else if (poliz.get(count).split(" - ")[0].charAt(0) == 'L')
                label(count);
            else if (poliz.get(count).split(" - ")[0].equals("!FOR"))
                loop(count);
            else if (poliz.get(count).split(" - ")[0].charAt(0) == 'G') {
                goTo(count);
                break first;
            }
            else
                other(count);
        }
    }

    /*
     * Метод создания переменных для интепритатора
     */
    private void variable(int index) {
        //если переменная типа int
        if (poliz.get(index + 1).split(" - ")[0].equals("0")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains(",") && !poliz.get(index + 3).split(" - ")[0].contains("R")) {
                //если значение переменной принимается от другой переменной
                for (String var: varName) {
                    if (var.equals(poliz.get(index + 3).split(" - ")[0])) {
                        varName.add(poliz.get(index).split(" - ")[0].substring(1));
                        varValue.add(varValue.get(varName.indexOf(var)));
                        varType.add("int");
                        ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                        count += 3;
                        return;
                    }
                }
                //если значение переменной явно инициализировано
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
                //если значение переменной принимается от другой переменной
                for (String var: varName) {
                    if (var.equals(poliz.get(index + 3).split(" - ")[0])) {
                        varName.add(poliz.get(index).split(" - ")[0].substring(1));
                        varValue.add(varValue.get(varName.indexOf(var)));
                        varType.add("double");
                        ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                        count += 3;
                        return;
                    }
                }
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
                for (String var: varName) {
                    if (var.equals(poliz.get(index + 3).split(" - ")[0].substring(1)) && varType.get(varName.indexOf(var)).equals("string")) {
                        varName.add(poliz.get(index).split(" - ")[0].substring(1));
                        varValue.add(varValue.get(varName.indexOf(var)));
                        varType.add("string");
                        ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                        count += 2;
                        return;
                    }
                }
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(poliz.get(index + 3).split(" - ")[0].substring(1));
                varType.add("string");
                ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 2;
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                if (poliz.get(index + 3).split(" - ")[0].charAt(0) == 'M' && poliz.get(index + 3).split(" - ")[0].charAt(1) == 'A') {

                    poliz.replace(index + 3, poliz.get(index + 3).replaceAll(" \\+ ", ""));
                    poliz.replace(index + 3, poliz.get(index + 3).replaceAll("\"", ""));
                    poliz.replace(index + 3, poliz.get(index + 3).substring(1));

                    //если внутри считаемого значения есть другие переменные
                    for (int i = 0; i < varName.size(); i++) {
                        //если встречаем имя существующей переменной
                        if (poliz.get(index + 3).split(" - ")[0].contains(varName.get(i))) {
                            //заменяем имя переменной на её значение
                            poliz.replace(index + 3, poliz.get(index + 3).replaceAll(varName.get(i), varValue.get(i).toString()));
                        }
                    }

                    varName.add(poliz.get(index).split(" - ")[0].substring(1));
                    varValue.add(poliz.get(index + 3).split(" - ")[0].substring(2));
                    varType.add("string");
                    ui.console.write("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                    count += 2;
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
                count += 2;
            }
        }
    }

    /*
     * Метод работы цикла for
     */
    private void loop(int index){
        /*
         * Вырезаем ПОЛИЗ всего, что происходит в цикле
         */
        Poliz tempPoliz = this.poliz;

        int i = 0;

        while (i != index){
            tempPoliz.remove(0);
            i++;
        }

        int countParenthesis = 0;
        int end = 0;
        int start = 0;
        Poliz loopPoliz = new Poliz();
        for (String line: tempPoliz.getAll()) {
            if (line.split(" - ")[0].equals("!FOR") || line.split(" - ")[0].equals("!IF") || line.split(" - ")[0].equals("!ELSE"))
                countParenthesis++;
            else if (line.split(" - ")[0].equals("!!FOR") || line.split(" - ")[0].equals("!!IF") || line.split(" - ")[0].equals("!!ELSE")) {
                countParenthesis--;
                end = Integer.parseInt(line.split(" - ")[1]);
            }
            if (countParenthesis == 0){
                start = Integer.parseInt(tempPoliz.get(0).split(" - ")[1]);
                for (i = 1; i < (end - start); i++){
                    loopPoliz.toPoliz(tempPoliz.get(i).split(" - ")[0]);
                }
                break;
            }

        }

        /*
         * Вырезаем из ПОЛИЗа условие окончание цикла и его шаг
         */
        Poliz optionsPoliz = new Poliz();
        end = 0;
        for (i = 0; i < loopPoliz.getSize()-1; i++){
            optionsPoliz.toPoliz(loopPoliz.get(i).split(" - ")[0]);

            if (loopPoliz.get(i).split(" - ")[0].equals("="))
                end++;

            if (end == 2)
                break;
        }

        /*
         * Вырезаем из ПОЛИЗа цикла действия, совершаемые в цикле
         */
        for (i = 0; i < optionsPoliz.getSize()-1; i++){
            loopPoliz.remove(0);
        }

        tempPoliz = new Poliz();
        for (String line: loopPoliz.getAll()) {
            tempPoliz.toPoliz(line.split(" - ")[0]);
        }

        loopPoliz.toPoliz("1");
        loopPoliz.toPoliz("2");

        for (i = 0; i < loopPoliz.getSize()-1; i++) {
            if (i == 0)
                loopPoliz.replace(i, "START" + " - " + (i+1));
            else if (i == 1)
                loopPoliz.replace(i, "END" + " - " + (i+1));
            else
                loopPoliz.replace(i, tempPoliz.get(i-2).split(" - ")[0] + " - " + (i+1));
        }

        System.out.println(Arrays.toString(optionsPoliz.getAll()));
        System.out.println(Arrays.toString(loopPoliz.getAll()));

        // меньше
        if (optionsPoliz.get(5).split(" - ")[0].equals("<")){
            // если переменная типа инт
            if (optionsPoliz.get(0).split(" - ")[0].charAt(0) == 'T' && optionsPoliz.get(1).split(" - ")[0].equals("0")){
                int counter = 0;
                //если меньше числа, а не переменной
                if (optionsPoliz.get(6).split(" - ")[0].matches("\\d++")) {
                    //цикл
                    for (counter = Integer.parseInt(optionsPoliz.get(3).split(" - ")[0]); counter < Integer.parseInt(optionsPoliz.get(6).split(" - ")[0])-1; counter++){
                        System.out.println("от " + optionsPoliz.get(3).split(" - ")[0] + " до " + optionsPoliz.get(6).split(" - ")[0]);
                        Interpreter inter = new Interpreter(loopPoliz, this.varName, this.varType, this.varValue, this.ui);
                        inter.start();
                        count = end + 1;
                    }
                }
            }
        }

    }

    /*
     * Метод, выводящий на консоль сообщение при
     * вызове метода cout() в программе
     */
    private void print(int index){
        String print = poliz.get(index + 1).split(" - ")[0];

        for (int i = 0; i < varName.size(); i++)
            if (varName.get(i).equals(print)) {
                ui.console.write("[INTERPRETER] - print: " + varValue.get(i));
                count += 1;
                return;
            }

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

                //если значение переменной явно задано
                if (poliz.get(index + 1).split(" - ")[0].matches("\\d++")){
                    if (varType.get(i).equals("int") || varType.get(i).equals("double")){
                        varValue.set(i, poliz.get(index + 1).split(" - ")[0]);
                        ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                    }
                }else if (poliz.get(index + 1).split(" - ")[0].matches("\\d++[.]\\d++")){
                    if (varType.get(i).equals("double")){
                        varValue.set(i, poliz.get(index + 1).split(" - ")[0]);
                        ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                    }
                } else if (poliz.get(index + 1).split(" - ")[0].matches("[\"]\\w[\"]")){
                    if (varType.get(i).equals("string")){
                        varValue.set(i, poliz.get(index + 1).split(" - ")[0]);
                        ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                    }
                }

                //если значение переменной необходимо вычислить
                if (poliz.get(index + 1).split(" - ")[0].contains("[")) {
                    //в зависимости от типа переменной, изменяем её значение
                    if (varType.get(i).equals("int")){

                        //если внутри считаемого значения есть другие переменные
                        for (int j = 0; j < varName.size(); j++){
                            //если встречаем имя существующей переменной
                            if (poliz.get(index + 1).split(" - ")[0].contains(varName.get(j))){
                                //проверяем является ли переменная нужного нам типа
                                if (!varType.get(j).equals("int")) {
                                    ui.console.write(Errors.getErrors(7, 0));

                                    count = poliz.getSize();
                                    return;
                                }
                                //заменяем имя переменной на её значение
                                poliz.replace(index + 1, poliz.get(index + 1).replaceAll(varName.get(j), varValue.get(j).toString()));
                            }
                        }

                        List<String> expression = StrToListStr(poliz.get(index + 1));
                        varValue.set(i, (int) Double.parseDouble(Ideone.calc(expression).toString()));
                        ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                        count += 2;
                    }
                    else if (varType.get(i).equals("double")){

                        //если внутри считаемого значения есть другие переменные
                        for (int j = 0; j < varName.size(); j++){
                            //если встречаем имя существующей переменной
                            if (poliz.get(index + 1).split(" - ")[0].contains(varName.get(j))){
                                //проверяем является ли переменная нужного нам типа
                                if (!varType.get(j).equals("double")) {
                                    ui.console.write(Errors.getErrors(7, 0));
                                    count = poliz.getSize();
                                    return;
                                }
                                //заменяем имя переменной на её значение
                                poliz.replace(index + 1, poliz.get(index + 1).replaceAll(varName.get(j), varValue.get(j).toString()));
                            }
                        }

                        List<String> expression = StrToListStr(poliz.get(index + 1));
                        varValue.set(i, Double.parseDouble(Ideone.calc(expression).toString()));
                        ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                        count += 2;
                    }
                    else if (varType.get(i).equals("string")){
                        //если склеиваются две строки
                        if (poliz.get(index + 1).split(" - ")[0].charAt(0) == 'M' && poliz.get(index + 1).split(" - ")[0].charAt(1) == 'A') {

                            poliz.replace(index + 1, poliz.get(index + 1).replaceAll(" \\+ ", " "));

                            //если внутри считаемого значения есть другие переменные
                            for (int j = 0; j < varName.size(); j++) {
                                //если встречаем имя существующей переменной
                                if (poliz.get(index + 1).split(" - ")[0].contains(varName.get(j))) {
                                    //заменяем имя переменной на её значение
                                    poliz.replace(index + 1, poliz.get(index + 1).replaceAll(varName.get(j), varValue.get(j).toString()));
                                }
                            }

                            varValue.set(i ,poliz.get(index + 1).split(" - ")[0].substring(2));
                            ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                            count += 3;
                        }
                    }
                }

                /*
                 * Если изменяется значение переменной типа string
                 */
                if (poliz.get(index + 1).split(" - ")[0].charAt(0) == 'S'){
                    varValue.set(i, poliz.get(index + 1).split(" - ")[0].substring(1));
                    ui.console.write("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
                }
                //если принимает значение переменной
                else if (poliz.get(index + 1).split(" - ")[0].charAt(0) != 'S'){
                    for (String var : varName)
                        if (var.equals(poliz.get(index + 1).split(" - ")[0])){
                            varValue.set(i, varValue.get(varName.indexOf(var)));
                        }
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
     * Метод создания меток
     */
    private void label(int index){
        labelName.add(poliz.get(index).split(" - ")[0].substring(1));
        labelCoord.add(Integer.parseInt(poliz.get(index).split(" - ")[1]) - 1);
    }

    /*
     * Метод перехода на метку
     */
    private void goTo(int index){
        for (int i = 0; i < labelName.size(); i++)
            if (poliz.get(index).split(" - ")[0].substring(1).equals(labelName.get(i))) {
                count = labelCoord.get(i);
                return;
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
todo условия
todo строки не изменяются
 */
}