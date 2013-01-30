package fr.mleclaire.codestory.jajascript;

import org.codehaus.jackson.annotate.JsonProperty;

public class Flight implements Comparable<Flight>{
    @JsonProperty("VOL")
	private String name;
    @JsonProperty("DEPART")
	private int start;
    @JsonProperty("DUREE")
    private int time;
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

    @Override
    public int compareTo(Flight f) {
        if ( start < f.getStart() ) {
            return -1;
        } else if (start > f.getStart()  || (start == f.getStart() && price >= f.getPrice())) {
            return 1;
        } else {
            return 0;
        }
    }
}
