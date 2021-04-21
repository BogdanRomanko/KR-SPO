import java.util.ArrayList;
import java.util.Scanner;

public class Interpreter {

    private Poliz poliz;
    private ArrayList<String> varName = new ArrayList <String>();
    private ArrayList<String> varType = new ArrayList <String>();
    private ArrayList<Object> varValue = new ArrayList <Object>();
    private Scanner scanner = new Scanner(System.in);

    public Interpreter(Poliz poliz) {
        this.poliz = poliz;
    }

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
        for (int i = 0; i < poliz.getSize() - 1; i++){
            if (poliz.get(i).split(" - ")[0].charAt(0) == 'V')
                variable(i);
            else if (poliz.get(i).split(" - ")[0].equals("W"))
                print(i);
            else
                other(i);
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
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //todo сделать
            }
            //если значение переменной необходимо считать с консоли
            else {
                System.out.print("[INTERPRETER] READ:");
                int temp = scanner.nextInt();
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("int");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
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
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //todo сделать
            }
            //если значение переменной необходимо считать с консоли
            else {
                System.out.print("[INTERPRETER] READ:");
                double temp = scanner.nextDouble();
                varName.add(poliz.get(index).split(" - ")[0].substring(1));
                varValue.add(temp);
                varType.add("double");
                System.out.println("[INTERPRETER] - " + varType.get(varType.size() - 1) + " " + varName.get(varName.size() - 1) + " = " + varValue.get(varValue.size() - 1));
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
            }
            //если значение переменной предстоит рассчитать
            else if (!poliz.get(index + 3).split(" - ")[0].contains("R")){
                //todo сделать
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
            }
        }
    }

    private void print(int index){
        String print = poliz.get(index + 1).split(" - ")[0];
        System.out.println("[INTERPRETER] - print:" + print);
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
                else if (poliz.get(index + 1).split(" - ")[0].contains("[")){
                    //todo сделать
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
todo действия над переменными
todo условия
todo циклы
todo goto
 */


}
