package ui.math.calculator;

import ui.table.CellValue;
import ui.table.SpreadSheetModel;
import ui.util.CellPointer;

import java.util.Scanner;

/**
 * @author Dmitriy Tseyler
 */

public class MathCalculator implements Calculator {
    private Lexem lexem;
    private Lexer lexer;
    private final SpreadSheetModel model;

    public MathCalculator(SpreadSheetModel model) {
        this.model = model;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        MathCalculator mathCalculator = new MathCalculator(new SpreadSheetModel(10, 10));
        double d = mathCalculator.calculate(expression);
        System.out.println(d);
    }

    public double calculate(String expression) {
        lexer = new Lexer(expression.toUpperCase(), model);
        lexem = lexer.nextLexem();
        return expression();
    }


    private double expression() {
        double result = composed();

        while (lexem == Lexem.PLUS || lexem == Lexem.MINUS) {
            Lexem tempLexem = lexem;
            lexem = lexer.nextLexem();
            result += tempLexem.getResult(composed());
        }
        return result;
    }

    private double composed() {
        double result = sign();
        while (lexem == Lexem.DIV || lexem == Lexem.MULT) {
            Lexem tempLexem = lexem;
            lexem = lexer.nextLexem();
            result = tempLexem.getResult(result, sign());
        }
        return result;
    }

    private double power() {
        double result = multiplier();
        while (lexem == Lexem.POW) {
            lexem = lexer.nextLexem();
            result = Math.pow(result, power());
        }
        return result;
    }

    private double sign() {
        if (lexem == Lexem.PLUS || lexem == Lexem.MINUS){
            Lexem tempLexem = lexem;
            lexem = lexer.nextLexem();
            return tempLexem.getResult(power());
        } else {
            return power();
        }
    }

    private double multiplier() {
        Lexem tempLexem = lexem;
        switch (lexem.getType()) {
            case C: {
                CellPointer pointer = lexer.getCellPointer();
                lexem = lexer.nextLexem();
                CellValue value = (CellValue)model.getValueAt(pointer.getRow(), pointer.getColumn());
                return Double.parseDouble(value.getRendererValue());
            } case NUMBERS: {
                double num = lexer.getNumber();
                lexem = lexer.nextLexem();
                return num;
            } case FUNC:{
                lexem = lexer.nextLexem();
                double temp = tempLexem.getResult(expression());
                lexem = lexer.nextLexem();
                return temp;
            } case OPERATION:{
                lexem = lexer.nextLexem();
                return tempLexem.getResult(multiplier());
            }
        }
        return 0;
    }
}
