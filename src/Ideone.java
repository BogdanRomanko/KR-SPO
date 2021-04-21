import java.util.*;
import java.lang.*;

class Ideone {

    public static Double calc(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<Double>();
        for (String x : postfix) {
            switch (x) {
                case "sqrt":
                    stack.push(Math.sqrt(stack.pop()));
                    break;
                case "cube":
                    Double tmp = stack.pop();
                    stack.push(tmp * tmp * tmp);
                    break;
                case "pow10":
                    stack.push(Math.pow(10, stack.pop()));
                    break;
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a - b);
                    break;
                }
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a / b);
                    break;
                }
                case "u-":
                    stack.push(-stack.pop());
                    break;
                default:
                    stack.push(Double.valueOf(x));
                    break;
            }
        }
        return stack.pop();
    }

    public static void Main() {
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        ExpressionParser n = new ExpressionParser();
        List<String> expression = ExpressionParser.parse(s);
        boolean flag = ExpressionParser.flag;
        if (flag) {
            for (String x : expression) System.out.print(x + " ");
            System.out.println();
            System.out.println(calc(expression));
        }
    }
}