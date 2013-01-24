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
				maxEndTime = f.getEnd();
				sortedList.add(f);
			}
			else {
				// Ajout en tete
				if (f.getStart() < minStartTime) { 
					sortedList.addFirst(f);
					minStartTime = f.getStart();
				} 
				// Ajout en queue
				else if (f.getEnd() > maxEndTime) { 
					sortedList.addLast(f);
					maxEndTime = f.getEnd();
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
							if (f.getRate() > f2.getRate()) {
								it.set(f);
								it.add(f2);
							} else {
								it.add(f);
							}
							break; 
						}
					}
				}
			}
		}
		
		return sortedList;
	}
	
	public static Candidate optimize(List<Flight> list) {
		list = sort(list);
		List<Candidate> candList = new LinkedList<Candidate>();
		Candidate best = null;
		for (Flight f : list) {
			if (candList.size() == 0) {
				best = new Candidate();
				best.addLast(f);
				candList.add(best);	
			} else {
				for (ListIterator<Candidate> it = candList.listIterator(); it.hasNext();) {
					Candidate c = it.next();
					if (c.getPath().getLast().getEnd() <= f.getStart() ) {
						c.addLast(f);
                        if (c.getGain() > best.getGain() ) {
                            best = c;
                        }
					} else {
						Candidate c2 = c.clone();
						while (c2.getPath().size() > 0 &&  c2.getPath().getLast().getEnd() > f.getStart() ) {
							c2.removeLast();
						}
						c2.addLast(f);
						it.add(c2);
                        if (c2.getGain() > best.getGain() ) {
                            best = c2;
                        }
					}

				}
			}
		}
		return best;
	}
}
