package fr.mleclaire.java.codestory.jajascript;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class Jajascript {
	
	public static int minStartTime;
	public static int maxEndTime;
	

	/**
	 * retourne une liste des vols triés par (heure,taux de rendement)
	 * @param list
	 * @return
	 */
	private static List<Flight> sort(List<Flight> list) {
		LinkedList<Flight> sortedList = new LinkedList<Flight>();
		for (Flight f : list) {
			if (sortedList.size() == 0)  {
				minStartTime = f.getStart();
				maxEndTime = f.getStart();
				sortedList.add(f);
			}
			else {
				// Ajout en tete
				if (f.getStart() < minStartTime) { 
					sortedList.addFirst(f);
					minStartTime = f.getStart();
				} 
				// Ajout en queue
				else if (f.getStart() > maxEndTime) {
					sortedList.addLast(f);
					maxEndTime = f.getStart();
				} 
				// Ajout au milieu
				else { 
					for (ListIterator<Flight> it = sortedList.listIterator(); it.hasNext();){
						Flight f2 = it.next();
						// Le vol commence avant 
						if (f.getStart()< f2.getStart()) {
							it.set(f);
							it.add(f2); 
							break; 
						} 
						// Le vol commence à la même heure
						else if (f.getStart() ==  f2.getStart()) {
							if (f.getPrice() > f2.getPrice()) {
								it.set(f);
								it.add(f2);
							} else {
								it.add(f);
							}
							break;
                        // Le vol commence pendant
						} else if (f.getStart() >  f2.getStart() && ! it.hasNext() ) {
                            it.add(f);
                            break;
                        }
					}
				}
			}
		}
		
		return sortedList;
	}
	
	public static Candidate optimize(List<Flight> list) {
		list = sort(list);     // On trie par ordre croissant les vols
		List<Candidate> candList = new LinkedList<Candidate>();
		Candidate best = null;
		for (Flight f : list) {
			if (candList.size() == 0) {
				best = new Candidate();
				best.addLast(f);
				candList.add(best);	
			} else {
              //  System.out.println("size :"+candList.size());
				for (ListIterator<Candidate> it = candList.listIterator(); it.hasNext();) {
					Candidate c = it.next();

                    // Si le candidat n'est plus considéré comme solution possible, on le supprime
                    if (c.getGain() < best.getGain()
                            && c.getPath().getLast().getEnd() >= best.getPath().getLast().getEnd())    {
               //         it.remove();
                    }
                    // Sinon si F peut être ajouté à la fin du candidat, on l'ajoute
                    if (c.getPath().getLast().getEnd() <= f.getStart() ) {
						c.addLast(f);
                        if (c.getGain() > best.getGain() ) {
                            best = c;
                        }
					}
                    // Sinon on duplique le candidat et on essaie d'insérer le vol dedans (en supprimant ceux qui gène)
                    else {
						Candidate c2 = c.clone();

						while (c2.getPath().size() > 0 &&  c2.getPath().getLast().getEnd() > f.getStart() ) {
							c2.removeLast();
						}
						c2.addLast(f);


                        // On garde la solution que  si elle n'existe pas déjà déjà dans la liste

                        if (c2.getPath().getLast().getEnd() <= c.getPath().getLast().getEnd() || c2.getGain() >= c.getGain() )  {
                            boolean found = false;
                            for(Candidate current : candList) {
                                if ((c2.equals(current))
                                        || (c2.getPath().size() > 1 && current.getPath().size() > 1
                                            && c2.getSecondToLast().equals(current.getSecondToLast())
                                            && c2.getGain() < current.getGain()  && c2.getLast().getEnd() >= current.getLast().getEnd()
                                            )
                                        ) {
                                    found = true;
                                    break;
                                }
                            }
                            if (!found) {
                                if (c2.getGain() >= c.getGain() ) {        // On trie pour avoir le meilleur gain des 2 en premier (pour le clean up)
                                    it.set(c2);
                                    it.add(c);
                                } else {
                                    it.add(c2);
                                }
                                if (c2.getGain() >= best.getGain()  ) {
                                    best = c2;
                                }
                            }
                        }
					}
				}

                // clean up
                Candidate last = null;
                for (ListIterator<Candidate> it = candList.listIterator(); it.hasNext();) {
                    Candidate c = it.next();
                    if (last == null) {
                        last = c;
                    } else {
                       if (c.getPath().size() > 1 && best.getPath().size() > 1
                           && c.getSecondToLast().equals(best.getSecondToLast())
                           && c.getPath().getLast().getEnd() >= best.getPath().getLast().getEnd() ) {
                           if (c.getGain() < best.getGain()) {
                                it.remove();
                           }

                       } else    {
                            last = c;
                       }
                    }

                }


			}
		}
		return best;
	}
}
