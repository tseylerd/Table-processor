package ui.math.calculator;

/**
 * @author Dmitriy Tseyler
 */
public class Lexer {
    private String expression;
    private int pointer;
    private String number;

    public Lexer(String expression){
        this.expression = expression.replaceAll(" ", "");
        pointer = 0;
        number = "";
    }

    public Lexem nextLexem(){
        if (pointer < expression.length()){
            char flow = expression.charAt(pointer);
            Lexem lexem = Lexem.getLexem(flow);
            if (lexem == Lexem.NUM){
                number = "";
                while (Character.isDigit(flow) || flow=='.'|| flow == 'E' || (number.charAt(number.length()-1) =='E' ? (flow == '-' || flow=='+') : false)) {
                    number += flow;
                    pointer++;
                    if (pointer < expression.length())
                        flow = expression.charAt(pointer);
                    else break;
                }
                return lexem;
            }else{
                pointer += lexem.getOffset();
                return lexem;
            }
        }
        return null;
    }
    public double getNumber(){
        return Double.parseDouble(number);
    }
}
