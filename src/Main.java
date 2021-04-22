public class Main {

    public static void main(String[] args) {
        Lexer lexer;

//        String pr = "int main()\n" +
//                    "{\n" +
//                    "m:\n" +
//                    "    int a;\n" +
//                    "    double b = 4.56;\n" +
//                    "    int p = cin();\n" +
//                    "    string s = cin();\n" +
//                    "    double f = 1 + 2 * (3.15 + 14 / 2 - (1 + 2)) * 2 + 1;\n" +
//                    "    int d = 1;\n" +
//                    "    double r = 14.252525;\n" +
//                    "    int c = a * b;\n" +
//                    "    string t = \"Привет, я значение переменной string\";\n" +
//                    "    /*\n" +
//                    "    Это блочный комментарий и его не выведет\n" +
//                    "    строка 1\t\n" +
//                    "    строка 2\n" +
//                    "    строка 3\n" +
//                    "    */\n" +
//                    "    //Циклы тоже надо проверить\n" +
//                    "    for (int i = 0; i < 5; i = i+1)\n" +
//                    "    {\n" +
//                    "        if(i > 2)\n" +
//                    "        {\n" +
//                    "            cout(i);\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "    for (int i = 0; i < 5; i = i+1)\n" +
//                    "    {\n" +
//                    "        if(i > 2)\n" +
//                    "        {\n" +
//                    "            for(int l = 5; l < 10; l = l+1)\n" +
//                    "            { \n"+
//                    "                cout(i+45);\n" +
//                    "            }\n" +
//                    "        } " +
//                    "        else \n" +
//                    "        { \n" +
//                    "            cout(\"232432\"); \n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "    goto m;\n" +
//                    "    string e;\n" +
//                    "    return 0;\n" +
//                    "}";

        String pr = "int main(){\n" +
                "int a = 12 + 7 + 8;\n"+
                "int b = cin();\n" +
                "a = cin();\n" +
                "double c = cin();\n" +
                "string d = cin();\n" +
                " cout(\"HELLO, LYOHA EBAT'\");\n" +
                " cout(\"HOW ARE YOU?\");\n" +
                "return 0;\n" +
                "}";

        lexer = new Lexer(pr);
        Syntax syntax = new Syntax(lexer);
        Interpreter interpreter = new Interpreter(syntax.getPoliz());
        interpreter.start();
    }
}
