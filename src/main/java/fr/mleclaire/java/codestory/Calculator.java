package fr.mleclaire.java.codestory;


import net.java.dev.eval.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Calculator {

    // To prevent bad characters
    public final static String VALID_PATTERN = "([0-9+\\-*()\\./])*";


    public static BigDecimal compute(String exp)  {
        BigDecimal result = null ;
        if (exp.matches(VALID_PATTERN)) {
            Expression e = new Expression(exp);
            result  = e.eval();
        }
        return result;
    }

}
