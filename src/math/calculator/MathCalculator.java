package math.calculator;

import ui.table.CellValue;
import ui.table.SpreadSheetModel;
import ui.util.CellPointer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Dmitriy Tseyler
 */

public class MathCalculator implements Calculator {
    private Lexeme lexeme;
    private Lexer lexer;
    private final SpreadSheetModel model;
    private final List<CellPointer> pointers;

    public MathCalculator(SpreadSheetModel model) {
        pointers = new ArrayList<>();
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
        pointers.clear();
        lexeme = lexer.nextLexem();
        return expression();
    }

    private double expression() {
        double result = composed();

        while (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result += tempLexeme.getResult(composed());
        }
        return result;
    }

    public List<CellPointer> getPointers() {
        return pointers;
    }

    public void reset() {
        pointers.clear();
    }

    private double composed() {
        double result = sign();
        while (lexeme == Lexeme.DIV || lexeme == Lexeme.MULT) {
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            result = tempLexeme.getResult(result, sign());
        }
        return result;
    }

    private double power() {
        double result = multiplier();
        while (lexeme == Lexeme.POW) {
            lexeme = lexer.nextLexem();
            result = Math.pow(result, power());
        }
        return result;
    }

    private double sign() {
        if (lexeme == Lexeme.PLUS || lexeme == Lexeme.MINUS){
            Lexeme tempLexeme = lexeme;
            lexeme = lexer.nextLexem();
            return tempLexeme.getResult(power());
        } else {
            return power();
        }
    }

    private double multiplier() {
        Lexeme tempLexeme = lexeme;
        switch (lexeme.getType()) {
            case C: {
                CellPointer pointer = lexer.getCellPointer();
                pointers.add(pointer);
                lexeme = lexer.nextLexem();
                CellValue value = (CellValue)model.getValueAt(pointer.getRow(), pointer.getColumn());
                return Double.parseDouble(value.getRendererValue());
            } case NUMBERS: {
                double num = lexer.getNumber();
                lexeme = lexer.nextLexem();
                return num;
            } case FUNC:{
                lexeme = lexer.nextLexem();
                double temp = tempLexeme.getResult(expression());
                lexeme = lexer.nextLexem();
                return temp;
            } case OPERATION:{
                lexeme = lexer.nextLexem();
                return tempLexeme.getResult(multiplier());
            }
        }
        return 0;
    }
}
