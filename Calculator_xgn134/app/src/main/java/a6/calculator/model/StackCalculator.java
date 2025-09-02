package a6.calculator.model;

import java.util.LinkedList;
import java.lang.Math;

public class StackCalculator extends LinkedList<Integer> {

    public void add() throws NotEnoughArgumentsException, OverflowException {
        checkArgs();
        try {
            int b = pop();
            int a = pop();
            push(Math.addExact(a, b));
        } catch (ArithmeticException e) {
            throw new OverflowException();
        }
    }

    public void subtract() throws NotEnoughArgumentsException, OverflowException {
        checkArgs();
        try {
            int b = pop();
            int a = pop();
            push(Math.subtractExact(a, b));
        } catch (ArithmeticException e) {
            throw new OverflowException();
        }
    }

    public void multiply() throws NotEnoughArgumentsException, OverflowException {
        checkArgs();
        try {
            int b = pop();
            int a = pop();
            push(Math.multiplyExact(a, b));
        } catch (ArithmeticException e) {
            throw new OverflowException();
        }
    }

    public void divide() throws NotEnoughArgumentsException, OverflowException, DivisionByZeroException {
        checkArgs();
        int b = pop();
        if (b == 0) throw new DivisionByZeroException();
        int a = pop();
        push(a / b);
    }

    private void checkArgs() throws NotEnoughArgumentsException {
        if (size() < 2) throw new NotEnoughArgumentsException();
    }
}