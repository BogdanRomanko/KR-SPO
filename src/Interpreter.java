import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Interpreter {

    private Poliz poliz;
    private ArrayList<String> varName = new ArrayList <String>();
    private ArrayList<String> varType = new ArrayList <String>();
    private ArrayList<Object> varValue = new ArrayList <Object>();
    private Scanner scanner = new Scanner(System.in);
    private int count = 0;

    /*
     * Конструктор интерпритатора, принимающий полиз и возвращающий
     * основной экземпляр интерпритатора
     */
    public Interpreter(Poliz poliz) {
        this.poliz = poliz;
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
     * главный метод интепритатора, запускающий интерпритацию
     */
    public void start(){
        for ( count = 0; count < poliz.getSize() - 1; count++){
            if (poliz.get(count).split(" - ")[0].charAt(0) == 'V')
                variable(count);
            else if (poliz.get(count).split(" - ")[0].equals("W"))
                print(count);
            else
                other(count);
        }
    }

    /*
     * метод создания переменных для интепритатора
     */
    private void variable(int index) {
        //если переменная типа int
        if (poliz.get(index + 1).split(" - ")[0].equals("0")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains(",") && !poliz.get(index + 3).split(" - ")[0].contains("R")) {
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(Integer.parseInt(poliz.get(index + 3).split(" - ")[0]));
                varType.add("int");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
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
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной необходимо считать с консоли
            else {
                System.out.print("[INTERPRETER] READ:");
                int temp = scanner.nextInt();
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("int");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
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
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
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
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной необходимо считать с консоли
            else {
                System.out.print("[INTERPRETER] READ:");
                double temp = scanner.nextDouble();
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("double");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
        }
        //если переменная типа string
        else if (poliz.get(index + 1).split(" - ")[0].equals("2")) {
            //если значение переменной уже известно
            if (!poliz.get(index + 3).split(" - ")[0].contains(",") && !poliz.get(index + 3).split(" - ")[0].contains("R")) {
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(poliz.get(index + 3).split(" - ")[0]);
                varType.add("string");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //todo сделать
                count += 3;
            }
            //если значение переменной необходимо считать с консоли
            else {
                System.out.print("[INTERPRETER] READ:");
                scanner.nextLine();
                String temp = scanner.nextLine();
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("string");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
                count += 3;
            }
        }
    }

    private void print(int index){
        String print = poliz.get(index + 1).split(" - ")[0];
        System.out.println("[INTERPRETER] - print:" + print);
        count += 1;
    }

    private void other(int index){
        //если происходит мутации переменных
        for (int i = 0; i < varName.size(); i++){
            if (poliz.get(index).split(" - ")[0].equals(varName.get(i))){
                //если значение переменной заранее известно
                if (poliz.get(index + 1).split(" - ")[0].matches("\\d{1,}")){
                    varValue.set(i, poliz.get(index + 1).split(" - ")[0]);
                    System.out.println("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
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
                        System.out.print("[INTERPRETER] READ:");
                        int temp = scanner.nextInt();
                        varValue.set(i, temp);
                    } else if (varType.get(i).equals("double")){
                        System.out.print("[INTERPRETER] READ:");
                        double temp = scanner.nextDouble();
                        varValue.set(i, temp);
                    }
                    else if (varType.get(i).equals("string")){
                        System.out.print("[INTERPRETER] READ:");
                        scanner.nextLine();
                        String temp = scanner.nextLine();
                        varValue.set(i, temp);
                    }
                    System.out.println("[INTERPRETER] - " + varType.get(i) + " " + varName.get(i) + " = " + varValue.get(i));
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
