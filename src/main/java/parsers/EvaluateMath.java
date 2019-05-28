package parsers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class EvaluateMath {
    private static final Set<Character> operator = new HashSet<>(Arrays.asList('+','-','*','x','^','/'));
    private static boolean parenEnd = false;
    private static StringBuilder strBuild = new StringBuilder();
    private static Stack<Double> values = new Stack<>();
    private static Stack<Character> ops = new Stack<>();

    //Evaluates math expressions.
    public static double evaluateMath(String expression) throws ArithmeticException, IllegalArgumentException {
        char[] tokens = expression.toCharArray();
        int tokenLength = tokens.length;

        for (char i : tokens) {
            tokenLength--;
            if(i == '-' ){
                negativeValue(i,tokens);
            }
            else if ((i >= '0' && i <= '9') || i == '.') {
                isNumberOrDecimal(i,tokenLength);
            }
            else if (i == '(') {
                parenthesisStart(i);
            }
            else if (i == ')') {
                parenthesisEnd();
            }
            else if (operator.contains(i)) {
                isOperator(i);
            }
        }

        while (!ops.empty()) {
            values.push(calcOp(ops.pop(), values.pop(), values.pop()));
            if(!values.empty()){
                maxValueRange(values);
            }
        }

        return values.pop();
    }

    private static void isOperator(char i){
        if(!parenEnd){
            values.push(Double.parseDouble(strBuild.toString()));
        }

        strBuild = new StringBuilder();
        parenEnd = false;

        while (!ops.empty() && precedence(i, ops.peek())) {
            values.push(calcOp(ops.pop(), values.pop(), values.pop()));
        }
        ops.push(i);
    }
    private static void parenthesisStart(char i){
        if(parenEnd){
            strBuild = new StringBuilder();
            parenEnd = false;
            ops.push('*');
        }

        else if(!values.empty() && ops.empty()){
            values.push(Double.parseDouble(strBuild.toString()));
            strBuild = new StringBuilder();

            ops.push('*');
        }

        if(!strBuild.toString().equals("")){
            values.push(Double.parseDouble(strBuild.toString()));
            ops.push('*');
        }

        strBuild = new StringBuilder();
        ops.push(i);
    }

    private static void parenthesisEnd(){
        values.push(Double.parseDouble(strBuild.toString()));
        strBuild = new StringBuilder();
        parenEnd = true;

        while (ops.peek() != '(') {
            values.push(calcOp(ops.pop(), values.pop(), values.pop()));
        }
        ops.pop();
    }

    private static void isNumberOrDecimal(char i, int tokenLength){
        if(parenEnd){
            strBuild = new StringBuilder();
            parenEnd = false;
            while (!ops.empty() && precedence('*', ops.peek())) {
                values.push(calcOp(ops.pop(), values.pop(), values.pop()));
            }
            ops.push('*');
        }

        if (tokenLength == 0){
            strBuild.append(i);
            values.push(Double.parseDouble(strBuild.toString()));
        }
        else{
            strBuild.append(i);
        }
    }

    private static void negativeValue(char i, char[] tokens){
        if(i == tokens[0]){
            strBuild.append(i);
        }

        else if(!ops.empty() && (ops.peek() == '*' || ops.peek() == '(' || ops.peek() == '/' || ops.peek() == '^' || ops.peek() == 'x')){
            values.push(Double.parseDouble(strBuild.toString()));
            strBuild = new StringBuilder();

            while (!ops.empty() && precedence(i, ops.peek())) {
                values.push(calcOp(ops.pop(), values.pop(), values.pop()));
            }
            ops.push(i);
        }
    }

    private static void maxValueRange(Stack<Double> values){
        if(values.peek() > Double.MAX_VALUE){
            throw new IllegalArgumentException("Result is out of bounds.");
        }
        else if (values.peek() < -Double.MAX_VALUE){
            throw new IllegalArgumentException("Result is out of bounds.");
        }
    }

    private static boolean precedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') {
            return false;
        }
        if (op1 == '^'){
            return !((op2 == '*' || op2 == '/' || op2 == '+' || op2 == '-'));
        }

        return !((op1 == '*' || op1 == '/' || op1 == 'x') && (op2 == '+' || op2 == '-'));
    }

    private static double calcOp(char op, double b, double a) {
        switch (op) {
            case '+':
                return a + b;
            case '-':
                return a - b;
            case '*':
                return a * b;
            case 'x':
                return a * b;
            case '/':
                if (b == 0) {
                    throw new ArithmeticException("Cannot divide by zero");
                }
                return a / b;
            case '^':
                return Math.pow(a, b);
        }
        return 0;
    }
}
