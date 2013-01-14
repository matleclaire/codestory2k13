package fr.mleclaire.java.codestory.scalaskel;

import java.util.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represent currencies and allow to exchange them :
 * Cents into foo, bar, qix, baz
 *
 * We use Integer to omit null values in JSON (with JAXB)
 */
@XmlRootElement
public class Currency {

    private static final int[] RATE = {1, 7, 11 ,21}; // Foo, Bar, Qix, Baz

    // in - to change
    private Integer cent = 0;

    // out - result
    @XmlElement
    private Integer foo;
    @XmlElement
    private Integer bar;
    @XmlElement
    private Integer qix;
    @XmlElement
    private Integer baz;



    public Currency(int cent) {
        this.cent = cent;
    }

    private Currency(int cent, Integer foo, Integer bar, Integer qix, Integer baz) {
        this.cent = cent;
        this.foo = foo;
        this.bar = bar;
        this.qix = qix;
        this.baz = baz;
    }


    /**
     * find all ways to exchange cents into (foo, bar, qix and baz)
     * with no duplicates
     * @param c
     * @return
     */
    public static List<Currency> exchange(Currency c) {
        return exchange(c, RATE.length-1);
    }


    private static List<Currency> exchange(Currency c, int ind) {
        List<Currency> result = new LinkedList<Currency>();
        // Stop condition
        if (c.cent == 0) {
            result.add(c);
        } else {
            if (ind >= 0 &&  c.cent/RATE[ind] >= 0) {
                for (int i=(c.cent/RATE[ind]); i>=0; i--) {
                    Currency ch = null ;
                    switch (ind) {
                        case 3: // Baz
                            ch = new Currency(c.cent-(i*RATE[ind]), c.foo, c.bar, c.qix, i == 0 ? null:i);
                            break;
                        case 2: // Qix
                            ch = new Currency(c.cent-(i*RATE[ind]), c.foo, c.bar, i == 0 ? null:i, c.baz);
                            break;
                        case 1: // Bar
                            ch = new Currency(c.cent-(i*RATE[ind]), c.foo, i == 0 ? null:i, c.qix, c.baz);
                            break;
                        case 0: // Foo
                            ch = new Currency(c.cent-(i*RATE[ind]), i == 0 ? null:i, c.bar, c.qix, c.baz);
                            break;
                    }
                    result.addAll(exchange(ch, ind-1));
                }
            }
        }
        return result;
    }
}
