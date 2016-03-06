package math.calculator;

import ui.table.SpreadSheetModel;
import ui.util.CellPointer;
import ui.util.Util;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    private String expression;
    private int pointer;
    private String number;
    private final SpreadSheetModel model;
    private CellPointer cellPointer;

    public Lexer(String expression, SpreadSheetModel model){
        this.model = model;
        this.expression = expression.replaceAll(" ", "");
        pointer = 0;
        number = "";
    }

    public Lexeme nextLexem(){
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
                flow = expression.charAt(++pointer);
                String column = "";
                String row = "";
                while (Character.isAlphabetic(flow)) {
                    column += flow;
                    flow = expression.charAt(++pointer);
                }
                while (Character.isDigit(flow)) {
                    row += flow;
                    if (pointer + 1 < expression.length()) {
                        flow = expression.charAt(++pointer);
                    } else {
                        pointer++;
                        break;
                    }
                }
                cellPointer = new CellPointer(Integer.parseInt(row), Util.getColumnFromString(column));
            } else {
                pointer += lexeme.getOffset();
            }
            return lexeme;
        }
        return null;
    }

    public CellPointer getCellPointer() {
        return cellPointer;
    }

    public double getNumber(){
        return Double.parseDouble(number);
    }
}
