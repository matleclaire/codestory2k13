package fr.mleclaire.java.codestory.jajascript;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//@XmlRootElement
public class Flight {
  //  @XmlElement(name = "VOL")
    @JsonProperty("VOL")
	private String name;
  //  @XmlElement(name = "DEPART")
    @JsonProperty("DEPART")
	private int start;
 //   @XmlElement(name = "DUREE")
    @JsonProperty("DUREE")
    private int time;
 //   @XmlElement(name = "PRIX")
    @JsonProperty("PRIX")
    private int price;
	
    public Flight() {

    }

	public Flight(String name, int start, int time, int price) {
		this.name = name;
		this.start = start;
		this.time = time;
		this.price = price;
	}
	
	
	public String getName() {
		return name;
	}

	public int getStart() {
		return start;
	}

	public int getTime() {
		return time;
	}

	public int getPrice() {
		return price;
	}

	public double getRate() {
		return price/time;
	}

	public int getEnd() {
		return start+time;
	}

    public boolean equals(Object o) {
        if (o instanceof Flight) {
            return name.equals(((Flight) o).getName());
        } else {
            return false;
        }
    }
	
}
