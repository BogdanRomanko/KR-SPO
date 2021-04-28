import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class Syntax {

    private Lexer lexer;

    private ArrayList<String> varName;
    private ArrayList<String> varType;
    private ArrayList<Object> varValue;

    private HashMap<String, Integer> parenthesisFor = new HashMap<>();
    private HashMap<String, Integer> parenthesisIf = new HashMap<>();
    private HashMap<String, Integer> parenthesisElse = new HashMap<>();

    private ArrayList<String> labels;

    private Poliz poliz = new Poliz();

    private UI ui;

    Syntax(Lexer lexer, UI ui) {
        this.lexer = lexer;
        this.ui = ui;

        Errors.clearErrors();

        varName = new ArrayList <String>();
        varType = new ArrayList <String>();
        varValue = new ArrayList <Object>();

        labels = new ArrayList <String>();

        ArrayList<Integer> labelsIndex = new ArrayList<>();

        if(checkStart())
            System.out.println("Шапка программы - OK");

        for (int i = 5; i < lexer.text.size(); i++){
            switch (lexer.text.get(i)) {
                case "int":
                    checkInt(i);
                    break;
                case "double":
                    checkDouble(i);
                    break;
                case "string":
                    checkString(i);
                    break;
                case "for":
                    checkFor(i);
                    break;
                case "if":
                    checkIf(i);
                    break;
                case ":":
                    labelsIndex.add(checkLabel(i));
                    break;
                case "goto":
                    checkGoto(i);
                    break;
                case "cout":
                    checkCout(i);
                    break;
                case "cin":
                    checkCin(i);
                    break;
                default:
                    checkOther(i);
                    break;
            }
            for (int j = 0; j < parenthesisFor.size(); j++)
                if (parenthesisFor.get("for" + j).equals(i))
                    poliz.toPoliz("!!FOR");
            for (int j = 0; j < parenthesisIf.size(); j++)
                if (parenthesisIf.get("if" + j).equals(i))
                    poliz.toPoliz("!!IF");
            for (int j = 0; j < parenthesisElse.size(); j++)
                if (parenthesisElse.get("else" + j).equals(i))
                    poliz.toPoliz("!!ELSE");
        }

        System.out.println("Вся программа - OK");

        if (checkEnd())
            System.out.println("Футер программы - OK");

        for (String a: poliz.getAll()) {
            ui.polizArea.setText(ui.polizArea.getText() + a + "\n");
            System.out.println(a);
        }
    }

    private boolean checkStart() {
        if (!lexer.letter.get(0).equals("n") && !lexer.text.get(0).equals("int"))
            ui.console.write(Errors.getErrors(0, lexer.line.get(0)));
        else if (!lexer.letter.get(1).equals("k") && !lexer.text.get(1).equals("main"))
            ui.console.write(Errors.getErrors(0, lexer.line.get(1)));
        else if (!lexer.letter.get(2).equals("s") && !lexer.text.get(2).equals("("))
            ui.console.write(Errors.getErrors(0, lexer.line.get(2)));
        else if (!lexer.letter.get(3).equals("s") && !lexer.text.get(3).equals(")"))
            ui.console.write(Errors.getErrors(0, lexer.line.get(3)));
        else if (!lexer.letter.get(4).equals("s") && !lexer.text.get(4).equals("{)}"))
            ui.console.write(Errors.getErrors(0, lexer.line.get(4)));
        else {
            poliz.toPoliz("START");
            return true;
        }

            return false;
    }

    private boolean checkEnd(){
        if (!lexer.letter.get(lexer.letter.size() - 4).equals("k") && !lexer.text.get(lexer.letter.size() - 4).equals("return"))
            ui.console.write(Errors.getErrors(1, lexer.line.get(lexer.letter.size() - 4)));
        else if (!lexer.letter.get(lexer.letter.size() - 3).equals("i") && !lexer.text.get(lexer.letter.size() -3).equals("0"))
            ui.console.write(Errors.getErrors(1, lexer.line.get(lexer.letter.size() - 3)));
        else if (!lexer.letter.get(lexer.letter.size() - 2).equals("s") && !lexer.text.get(lexer.letter.size() -2).equals(";"))
            ui.console.write(Errors.getErrors(1, lexer.line.get(lexer.letter.size() - 2)));
        else if (!lexer.letter.get(lexer.letter.size() - 1).equals("s") && !lexer.text.get(lexer.letter.size() - 1).equals("}"))
            ui.console.write(Errors.getErrors(1, lexer.line.get(lexer.letter.size() - 1)));
        else {
            poliz.toPoliz("END");
            return true;
        }

        return false;
    }

    private void checkInt(int index) {
        if (!lexer.text.get(index - 2).equals("for")) {
            for (String name : varName) {
                if (name.equals(lexer.text.get(index + 1)))
                    ui.console.write(Errors.getErrors(25, lexer.line.get(index + 1)));
            }
        } else {
            if (!poliz.get(poliz.getSize() - 2).contains("=")) {
                poliz.toPoliz("T" + lexer.text.get(index + 1));
                poliz.toPoliz("0");
                poliz.toPoliz("=");
                poliz.toPoliz(lexer.text.get(index + 3));

                varName.add(lexer.text.get(index + 1));
                varType.add("int");
                varValue.add(lexer.text.get(index + 3));
                return;
            }
        }

        String s = "";
        int i = index;
        int line = lexer.line.get(index);
        while (!lexer.text.get(i).equals(";")) {
            if (!lexer.line.get(i).equals(line))
                break;
            s += lexer.text.get(i) + " ";
            i++;
        }
        s += lexer.text.get(i);

        if (s.charAt(s.length() - 1) != ';')
            ui.console.write(Errors.getErrors(4, lexer.line.get(index)));

        if (s.indexOf('=') != -1) {

            if (i - index > 4) {
                String math = "";
                i = index + 3;

                while (!lexer.text.get(i).equals(";")) {
                    math += lexer.text.get(i) + " ";
                    i++;
                }

                if (!math.contains("cin")) {
                    math = math(math, lexer.line.get(i));

                    varName.add(lexer.text.get(index + 1));
                    varValue.add(math);
                    varType.add("int");

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("0");
                    poliz.toPoliz("=");
                    poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                }
                else {
                    poliz.toPoliz("V" + lexer.text.get(index + 1));
                    poliz.toPoliz("0");
                    poliz.toPoliz("=");
                    poliz.toPoliz("R");
                }

            } else {
                String tmp = "";
                i = index + 1;
                while (!lexer.text.get(i).equals(";")) {
                    tmp += lexer.text.get(i) + " ";
                    i++;
                }
                String[] temp = tmp.replaceAll(" ", "").split("=");

                if (temp[0].length() >= 2)
                    ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
                else if (!temp[1].matches("\\d{1,}")) {
                    int ind = varName.indexOf(lexer.text.get(index + 3));
                    if (ind != -1) {
                        if (varType.get(ind).equals("int")) {
                            varName.add(temp[0]);
                            varType.add("int");
                            varValue.add(lexer.text.get(index + 3));

                            poliz.toPoliz("V" + varName.get(varName.size() - 1));
                            poliz.toPoliz("0");
                            poliz.toPoliz("=");
                            poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                        } else
                            ui.console.write(Errors.getErrors(7, lexer.line.get(index)));
                    }
                } else {
                    varName.add(temp[0]);
                    varType.add("int");
                    varValue.add(Integer.valueOf(temp[1]));

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("0");
                    poliz.toPoliz("=");
                    poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                }
            }
        } else {
            String tmp = "";
            i = index;
            while (!lexer.text.get(i).equals(";")) {
                tmp += lexer.text.get(i) + " ";
                i++;
            }

            String[] temp = tmp.split(" ");

            if (temp[1].length() >= 2)
                ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
            else {
                varType.add("int");
                varName.add(temp[1]);
                varValue.add(null);

                poliz.toPoliz("V" + varName.get(varName.size() - 1));
                poliz.toPoliz("0");
                poliz.toPoliz("=");
                poliz.toPoliz("0");
            }
        }
    }

    private void checkDouble(int index){
        if (!lexer.text.get(index - 2).equals("for")) {
            for (String name : varName) {
                if (name.equals(lexer.text.get(index + 1)))
                    ui.console.write(Errors.getErrors(25, lexer.line.get(index + 1)));
            }
        }else {
            poliz.toPoliz("T" + lexer.text.get(index + 1));
            poliz.toPoliz("1");
            poliz.toPoliz("=");
            poliz.toPoliz(lexer.text.get(index + 3));
            varName.add(lexer.text.get(index + 1));
            varType.add("double");
            varValue.add(lexer.text.get(index + 3));
            return;
        }

        String s = "";
        int i = index;
        int line = lexer.line.get(index);
        while (!lexer.text.get(i).equals(";")) {
            if (!lexer.line.get(i).equals(line))
                break;
            s += lexer.text.get(i) + " ";
            i++;
        }
        s += lexer.text.get(i);

        if (s.charAt(s.length() - 1) != ';')
            ui.console.write(Errors.getErrors(4, lexer.line.get(index)));

        if (s.indexOf('=') != -1) {

            if (i - index > 4){
                String math = "";
                i = index + 3;

                while (!lexer.text.get(i).equals(";")) {
                    math += lexer.text.get(i) + " ";
                    i++;
                }

                if (!math.contains("cin")) {
                    math = math(math, lexer.line.get(i));

                    varName.add(lexer.text.get(index + 1));
                    varValue.add(math);
                    varType.add("double");

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("1");
                    poliz.toPoliz("=");
                    poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                }
                else {
                    poliz.toPoliz("V" + lexer.text.get(index + 1));
                    poliz.toPoliz("1");
                    poliz.toPoliz("=");
                    poliz.toPoliz("R");
                }
            }
            else {
                String tmp = "";
                i = index + 1;
                while (!lexer.text.get(i).equals(";")) {
                    tmp += lexer.text.get(i) + " ";
                    i++;
                }
                String[] temp = tmp.replaceAll(" ", "").split("=");

                if (temp[0].length() >= 2)
                    ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
                else if (!temp[1].matches("\\d{1,}[.]\\d{1,}")) {
                    int ind = varName.indexOf(lexer.text.get(index + 3));
                    if (ind != -1){
                        if (varType.get(ind).equals("double")) {
                            varName.add(temp[0]);
                            varType.add("double");
                            varValue.add(lexer.text.get(index + 3));

                            poliz.toPoliz("V" + varName.get(varName.size() - 1));
                            poliz.toPoliz("1");
                            poliz.toPoliz("=");
                            poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                        }
                        else
                            ui.console.write(Errors.getErrors(7, lexer.line.get(index)));
                    }
                }
                else{
                    varName.add(temp[0]);
                    varType.add("double");
                    varValue.add(Double.valueOf(temp[1]));

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("1");
                    poliz.toPoliz("=");
                    poliz.toPoliz(varValue.get(varValue.size() - 1).toString());
                }
            }
        } else {
            String tmp = "";
            i = index;
            line = lexer.line.get(index);
            while (!lexer.text.get(i).equals(";")) {
                if (line != lexer.line.get(i))
                    ui.console.write(Errors.getErrors(4, lexer.line.get(i)));
                tmp += lexer.text.get(i) + " ";
                i++;
            }

            String[] temp = tmp.split(" ");

            if (temp[1].length() >= 2)
                ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
            else {
                varType.add("double");
                varName.add(temp[1]);
                varValue.add(null);

                poliz.toPoliz("V" + varName.get(varName.size() - 1));
                poliz.toPoliz("1");
                poliz.toPoliz("=");
                poliz.toPoliz("0.0");
            }
        }
    }

    private void checkString(int index){
        for (String item : varName) {
            if (item.equals(lexer.text.get(index + 1)))
                ui.console.write(Errors.getErrors(25, lexer.line.get(index + 1)));
        }

        String s = "";
        int i = index;
        int line = lexer.line.get(index);
        while (!lexer.text.get(i).equals(";")) {
            if (!lexer.line.get(i).equals(line))
                break;
            s += lexer.text.get(i) + " ";
            i++;
        }

        s += lexer.text.get(i);

        if (s.charAt(s.length() - 1) != ';')
            ui.console.write(Errors.getErrors(4, lexer.line.get(index)));

        if (s.indexOf('=') != -1) {

            if (i - index > 4){
                String value = "";
                i = index + 3;

                while (!lexer.text.get(i).equals(";")) {
                    value += lexer.text.get(i) + " ";
                    i++;
                }

                if (!value.contains("cin")){

                    if (!lexer.text.get(index + 3).equals("\"") || !lexer.text.get(index + 5).equals("\""))
                        ui.console.write(Errors.getErrors(5, lexer.line.get(index)));

                    varName.add(lexer.text.get(index + 1));
                    varType.add("string");
                    varValue.add(lexer.text.get(index + 4));

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("2");
                    poliz.toPoliz("=");
                    poliz.toPoliz("S" + varValue.get(varValue.size() - 1).toString());
                }
                else {
                    varName.add(lexer.text.get(index + 1));
                    varType.add("string");
                    varValue.add(value);

                    poliz.toPoliz("V" + lexer.text.get(index + 1));
                    poliz.toPoliz("2");
                    poliz.toPoliz("=");
                    poliz.toPoliz("R");

                }
            }
            else {
                if (lexer.text.get(index + 1).length() >= 2)
                    ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
                else if (!lexer.text.get(index + 3).equals("\"") && lexer.text.get(index + 3).matches("\\w")) {
                    int ind = varName.indexOf(lexer.text.get(index + 3));
                    if (ind != -1){
                        if (varType.get(ind).equals("string")) {
                            varName.add(lexer.text.get(index + 1));
                            varType.add("string");
                            varValue.add(lexer.text.get(index + 3));

                            poliz.toPoliz("V" + varName.get(varName.size() - 1));
                            poliz.toPoliz("2");
                            poliz.toPoliz("=");
                            poliz.toPoliz("S" + varValue.get(varValue.size() - 1).toString());
                        }
                        else
                            ui.console.write(Errors.getErrors(7, lexer.line.get(index)));
                    }
                }
                else{
                    varName.add(lexer.text.get(index + 1));
                    varType.add("string");
                    varValue.add(lexer.text.get(index + 4));

                    poliz.toPoliz("V" + varName.get(varName.size() - 1));
                    poliz.toPoliz("2");
                    poliz.toPoliz("=");
                    poliz.toPoliz("S" + varValue.get(varValue.size() - 1).toString());
                }
            }
        } else {
            String tmp = "";
            i = index;
            line = lexer.line.get(index);
            while (!lexer.text.get(i).equals(";")) {
                if (line != lexer.line.get(i))
                    ui.console.write(Errors.getErrors(4, lexer.line.get(i)));
                tmp += lexer.text.get(i) + " ";
                i++;
            }

            String[] temp = tmp.split(" ");

            if (temp[1].length() >= 2)
                ui.console.write(Errors.getErrors(3, lexer.line.get(index)));
            else {
                varType.add("string");
                varName.add(temp[1]);
                varValue.add(null);

                poliz.toPoliz("V" + varName.get(varName.size() - 1));
                poliz.toPoliz("2");
                poliz.toPoliz("=");
                poliz.toPoliz("S\"\"");
            }
        }
    }

    private void checkFor(int index) {
        poliz.toPoliz("!FOR");
        if (!lexer.text.get(index + 1).equals("(") && !lexer.letter.get(index + 1).equals("s"))
            ui.console.write(Errors.getErrors(8, lexer.line.get(index + 1)));

        if ((lexer.text.get(index + 2).equals("int") || lexer.text.get(index + 2).equals("double"))
                && (lexer.letter.get(index + 2).equals("n") || lexer.letter.get(index + 2).equals("d"))) {

            if (lexer.text.get(index + 2).equals("int"))
                checkInt(index + 2);
            else if (lexer.text.get(index + 2).equals("double"))
                checkDouble(index + 2);

            if (!lexer.text.get(index + 6).equals(";") && !lexer.letter.get(index + 6).equals("s"))
                ui.console.write(Errors.getErrors(10, lexer.line.get(index + 6)));

            int i = varName.indexOf(lexer.text.get(index + 7));
            int j = varName.indexOf(lexer.text.get(index + 9));

            if (i == -1)
                if (j == -1)
                    ui.console.write(Errors.getErrors(6, lexer.line.get(index + 9)));

            String operators = "> < == <= >= !=";
            if (!operators.contains(lexer.text.get(index + 8)))
                ui.console.write(Errors.getErrors(11, lexer.line.get(index + 8)));

            poliz.toPoliz(lexer.text.get(index + 7));
            poliz.toPoliz(lexer.text.get(index + 8));
            poliz.toPoliz(lexer.text.get(index + 9));

            if (!lexer.text.get(index + 10).equals(";") && !lexer.letter.get(index + 10).equals("s"))
                ui.console.write(Errors.getErrors(8, lexer.line.get(index + 10)));

            i = varName.indexOf(lexer.text.get(index + 11));
            j = varName.indexOf(lexer.text.get(index + 13));
            int k = varName.indexOf(lexer.text.get(index + 15));

            if (i == -1) {
                if (j == -1) {
                    if (k == -1) {
                        ui.console.write(Errors.getErrors(8, lexer.line.get(index + 11)));
                    }
                }
            }

            operators = "+ - * /";
            if (!lexer.text.get(index + 12).equals("="))
                ui.console.write(Errors.getErrors(8, lexer.line.get(index + 12)));
            else if (!operators.contains(lexer.text.get(index + 14)))
                ui.console.write(Errors.getErrors(8, lexer.line.get(index + 14)));

            if (!lexer.text.get(index + 16).equals(")") && !lexer.letter.get(index + 16).equals("s"))
                ui.console.write(Errors.getErrors(8, lexer.line.get(index + 16)));

            poliz.toPoliz(lexer.text.get(index + 11));
            poliz.toPoliz(lexer.text.get(index + 13));
            poliz.toPoliz(lexer.text.get(index + 15));
            poliz.toPoliz(lexer.text.get(index + 14));
            poliz.toPoliz(lexer.text.get(index + 12));

            if (!lexer.text.get(index + 17).equals("{") && !lexer.letter.get(index + 17).equals("s"))
                ui.console.write(Errors.getErrors(12, lexer.line.get(index + 17)));


            boolean notEnd = true;
            int count = 0;
            first:
            for (i = index + 17; i < lexer.text.size() - 1; i++) {
                if (lexer.text.get(i).equals("for") || lexer.text.get(i).equals("if") || lexer.text.get(i).equals("else"))
                    count++;
                else if (lexer.text.get(i).equals("}")) {
                    if (count != 0) {
                        count--;
                        continue;
                    }
                    if (parenthesisFor.isEmpty()) {
                        notEnd = false;
                        parenthesisFor.put("for" + parenthesisFor.size(), i);
                        break;
                    } else
                        for (j = 0; j < parenthesisFor.size(); j++) {
                            if (parenthesisFor.get("for" + j) != i) {
                                notEnd = false;
                                parenthesisFor.put("for" + parenthesisFor.size(), i);
                                break first;
                            }
                        }
                }
            }

            if (notEnd)
                ui.console.write(Errors.getErrors(13, lexer.line.get(index + 16)));

            varName.remove(varName.size() - 1);
            varType.remove(varType.size() - 1);
            varValue.remove(varValue.size() - 1);

        } else {
            int i = varName.indexOf(lexer.text.get(index + 2));
            if (i != -1 && (varType.get(i).equals("int") || varType.get(i).equals("double"))) {

                poliz.toPoliz(lexer.text.get(index + 2));
                poliz.toPoliz(lexer.text.get(index + 4));
                poliz.toPoliz(lexer.text.get(index + 3));

                if (!lexer.text.get(index + 5).equals(";") && !lexer.letter.get(index + 5).equals("s"))
                    ui.console.write(Errors.getErrors(10, lexer.line.get(index + 5)));

                String operators = "> < == <= >= !=";
                if (!operators.contains(lexer.text.get(index + 7)))
                    ui.console.write(Errors.getErrors(11, lexer.line.get(index + 7)));

                poliz.toPoliz(lexer.text.get(index + 6));
                poliz.toPoliz(lexer.text.get(index + 7));
                poliz.toPoliz(lexer.text.get(index + 8));

                if (!lexer.text.get(index + 9).equals(";") && !lexer.letter.get(index + 9).equals("s"))
                    ui.console.write(Errors.getErrors(8, lexer.line.get(index + 9)));

                i = varName.indexOf(lexer.text.get(index + 10));
                int j = varName.indexOf(lexer.text.get(index + 12));
                int k = varName.indexOf(lexer.text.get(index + 14));

                if (i == -1) {
                    if (j == -1) {
                        if (k == -1) {
                            ui.console.write(Errors.getErrors(8, lexer.line.get(index + 10)));
                        }
                    }
                }

                operators = "+ - * /";
                if (!lexer.text.get(index + 11).equals("="))
                    ui.console.write(Errors.getErrors(8, lexer.line.get(index + 11)));
                else if (!operators.contains(lexer.text.get(index + 13)))
                    ui.console.write(Errors.getErrors(8, lexer.line.get(index + 13)));

                if (!lexer.text.get(index + 15).equals(")") && !lexer.letter.get(index + 15).equals("s"))
                    ui.console.write(Errors.getErrors(8, lexer.line.get(index + 15)));

                poliz.toPoliz(lexer.text.get(index + 10));
                poliz.toPoliz(lexer.text.get(index + 12));
                poliz.toPoliz(lexer.text.get(index + 14));
                poliz.toPoliz(lexer.text.get(index + 13));
                poliz.toPoliz(lexer.text.get(index + 11));

                if (!lexer.text.get(index + 16).equals("{") && !lexer.letter.get(index + 16).equals("s"))
                    ui.console.write(Errors.getErrors(12, lexer.line.get(index + 16)));


                boolean notEnd = true;
                int count = 0;
                first:
                for (i = index + 17; i < lexer.text.size() - 1; i++) {
                    if (lexer.text.get(i).equals("for") || lexer.text.get(i).equals("if") || lexer.text.get(i).equals("else"))
                        count++;
                    else if (lexer.text.get(i).equals("}")) {
                        if (count != 0) {
                            count--;
                            continue;
                        }
                        if (parenthesisFor.isEmpty()) {
                            notEnd = false;
                            parenthesisFor.put("for" + parenthesisFor.size(), i);
                            break;
                        } else
                            for (j = 0; j < parenthesisFor.size(); j++) {
                                if (parenthesisFor.get("for" + j) != i) {
                                    notEnd = false;
                                    parenthesisFor.put("for" + parenthesisFor.size(), i);
                                    break first;
                                }
                            }
                    }
                }

                if (notEnd)
                    ui.console.write(Errors.getErrors(13, lexer.line.get(index + 15)));
            } else
                ui.console.write(Errors.getErrors(9, lexer.line.get(index + 2)));
        }
    }

    private void checkIf(int index) {
        if (!lexer.text.get(index).equals("if") && !lexer.letter.get(index).equals("k"))
            ui.console.write(Errors.getErrors(14, lexer.line.get(index)));
        else {
            if (!lexer.text.get(index + 1).equals("(") && !lexer.letter.get(index + 1).equals("s"))
                ui.console.write(Errors.getErrors(14, lexer.line.get(index + 1)));

            int line = lexer.line.get(index + 1);
            int i = index + 2;
            String expression = "";

            while (!lexer.text.get(i).equals(")")) {
                if (lexer.line.get(i) != line)
                    ui.console.write(Errors.getErrors(14, lexer.line.get(i)));
                expression += lexer.text.get(i) + " ";
                i++;
            }

            logical(expression, lexer.line.get(i));

            if (!lexer.text.get(i).equals("{") && !lexer.letter.get(i).equals("s"))
                ui.console.write(Errors.getErrors(15, lexer.line.get(i)));

            boolean notEnd = true;
            line = 0;
            int count = 0;
            int j;
first:
            for (j = i; j < lexer.text.size() - 1; j++) {
                line = lexer.line.get(j);
                if (lexer.text.get(j).equals("for") || lexer.text.get(j).equals("if") || lexer.text.get(j).equals("else"))
                    count++;
                else if (lexer.text.get(j).equals("}")) {
                    if (count != 0) {
                        count--;
                    } else if (parenthesisIf.isEmpty()) {
                        notEnd = false;
                        parenthesisIf.put("if" + parenthesisIf.size(), j);
                        break;
                    } else
                        for (int k = 0; k < parenthesisIf.size(); k++) {
                            if (parenthesisIf.get("if" + k) != j) {
                                notEnd = false;
                                parenthesisIf.put("if" + parenthesisIf.size(), j);
                                break first;
                            }
                        }
                }
            }

            if (notEnd)
                ui.console.write(Errors.getErrors(16, lexer.line.get(line)));

            if (lexer.text.get(j + 1).equals("else")) {
                if (!lexer.text.get(j + 2).equals("{"))
                    ui.console.write(Errors.getErrors(18, lexer.line.get(j + 2)));

                notEnd = true;
                line = 0;
                count = 0;
                first:
                for (i = j + 2; i < lexer.text.size() - 1; i++) {
                    line = lexer.line.get(i);
                    if (lexer.text.get(i).equals("for") || lexer.text.get(i).equals("if") || lexer.text.get(i).equals("else"))
                        count++;

                    else if (lexer.text.get(i).equals("}")) {
                        if (count != 0) {
                            count--;
                        } else if (parenthesisElse.isEmpty()) {
                            notEnd = false;
                            parenthesisElse.put("else" + parenthesisElse.size(), i);
                            break;
                        } else
                            for (int k = 0; k < parenthesisElse.size(); k++) {
                                if (parenthesisElse.get("else" + k) != i) {
                                    notEnd = false;
                                    parenthesisElse.put("else" + parenthesisElse.size(), i);
                                    break first;
                                }
                            }
                    }
                }
                if (notEnd)
                    ui.console.write(Errors.getErrors(19, lexer.line.get(line)));
            }
        }
    }

    private void checkCout(int index) {
        if (!lexer.text.get(index).equals("cout") || !lexer.letter.get(index).equals("k"))
            ui.console.write(Errors.getErrors(20, lexer.line.get(index)));

        if (!lexer.text.get(index + 1).equals("("))
            ui.console.write(Errors.getErrors(20, lexer.line.get(index)));

        if (lexer.text.get(index + 2).equals("\"")) {
            if (!lexer.letter.get(index + 3).equals("i"))
                ui.console.write(Errors.getErrors(20, lexer.line.get(index + 3)));
            else if (!lexer.text.get(index + 4).equals("\""))
                ui.console.write(Errors.getErrors(5, lexer.line.get(index + 4)));
            else if (!lexer.text.get(index + 5).equals(")"))
                ui.console.write(Errors.getErrors(20, lexer.line.get(index + 5)));
            else if (!lexer.text.get(index + 6).equals(";"))
                ui.console.write(Errors.getErrors(4, lexer.line.get(index + 6)));

            poliz.toPoliz("W");
            poliz.toPoliz(lexer.text.get(index + 3));
        } else {
            int i = index + 2;
            String cout = "";
            int line = lexer.line.get(index + 2);
            while (!lexer.text.get(i).equals(")")) {
                if (line != lexer.line.get(i))
                    ui.console.write(Errors.getErrors(20, line));
                cout += lexer.text.get(i);
                i++;
            }
            if (cout.length() >= 2) {
                poliz.toPoliz("W");
                poliz.toPoliz(math(cout, lexer.line.get(i)));
            }
            else {
                boolean isVar = false;
                for (String v : varName) {
                    if (v.equals(cout)) {
                        isVar = true;
                        break;
                    }
                }
                if (!isVar)
                    ui.console.write(Errors.getErrors(6, lexer.line.get(index + 2)));
                if (!lexer.text.get(i + 1).equals(";"))
                    ui.console.write(Errors.getErrors(10, line));
                poliz.toPoliz("W");
                poliz.toPoliz(lexer.text.get(i-1));
            }
        }
    }

    private void checkCin(int index){
            if (!lexer.text.get(index).equals("cin") && !lexer.letter.get(index).equals("k"))
                ui.console.write(Errors.getErrors(21, lexer.line.get(index)));
            else if (!lexer.text.get(index + 1).equals("(") && !lexer.letter.get(index + 1).equals("s"))
                ui.console.write(Errors.getErrors(21, lexer.line.get(index + 1)));
            else if (!lexer.text.get(index + 2).equals(")") && !lexer.letter.get(index + 2).equals("s"))
                ui.console.write(Errors.getErrors(21, lexer.line.get(index + 2)));
            else if (!lexer.text.get(index + 3).equals(";") && !lexer.letter.get(index + 3).equals("s"))
                ui.console.write(Errors.getErrors(4, lexer.line.get(index + 3)));
    }

    private int checkLabel(int index){
        if (!lexer.letter.get(index - 1).equals("i"))
            ui.console.write(Errors.getErrors(22, lexer.line.get(index)));
        else if (labels.size() == 0) {
            labels.add(lexer.text.get(index - 1));
            poliz.toPoliz("L" + labels.get(labels.size() - 1));
            return index;
        }
        else {
            for (String label: labels) {
                if (label.equals(lexer.text.get(index - 1)))
                    ui.console.write(Errors.getErrors(23, lexer.line.get(index - 1)));
            }
            labels.add(lexer.text.get(index - 1));
            poliz.toPoliz("L" + labels.get(labels.size() - 1));
            return index;
        }
        return 0;
    }

    private void checkGoto(int index) {
        boolean isReal = false;
        for (String label : labels)
            if (label.equals(lexer.text.get(index + 1))) {
                isReal = true;
                break;
            }
        if (!isReal)
            ui.console.write(Errors.getErrors(24, lexer.line.get(index + 1)));
        else if (!lexer.text.get(index + 2).equals(";"))
            ui.console.write(Errors.getErrors(4, lexer.line.get(index + 2)));

        poliz.toPoliz("G" + lexer.text.get(index + 1));
    }

    //todo не проверяется на мутацию, если там больше символом - скорее всего ошибка
    private void checkOther(int index) {
        //проверяем на мутации переменных
        for (int i = 0; i < varName.size(); i++)
            if (varName.get(i).equals(lexer.text.get(index)))
                if (lexer.text.get(index + 1).equals("=")) {
                    if (lexer.text.get(index - 1).equals(";")) {
                        //если изменяется переменная типа int
                        System.out.println(varType.get(i));
                        if (varType.get(i).equals("int")) {
                            //если значение переменной явно указано
                            if (lexer.text.get(index + 2).matches("\\d{1,}") && lexer.text.get(index + 3).equals(";")) {
                                varValue.set(i, lexer.text.get(index + 2));
                                poliz.toPoliz(varName.get(i));
                                poliz.toPoliz(lexer.text.get(index + 2));
                                poliz.toPoliz("=");
                            }
                            //если значение переменной необходимо вычислить
                            else if (!lexer.text.get(index + 3).equals(";") && !lexer.text.get(index + 2).equals("cin")) {
                                int j = index;
                                String math = "";
                                while (!lexer.text.get(j).equals(";")) {
                                    math += lexer.text.get(j) + " ";
                                    j++;
                                }

                                math = math(math, lexer.line.get(i));
                                System.out.println(math);

                                poliz.toPoliz(varName.get(i));
                                poliz.toPoliz(math);
                                poliz.toPoliz("=");
                            }
                            //если значение переменной необходимо считать с консоли
                            else {
                                poliz.toPoliz(varName.get(i));
                                poliz.toPoliz("R");
                                poliz.toPoliz("=");
                            }
                        } else
                            //если изменяется переменная типа double
                            if (varType.get(i).equals("double")) {
                                //если значение переменной явно указано
                                if (lexer.text.get(index + 2).matches("\\d{1,}[.]\\d{1,}") && lexer.text.get(index + 3).equals(";")) {
                                    varValue.set(i, lexer.text.get(index + 2));
                                    poliz.toPoliz(varName.get(i));
                                    poliz.toPoliz(lexer.text.get(index + 2));
                                    poliz.toPoliz("=");
                                }
                                //если значение переменной необходимо вычислить
                                else if (!lexer.text.get(index + 3).equals(";") && !lexer.text.get(index + 2).equals("cin")) {
                                    int j = index;
                                    String math = "";
                                    while (!lexer.text.get(j).equals(";")) {
                                        math += lexer.text.get(j) + " ";
                                        j++;
                                    }

                                    math = math(math, lexer.line.get(i));
                                    System.out.println(math);

                                    poliz.toPoliz(varName.get(i));
                                    poliz.toPoliz(math);
                                    poliz.toPoliz("=");
                                }
                                //если значение переменной необходимо считать с консоли
                                else {
                                    poliz.toPoliz(varName.get(i));
                                    poliz.toPoliz("R");
                                    poliz.toPoliz("=");
                                }
                            }

                    }
                }

    }

    private String math(String text, int line){
        String[] tokens = text.replaceAll("\\+", " + ")
                .replaceAll("-", " - ")
                .replaceAll("/", " / ")
                .replaceAll("\\*", " * ")
                .split(" ");

        for (String s : tokens) {
            if (s.matches("\\w{1,}") && !s.matches("\\d{1,}[.]\\d{1,}|\\d{1,}")){
                if (s.length() >= 2)
                    ui.console.write(Errors.getErrors(3, line));
                boolean isReal = false;
                for (String var: varName) {
                    if (var.equals(s)) {
                        isReal = true;
                        break;
                    }
                }
                if (!isReal)
                    ui.console.write(Errors.getErrors(6, line));
            }
        }

        List<String> result = ExpressionParser.parse(text);

        return result.toString();
    }

    private boolean logical(String expression, int line) {
        String[] words = expression.split(" ");
        String operators = "> < <= >= != ! == || &&";
        String separators = "( )";
        boolean isFine = false;
        for (String w : words) {
            if (!operators.contains(w) && !separators.contains(w) && !w.matches("[1-9]")) {
                for (String v : varName) {
                    if (v.equals(w)) {
                        isFine = true;
                        break;
                    }
                }
            } else
                isFine = true;
        }
        if (!isFine)
            ui.console.write(Errors.getErrors(17, line));

        return true;
    }

    public Poliz getPoliz(){
        return poliz;
    }
}
/*
 * todo условия
 */