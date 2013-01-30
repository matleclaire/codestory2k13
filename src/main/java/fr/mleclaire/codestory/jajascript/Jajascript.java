package fr.mleclaire.codestory.jajascript;

import java.util.*;

/**
 * <b>Algorithm</b>
 *
 * First, we sort Flights by asc order (start time) with a TreeSet.
 * We iterate on it  and we build a list of candidates.
 * After each flight, we filter candidates to keep only potentiels solutions and remove poor candidates
 *
 *
 */
public class Jajascript {


    /**
     * Find the ultimate response for jajascript airLines
     * @param list, payload (not sorted)
     * @return solution
     */
	public static Candidate optimize(List<Flight> list) {
		Collections.sort(list);

        Candidate best = null;

        TreeSet<Candidate> candList = new TreeSet<Candidate>();

        Iterator<Flight>itFlight = list.iterator();
        while (itFlight.hasNext()) {
            Flight f = itFlight.next();
            if (candList.size() == 0) {
                best = new Candidate();
                best.addLast(f);
                candList.add(best);
            } else {

                List<Candidate> toAdd = new LinkedList<Candidate>();
                for (Iterator<Candidate> it = candList.iterator(); it.hasNext();) {
                    Candidate c = it.next();

                    // if F can be add in queue, we do!
                    if (c.getLast().getEnd() <= f.getStart() ) {
                        c.addLast(f);
                        if (c.getGain() > best.getGain() ) {
                            best = c;
                        }
                    }
                    // else, candidate is duplicated and we try to insert f inside (at the end)
                    else {
                        //  before duplicate, we try without cloning (because clone is expensive! $$$ )
                        int gain = c.getGain();
                        int end ;

                        if (c.getPath().size() > 0 &&  c.getLast().getEnd() > f.getStart()) {
                            gain-= c.getLast().getPrice();
                        }
                        gain += f.getPrice();
                        end = f.getEnd();

                        // if candidate seems to be lucky ( aka best gain and smallest or equal path)
                        if (gain >= best.getGain()  || (best.getPath().size() > 0 && end <= best.getLast().getEnd()))  {
                            Candidate c2 = c.clone();   // we clone it !
                            if (c.getPath().size() > 0 &&  c.getLast().getEnd() > f.getStart()) {
                                c2.removeLast();
                            }
                            c2.addLast(f);

                            if (!toAdd.contains(c2) && !candList.contains(c2)) {
                                if (c2.getGain() > best.getGain()) {
                                    best = c2;
                                }
                                toAdd.add(c2);
                            }
                        }
                    }
                }

                // Add new candidates
                if (toAdd.size() > 0) {
                    candList.addAll(toAdd);
                }


                // clean up poor candidates
                 Iterator<Candidate> it = candList.iterator();
                 Candidate last = it.next();
                 while(it.hasNext()) {
                     Candidate c = it.next();
                     // Magic. Don't touch! 
                     if (c.equals(last)
                        ||(  c.getPath().size() > 1 && last.getPath().size() > 1   // Compare to last
                             && c.getSecondToLast().equals(last.getSecondToLast())
                             && c.getGain() < last.getGain()
                             && c.getLast().getEnd() >= last.getLast().getEnd()
                             )
                        ||(  c.getPath().size() > 1 && best.getPath().size() > 1  // Compare to Best
                             && c.getSecondToLast().equals(best.getSecondToLast())
                             && c.getGain() < best.getGain()
                             && c.getLast().getEnd() >= best.getLast().getEnd()
                        )) {
                         it.remove();
                     } else {
                         last = c;
                     }
                 }
            }
        }
        return best;
    }
           
}
