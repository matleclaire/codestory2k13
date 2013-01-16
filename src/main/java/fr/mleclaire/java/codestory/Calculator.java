package fr.mleclaire.java.codestory;


import net.java.dev.eval.Expression;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Calculator {

    public static BigDecimal compute(String exp)  {
        BigDecimal result = null ;
        Expression e = new Expression(exp);
        result  = e.eval();
        return result;
    }

}
