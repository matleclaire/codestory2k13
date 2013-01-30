package fr.mleclaire.codestory.calc;


import net.java.dev.eval.Expression;

public class Calculator {

    public static String compute(String exp)  {
        String result = null ;
        Expression e = new Expression(exp);
        result  = e.eval().stripTrailingZeros().toPlainString();

        return result.replace(".",","); // format string
    }

}
