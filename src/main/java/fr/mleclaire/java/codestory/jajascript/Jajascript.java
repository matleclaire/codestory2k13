package fr.mleclaire.java.codestory.jajascript;

import java.util.*;


public class Jajascript {
	
	public static int maxStartTime;
    public static Candidate best;
	
    /**
	 * retourne une liste des vols triés par (heure,taux de rendement)
	 * @param list
	 * @return
	 */
	public static Candidate optimize(List<Flight> list) {
		Collections.sort(list);

        best = null;

        TreeSet<Candidate> candList = new TreeSet<Candidate>();

        Iterator<Flight>itFlight = list.iterator();
        int count = 0;
        while (itFlight.hasNext()) {
            Flight f = itFlight.next();
            count++;
            if (candList.size() == 0) {
                best = new Candidate();
                best.addLast(f);
                candList.add(best);
            } else {
                // Just for debug
                if (count%1000 == 0) System.out.println(count+" size :"+candList.size());

                List<Candidate> toAdd = new LinkedList<Candidate>();
                for (Iterator<Candidate> it = candList.iterator(); it.hasNext();) {
                    Candidate c = it.next();

                    // Si F peut être ajouté à la fin du candidat, on l'ajoute
                    if (c.getLast().getEnd() <= f.getStart() ) {
                        c.addLast(f);
                        if (c.getGain() > best.getGain() ) {
                            best = c;
                        }
                    }
                    // Sinon on duplique le candidat et on essaie d'insérer le vol dedans (à la place du dernier element)
                    else {
                        //  avant de dupliquer, on teste sans cloner l'objet pour savoir si ça vaut le coup de dépenser de l'énergie à cloner (car couteux!!) :)
                        int gain = c.getGain();
                        int end = c.getLast().getEnd();

                        if (c.getPath().size() > 0 &&  c.getLast().getEnd() > f.getStart()) {
                           // c2.removeLast();
                            gain-= c.getLast().getPrice();
                            if (c.getSecondToLast() != null) {
                                end = c.getSecondToLast().getEnd();
                            }  else {
                                end = 0;
                            }
                        }
                        gain += f.getPrice();
                        end = f.getEnd();

                        // Si le candidat est retenu ( = meilleur gain que le best OU plus petit)
                        if (gain >= best.getGain()  || (best.getPath().size() > 0 && end <= best.getLast().getEnd()))  {

                            Candidate c2 = c.clone();
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


                // clean up bad candidates
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
