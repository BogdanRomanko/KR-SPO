import java.util.Stack;

public class Lexer {

    private String[] separators = {",", ";", "{", "}", "(", ")", "\""};
    private String[] operators = {"+", "-", "*", "/", "=", "!", ">", "<", "==", "<=", ">=", "!=", "||", "&&", ":"};
    private String[] keyWords = {"if", "else", "for", "goto", "cin", "cout", "main", "return"};
    private String[] comments = {"/*", "*/", "//"};
    private String[] types = {"int", "double", "string"};

    private int k = 0; //key words
    private int s = 0; //separator
    private int i = 0; //identifier
    private int l = 0; //string
    private int n = 0; //int
    private int d = 0; //double
    private int o = 0; //operator

    public Stack <String> letter;
    public Stack <Integer> position;
    public Stack <String> text;
    public Stack <Integer> line;

    private int LineId = 0;

    public Lexer(String program) {

        letter = new Stack<>();
        position = new Stack<>();
        text = new Stack<>();
        line = new Stack<>();

        String[] lines = program.
                replaceAll("\n", " \n")
                .replaceAll("\\+", " + ")
                .replaceAll("-", " - ")
                .replaceAll("\\\\", " \\")
                .replaceAll("<", " <")
                .replaceAll(">", " >")
                .split("\n");

        String buffer = "";
        char[] tempLines;
        boolean isStr = false;
        boolean isComm = false;


        for (int i = 0; i < lines.length; i++) {
            tempLines = lines[i].toCharArray();
            LineId = i;


            for (int j = 0; j < lines[i].length(); j++) {
                second:
                {
                    char second;

                    if (String.valueOf(tempLines[j]).matches("\\d|\\w|[.]"))
                        buffer += tempLines[j];
                    else {

                        if (j + 1 >= tempLines.length)
                            second = ' ';
                        else
                            second = tempLines[j + 1];

                        while (isStr) {
                            if (tempLines[j] != '"') {
                                buffer += tempLines[j];
                                break second;
                            } else {
                                isStr = false;
                                checkOther(buffer);
                                System.out.println("[IDENTIFIER] - " + buffer);

                                if (checkSeparators(String.valueOf(tempLines[j])))
                                    System.out.println("[SEPARATOR] - " + tempLines[j]);

                                buffer = "";
                                break second;
                            }
                        }

                        int comm = checkComments(tempLines[j], second);
                        if (comm == 1) {
                            System.out.println("[COMMENT LINE] - " + tempLines[j] + second);
                            break;
                        } else if (comm == 2) {
                            System.out.println("[COMMENT START] - " + tempLines[j] + second);
                            buffer = "";
                            j += 2;
                            isComm = true;
                        } else if (comm == 3) {
                            System.out.println("[COMMENT END] - " + tempLines[j] + second);
                            j += 2;
                            isComm = false;
                            buffer = "";
                            break second;
                        }

                        if (isComm)
                            break second;

                        int type = checkType(buffer);
                        if (checkKeyWords(buffer))
                            System.out.println("[KEYWORD] - " + buffer);
                        else if (type == 1)
                            System.out.println("[INTEGER] - " + buffer);
                        else if (type == 2)
                            System.out.println("[DOUBLE] - " + buffer);
                        else if (type == 3)
                            System.out.println("[STRING] - " + buffer);
                        else if (checkOther(buffer) == 2)
                            System.out.println("[IDENTIFIER] - " + buffer);

                        int oper = checkOperators(tempLines[j], second);
                        if (oper == 2) {
                            System.out.println("[OPERATOR] - " + tempLines[j] + second);
                            buffer = "";
                            break second;
                        } else if (oper == 1)
                            System.out.println("[OPERATOR] - " + tempLines[j]);
                        else if (checkSeparators(String.valueOf(tempLines[j])))
                            System.out.println("[SEPARATOR] - " + tempLines[j]);

                        if (tempLines[j] == '"')
                            isStr = true;

                        buffer = "";
                    }

                }
            }

        }

        /*for (int i = 0; i < letter.size(); i++)
            System.out.println(text.get(i) + " " + letter.get(i) + " " + position.get(i));

        for (int i = 0; i < letter.size(); i++)
            System.out.print(letter.get(i) + position.get(i) + " ");*/

        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
    }

    private boolean checkKeyWords(String word) {
        for (String keyWord : keyWords)
            if (word.equals(keyWord)) {
                letter.push("k");
                position.push(k);
                k++;
                text.push(word);
                line.push(LineId);
                return true;
            }

        return false;
    }

    private boolean checkSeparators(String word) {
        for (String separator : separators)
            if (word.equals(separator)) {
                letter.push("s");
                position.push(s);
                s++;
                text.push(word);
                line.push(LineId);
                return true;
            }

        return false;
    }

    private int checkType(String word) {
        if (word.equals(types[0])) {
            letter.push("n");
            position.push(n);
            n++;
            text.push(word);
            line.push(LineId);
            return 1;
        } else if (word.equals(types[1])) {
            letter.push("d");
            position.push(d);
            d++;
            text.push(word);
            line.push(LineId);
            return 2;
        } else if (word.equals(types[2])) {
            letter.push("l");
            position.push(l);
            l++;
            text.push(word);
            line.push(LineId);
            return 3;
        }

        return 0;
    }

    private int checkOperators(char word, char nextChar) {
        for (String operator : operators)
            if (operator.length() == 2)
                if (word == operator.charAt(0) && nextChar == operator.charAt(1)) {
                    letter.push("o");
                    position.push(o);
                    text.push(operator);
                    line.push(LineId);
                    o++;
                    return 2;
                }

        for (String operator : operators)
            if (String.valueOf(word).equals(operator)) {
                letter.push("o");
                position.push(o);
                text.push(operator);
                line.push(LineId);
                o++;
                return 1;
            }


        return 0;
    }

    private int checkComments(char word, char nextChar) {
        if (word == comments[2].charAt(0) && nextChar == comments[2].charAt(1))
            return 1;
        else if (word == comments[0].charAt(0) && nextChar == comments[0].charAt(1))
            return 2;
        else if (word == comments[1].charAt(0) && nextChar == comments[1].charAt(1))
            return 3;

        return 0;
    }

    private int checkOther(String word) {
        if (word.length() > 0) {

            letter.push("i");
            position.push(i);
            i++;
            text.push(word);
            line.push(LineId);
            return 2;
        }
        return 0;
    }

}