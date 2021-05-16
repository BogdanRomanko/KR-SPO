import java.util.Stack;

public class Lexer {

    /*
     * Массивы с ключевыми словами, разделителями и прочим для
     * ЯП Small C++
     */
    private String[] separators = {",", ";", "{", "}", "(", ")", "\""};
    private String[] operators = {"+", "-", "*", "/", "=", "!", ">", "<", "==", "<=", ">=", "!=", "||", "&&", ":"};
    private String[] keyWords = {"if", "else", "for", "goto", "cin", "cout", "main", "return"};
    private String[] comments = {"/*", "*/", "//"};
    private String[] types = {"int", "double", "string"};

    /*
     * Счётчики определённых лексических токенов ЯП
     */
    private int k = 0; //key words
    private int s = 0; //separator
    private int i = 0; //identifier
    private int l = 0; //string
    private int n = 0; //int
    private int d = 0; //double
    private int o = 0; //operator

    /*
     * Стэки, хранящие в себе лексические токены, позиции токенов,
     * текст и номера линий
     */
    public Stack <String> letter;
    public Stack <Integer> position;
    public Stack <String> text;
    public Stack <Integer> line;

    private int LineId = 0;

    /*
     * Поле, хранящее ссылку на интерфейс пользователя
     * для корректной работы с текстовым полем лексического анализатора
     * и лексической таблицы
     */
    private UI ui;

    /*
     * Конструктор класса, инициализирующий интерфейс пользователя
     * и запускающий проверку полученной программы
     */
    public Lexer(String program, UI ui) {
        this.ui = ui;

        letter = new Stack<>();
        position = new Stack<>();
        text = new Stack<>();
        line = new Stack<>();

        /*
         * Разбиваем переданный текст на массив линий
         */
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

        /*
         * В основном цикле проходимся по всем строкам программы
         */
        for (int i = 0; i < lines.length; i++) {
            /*
             * Разбиваем строку на массив символов
             */
            tempLines = lines[i].toCharArray();
            LineId = i;

            /*
             * По каждому символу строки проходимся отдельным циклом
             */
            for (int j = 0; j < lines[i].length(); j++) {
                second:
                {
                    char second;
                    char pastChar = ' ';
                    /*
                     * Если символ является буквой, цифрой или знаком точка,
                     * то записываем его в буфер
                     */
                    if (String.valueOf(tempLines[j]).matches("\\d|\\w|[.]"))
                        buffer += tempLines[j];
                    else {

                        /*
                         * Если следующий символ от текущего является последниим,
                         * дополнительный символ обнуляем
                         */
                        if (j + 1 >= tempLines.length)
                            second = ' ';
                        /*
                         * Иначе считываем в дополнительный символ следующий
                         * от текущего
                         */
                        else
                            second = tempLines[j + 1];

                        /*
                         * Если предыдущий символ хотя бы первый символ строки
                         */
                        if (j - 1 >= 0)
                            pastChar = tempLines[j - 1];


                        /*
                         * Если текущий символ не равен символу двойной кавычки,
                         * то записываем его в буффер и возвращаемся назад на метку second
                         */
                        while (isStr) {
                            if (tempLines[j] != '"') {
                                buffer += tempLines[j];
                                break second;
                            }
                            /*
                             * Иначе проверяем буфер с помощью метода checkOther, выводим на экран (консоль)
                             * буфер, очищаем его и вовзращаемся на метку second
                             */
                            else {
                                isStr = false;
                                checkOther(buffer);

                                System.out.println("[IDENTIFIER] - " + buffer);
                                ui.lexerArea.setText(ui.lexerArea.getText() + "[IDENTIFIER] - " + buffer + "\n");

                                if (checkSeparators(String.valueOf(tempLines[j]))) {
                                    System.out.println("[SEPARATOR] - " + tempLines[j]);
                                    ui.lexerArea.setText(ui.lexerArea.getText() + "[SEPARATOR] - " + tempLines[j] + "\n");
                                }

                                buffer = "";
                                break second;
                            }
                        }

                        /*
                         * Проверяем текущую линию на то, является ли она
                         * комментарием и возвращаем код вида комментария,
                         * где 1 - строчный комментарий;
                         * 2 - начало многострочного комментария;
                         * 3 - конец многострочного комментария
                         */
                        int comm = checkComments(tempLines[j], second);

                        /*
                         * Проверяем на то, какой комментарий был встречен и выводим информацию
                         * на экран (консоль)
                         */
                        if (comm == 1) {
                            System.out.println("[COMMENT LINE] - " + tempLines[j] + second);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[COMMENT LINE] - " + tempLines[j] + second + "\n");
                            break;
                        } else if (comm == 2) {
                            System.out.println("[COMMENT START] - " + tempLines[j] + second);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[COMMENT START] - " + tempLines[j] + second + "\n");
                            buffer = "";
                            j += 2;
                            isComm = true;
                        } else if (comm == 3) {
                            System.out.println("[COMMENT END] - " + tempLines[j] + second);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[COMMENT END] - " + tempLines[j] + second + "\n");
                            j += 2;
                            isComm = false;
                            buffer = "";
                            break second;
                        }

                        /*
                         * Если многострочный комментарий не закончился,
                         * возвращаемся на метку second
                         */
                        if (isComm)
                            break second;

                        /*
                         * Проверяем каким типом данных является буфер, где
                         * 1 - тип данных int
                         * 2 - тип данных double
                         * 3 - тип данных string
                         */
                        int type = checkType(buffer);

                        /*
                         * Проверяем каким ключевым словом является наш буфер, и
                         * является ли. Если да - выводим на экран и очищаем буфер
                         */
                        if (checkKeyWords(buffer)) {
                            System.out.println("[KEYWORD] - " + buffer);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[KEYWORD] - " + buffer + "\n");
                        }

                        /*
                         * В зависимости от типа данных, выводим на экран (консоль) информацию об этом
                         */
                        else if (type == 1){
                            System.out.println("[INTEGER] - " + buffer);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[INTEGER] - " + buffer + "\n");
                        }
                        else if (type == 2){
                            System.out.println("[DOUBLE] - " + buffer);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[DOUBLE] - " + buffer + "\n");
                        }
                        else if (type == 3) {
                            System.out.println("[STRING] - " + buffer);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[STRING] - " + buffer + "\n");
                        }
                        else if (checkOther(buffer) == 2) {
                            System.out.println("[IDENTIFIER] - " + buffer);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[IDENTIFIER] - " + buffer + "\n");
                        }

                        /*
                         * Проверяем каким оператором является буфер, где
                         * 1 - односимвольный оператор
                         * 2 - двусимвольный оператор
                         */
                        int oper = checkOperators(tempLines[j], second, pastChar);

                        /*
                         * В зависимости от типа оператора, выводим информацию на
                         * экран (консоль)
                         */
                        if (oper == 2) {
                            System.out.println("[OPERATOR] - " + tempLines[j] + second);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[OPERATOR] - " + tempLines[j] + second + "\n");
                            buffer = "";
                            break second;
                        } else if (oper == 1) {
                            System.out.println("[OPERATOR] - " + tempLines[j]);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[OPERATOR] - " + tempLines[j] + "\n");
                        }
                        /*
                         * Если не является оператором, проверяем на то, является ли разделителем
                         * и выводим на экран (консоль)
                         */
                        else if (checkSeparators(String.valueOf(tempLines[j]))) {
                            System.out.println("[SEPARATOR] - " + tempLines[j]);
                            ui.lexerArea.setText(ui.lexerArea.getText() + "[SEPARATOR] - " + tempLines[j] + "\n");
                        }

                        /*
                         * Если текущий символ является двойной кавычкой, отмечаем начало
                         * строки
                         */
                        if (tempLines[j] == '"')
                            isStr = true;

                        /*
                         * Очищаем буфер
                         */
                        buffer = "";
                    }

                }
            }

        }

        /*
         * Выводим лексическую таблицу в интерфейсе пользователя
         */
        for (int i = 0; i < letter.size(); i++)
            ui.lexerTableArea.setText(ui.lexerTableArea.getText() + text.get(i) + "\t" + letter.get(i) + "\t" + position.get(i) + "\n");

    }

    /*
     * Метод, проверяющий является ли слово ключевым
     */
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

    /*
     * Метод, проверяющий является ли слово разделителем
     */
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

    /*
     * Метод, проверяющий является ли слово
     * типом данных и каким именно, если является
     */
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

    /*
     * Метод, проверяющий являетя ли слово оператором и
     * двусимвольным или односимвольным, если является
     */
    private int checkOperators(char word, char nextChar, char pastChar) {
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
            if (String.valueOf(word).equals(operator) && pastChar != '=' && pastChar != '>' && pastChar != '<' && pastChar != '!') {
                letter.push("o");
                position.push(o);
                text.push(operator);
                line.push(LineId);
                o++;
                return 1;
            }

        return 0;
    }

    /*
     * Проверяет является ли слово комментарием и каким именно,
     * если является
     */
    private int checkComments(char word, char nextChar) {
        if (word == comments[2].charAt(0) && nextChar == comments[2].charAt(1))
            return 1;
        else if (word == comments[0].charAt(0) && nextChar == comments[0].charAt(1))
            return 2;
        else if (word == comments[1].charAt(0) && nextChar == comments[1].charAt(1))
            return 3;

        return 0;
    }

    /*
     * Проверка на всё остальное (идентификаторы)
     */
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