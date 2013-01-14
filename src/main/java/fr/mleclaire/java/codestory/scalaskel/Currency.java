package fr.mleclaire.java.codestory.scalaskel;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
	
	public static final int FOO_RATE = 1;
    public static final int BAR_RATE = 7;
    public static final int QIX_RATE = 11;
    public static final int BAZ_RATE = 21;

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
     * find the best way to exchange cents into (foo, bar, qix and baz)
     * @param c
     * @return list with size of 1 which contains the best way
     */
	public static List<Currency> optimalExchange(Currency c) {
		List<Currency> result = new LinkedList<Currency>();
		
		if (c.cent == 0) {
			result.add(c);
		} else {
            Currency ch= null;
			if (c.cent/ BAZ_RATE >0  ) {
                int value;
                if (c.baz == null) value = 0; else value = c.baz;
			    ch = new Currency(c.cent-BAZ_RATE, c.foo, c.bar, c.qix, value+1);
			} else if (c.cent/ QIX_RATE >0 ) {
                int value;
                if (c.qix == null) value = 0; else value = c.qix;
				ch = new Currency(c.cent-QIX_RATE, c.foo, c.bar, value+1, c.baz);
            } else if (c.cent/ BAR_RATE >0 ) {
                int value;
                if (c.bar == null) value = 0; else value = c.bar;
				ch = new Currency(c.cent-BAR_RATE, c.foo, value+1, c.qix, c.baz);
			} else if (c.cent/ FOO_RATE >0 ) {
                int value;
                if (c.foo == null) value = 0; else value = c.foo;
				ch = new Currency(c.cent-FOO_RATE, value+1, c.bar, c.qix, c.baz);
			}
            result.addAll(optimalExchange(ch));
        }
		return result;
	}

    /**
     * find all ways to exchange cents into (foo, bar, qix and baz)
     * with no duplicates
     * @param c
     * @return
     */
    public static HashSet<Currency> exchange(Currency c) {
        HashSet<Currency> result = new HashSet<Currency>();

        if (c.cent == 0) {
            result.add(c);
        } else {
            Currency ch= null;


            if (c.cent/ FOO_RATE >0 ) {
                int value;
                if (c.foo == null) value = 0; else value = c.foo;
                ch = new Currency(c.cent- FOO_RATE, value+1, c.bar, c.qix, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange(ch));
            }
            if (c.cent/ BAR_RATE >0 ) {
                int value;
                if (c.bar == null) value = 0; else value = c.bar;
                ch = new Currency(c.cent- BAR_RATE, c.foo, value+1, c.qix, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange(ch));
            }
            if (c.cent/ QIX_RATE >0 ) {
                int value;
                if (c.qix == null) value = 0; else value = c.qix;
                ch = new Currency(c.cent- QIX_RATE, c.foo, c.bar, value+1, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange(ch));
            }
            if (c.cent/ BAZ_RATE >0  ) {
                int value;
                if (c.baz == null) value = 0; else value = c.baz;
                ch = new Currency(c.cent- BAZ_RATE, c.foo, c.bar, c.qix, value+1);
                if (!result.contains(ch))
                    result.addAll(exchange(ch));
            }

        }
        return result;
    }


    /**
     * find all ways to exchange cents into (foo, bar, qix and baz)
     * with no duplicates
     * @param c
     * @return
     */
    public static HashSet<Currency> exchange2(Currency c) {
        HashSet<Currency> result = new HashSet<Currency>();

        if (c.cent == 0) {
            result.add(c);
        } else {
            Currency ch= null;


            if (c.cent/ FOO_RATE >0 ) {
                int value;
                if (c.foo == null) value = 0; else value = c.foo;
                ch = new Currency(c.cent- FOO_RATE, value+1, c.bar, c.qix, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange2(ch));
            }
            if (c.cent/ BAR_RATE >0 ) {
                int value;
                if (c.bar == null) value = 0; else value = c.bar;
                ch = new Currency(c.cent- BAR_RATE, c.foo, value+1, c.qix, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange2(ch));
            }
            if (c.cent/ QIX_RATE >0 ) {
                int value;
                if (c.qix == null) value = 0; else value = c.qix;
                ch = new Currency(c.cent- QIX_RATE, c.foo, c.bar, value+1, c.baz);
                if (!result.contains(ch))
                    result.addAll(exchange2(ch));
            }
            if (c.cent/ BAZ_RATE >0  ) {
                int value;
                if (c.baz == null) value = 0; else value = c.baz;
                ch = new Currency(c.cent- BAZ_RATE, c.foo, c.bar, c.qix, value+1);
                if (!result.contains(ch))
                    result.addAll(exchange2(ch));
            }

        }
        return result;
    }


    /**
     * return always "1" to force HashSet to call equals method
     */
    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Currency) {
            return this.cent == ((Currency) o).cent
                    && this.foo == ((Currency) o).foo
                    && this.bar == ((Currency) o).bar
                    && this.qix == ((Currency) o).qix
                    && this.baz == ((Currency) o).baz ;
        } else return false;
    }


    public String toString() {
        return "["+foo+", "+bar+", "+qix+", "+baz+"]";
    }

}
