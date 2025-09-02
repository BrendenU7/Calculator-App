package a6.calculator.controller;

import a6.calculator.model.*;

public interface Operation {
    void apply() throws NotEnoughArgumentsException, OverflowException, DivisionByZeroException;
}