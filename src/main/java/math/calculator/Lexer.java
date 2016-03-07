package math.calculator;

import cells.CellPointer;
import cells.CellRange;
import util.Util;

import java.text.ParseException;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    private String expression;
    private int pointer;
    private String number;
    private CellPointer cellPointer;
    private CellRange range;
    private AggregateFunction function;

    public Lexer(String expression){
        this.expression = expression.replaceAll(" ", "");
        pointer = 0;
        number = "";
    }

    public Lexeme nextLexem() throws ParseException {
        if (pointer < expression.length()){
            char flow = expression.charAt(pointer);
            Lexeme lexeme = Lexeme.getLexem(flow);
            if (lexeme == Lexeme.NUM){
                number = "";
                while (Character.isDigit(flow) || flow=='.'|| flow == 'E' || number.charAt(number.length()-1) =='E' && (flow == '-' || flow=='+')) {
                    number += flow;
                    pointer++;
                    if (pointer < expression.length())
                        flow = expression.charAt(pointer);
                    else break;
                }
            } else if (lexeme == Lexeme.CELL) {
                pointer++;
                cellPointer = readCellPointer();
            } else if (lexeme == Lexeme.AMPERSAND) {
                pointer++;
                String function = readLiteral();
                pointer++;
                pointer++;
                CellPointer begin = readCellPointer();
                pointer++;
                pointer++;
                CellPointer end = readCellPointer();
                range = new CellRange(begin, end);
                this.function = AggregateFunction.getFunction(function);
                pointer++;
                return lexeme;
            } else {
                pointer += lexeme.getOffset();
            }
            return lexeme;
        }
        return null;
    }

    public CellRange getRange() {
        return range;
    }

    public AggregateFunction getFunction() {
        return function;
    }

    private CellPointer readCellPointer() {
        char flow = expression.charAt(pointer);
        StringBuilder column = new StringBuilder();
        StringBuilder row = new StringBuilder();
        while (Character.isAlphabetic(flow)) {
            column.append(flow);
            flow = expression.charAt(++pointer);
        }
        while (Character.isDigit(flow)) {
            row.append(flow);
            if (pointer + 1 < expression.length()) {
                flow = expression.charAt(++pointer);
            } else {
                pointer++;
                break;
            }
        }
        return new CellPointer(Integer.parseInt(row.toString()), Util.getColumnFromString(column.toString()));
    }

    private String readLiteral() {
        char flow = expression.charAt(pointer);
        StringBuilder builder = new StringBuilder();
        while (Character.isAlphabetic(flow) && pointer + 1 < expression.length()) {
            builder.append(flow);
            flow = expression.charAt(++pointer);
        }
        return builder.toString();
    }

    public CellPointer getCellPointer() {
        return cellPointer;
    }

    public String getNumber(){
        return number;
    }
}
