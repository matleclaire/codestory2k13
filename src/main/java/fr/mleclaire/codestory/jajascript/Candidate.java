package fr.mleclaire.codestory.jajascript;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;

public class Candidate implements Comparable<Candidate> {
    @JsonProperty("gain")
	private int gain;
    @JsonProperty("path")
	private ArrayList<String> path; // ArrayList better than LinkedList when using clone 

    @JsonIgnore
    private Flight secondToLast, last;

	public Candidate() {
		this.gain = 0;
		this.path = new ArrayList<String>();
	}

	public void addLast(Flight f) {
		if ( last == null ||  f.getStart()>= last.getEnd()) {
            if (last != null ) secondToLast = last;

            path.add(f.getName());
			gain+=f.getPrice();
            last = f;
		}
	}


    public Flight getLast() {
        return last;
    }

    public Flight getSecondToLast() {
        return this.secondToLast;
    }

	public int getGain() {
		return gain;
	}

	public void removeLast() {
		gain-=last.getPrice();
		path.remove(path.size()-1);
        last = secondToLast;
	}
	
	public ArrayList<String> getPath() {
		return path;
	}
    
	@Override
    public Candidate clone() {
		Candidate c = new Candidate();
		c.gain = this.gain;
		c.path = (ArrayList<String>)path.clone();//new ArrayList(c.path.size());
    //    c.path = new LinkedList<Flight>();
    //    c.path.addAll(this.path);
        c.secondToLast = this.secondToLast;
        c.last = last;
		return c;
	}

    @Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append(gain);
		sb.append(" [");
		for (String f : path) {
			sb.append(f).append(" ");
		}
		sb.append(" ]");
		return sb.toString();
	}

    /**
     * Candidates are equals if having same Gain and same last flight
     */
    public boolean equals(Object o) {
        if (o instanceof Candidate) {
           return ((Candidate) o).getGain()  == this.getGain() && (((Candidate) o).getLast().equals(this.getLast()));
        }  else return super.equals(o);
    }

    /**
     * Sort Candidate 
     */
    @Override
    public int compareTo(Candidate c) {
        // equals (or worse than "c")
        if ( this.equals(c)
             || (
                path.size() > 1 && c.getPath().size() > 1
                && secondToLast.equals(c.getSecondToLast())
                && gain < c.getGain()  && last.getEnd() >= c.getLast().getEnd()
                )

        ) {

            return 0; // Return 0 to not add element to the set 
        }
        // Compare
        else if (( path.size() > 1 && c.getPath().size() > 1 && secondToLast.equals(c.getSecondToLast()) && gain > c.getGain() ) ) {
            return -1;
        }  else {
            return 1;
        }
    }
}
