package fr.mleclaire.java.codestory.jajascript;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement
public class Candidate {
    @XmlElement
	private int gain;
    @XmlTransient
	private LinkedList<Flight> path;

    private Flight secondToLast;

	public Candidate() {
		this.gain = 0;
		this.path = new LinkedList<Flight>();
	}
	
	public Candidate(int gain, LinkedList<Flight> path) {
		this.gain = gain;
		this.path = path;
	}

	public void addLast(Flight f) {
		if ( path.size() == 0
				||  f.getStart()>= path.getLast().getEnd()) { // getLast()  est en O(1) car liste doublement chain�e

            if (path.size() > 0 ) secondToLast = path.getLast();

            path.addLast(f);
			gain+=f.getPrice();
		}
	}

    @XmlTransient
    public Flight getLast() {
        return this.path.getLast();
    }

    @XmlTransient
    public Flight getSecondToLast() {
        return this.secondToLast;
    }

	public int getGain() {
		return gain;
	}
	
	public void removeLast() {
		gain-=path.getLast().getPrice();
		path.removeLast();
	}
	
	public LinkedList<Flight> getPath() {
		return path;
	}

	@Override
    public Candidate clone() {
		Candidate c = new Candidate();
		c.gain = this.gain;
		c.path = (LinkedList<Flight>) this.path.clone();
		return c;
	}

    @Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append(gain);
		sb.append(" [");
		for (Flight f : path) {
			sb.append(f.getName()).append(" ");
		}
		sb.append(" ]");
		return sb.toString();
	}

    public boolean equals(Object o) {
        if (o instanceof Candidate) {
           return (((Candidate) o).getPath().equals(this.getPath()));
        }  else return super.equals(o);
    }

    @XmlElement(name = "path")
    public List<String> getJSONPath() {
        List<String> jsonPath = new ArrayList<String>();
        for (Flight f : this.path) {
            jsonPath.add(f.getName());
        }
        return jsonPath;
    }
}
