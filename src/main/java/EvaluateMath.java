import java.util.Stack;

public class EvaluateMath {
    //Evaluates math expressions.
    public static int evaluateMath(String expression)
    {
        Stack<Integer> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        char[] tokens = expression.toCharArray();

        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i] >= '0' && tokens[i] <= '9') {
                StringBuilder strBuild = new StringBuilder();
                while (i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9') {
                    strBuild.append(tokens[i++]);
                    System.out.println(values + " val1");
                }
                i--;
                values.push(Integer.parseInt(strBuild.toString()));
                System.out.println(values + " val2");
            }
            else if (tokens[i] == '(') {
                ops.push(tokens[i]);
            }
            else if (tokens[i] == ')') {
                while (ops.peek() != '(') {
                    values.push(calcOp(ops.pop(), values.pop(), values.pop()));
                    System.out.println(values + " val3");
                }
                ops.pop();
            }
            else if (tokens[i] == '+' || tokens[i] == '-' || tokens[i] == '*' || tokens[i] == '/') {
                while (!ops.empty() && precedence(tokens[i], ops.peek())) {
                    values.push(calcOp(ops.pop(), values.pop(), values.pop()));
                    System.out.println(values + " val4");
                }
                ops.push(tokens[i]);
            }
        }
        while (!ops.empty()) {
            values.push(calcOp(ops.pop(), values.pop(), values.pop()));
        }
        System.out.println(values + " val5");
        return values.pop();
    }

    private static boolean precedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        return !((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-'));
    }

    private static int calcOp(char op, int b, int a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new UnsupportedOperationException("Cannot divide by zero");
                }
                return a / b;
        }
        return 0;
    }
}
