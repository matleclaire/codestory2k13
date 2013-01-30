package fr.mleclaire.java.codestory;


import net.java.dev.eval.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Calculator {

    public static String compute(String exp)  {
        String result = null ;
        Expression e = new Expression(exp);
        result  = e.eval().stripTrailingZeros().toPlainString();

        return result.replace(".",","); // format string
    }

}
