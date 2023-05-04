import java.io.File;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

public class TreeSetTest {
	/**
	 * holds the tree and an int so that I can get a random word from the tree, this is the 2nd tree
	 * @author me
	 *
	 */
	private class Pair{
		private int ct;
		private TreeSet<String> setofNextWords;
		public Pair() {
			setofNextWords=new TreeSet<>();
			ct=0;
		}
		public void add(String newword) {
			if (setofNextWords.contains(newword))
				return;
			ct++;
			setofNextWords.add(newword);
		}
		public Optional<String> getRandom() {
			if (ct==0)
				return Optional.empty();
			Iterator<String> it = setofNextWords.iterator();
			Random rand = new Random();
			int j = rand.nextInt(ct);
			//upper limit for the random number
			for (int i=0;i<j;i++) {
				//throws unused Strings to garbage collector
				it.next();
			}
			//returns final
			return Optional.of(it.next());
		}
	}
	
	/**
	 *  2x trees instead of 2x linked list is nlogn
	 *  2x linked list is n^2
	 *  hashmap and a linked list is actually probably better, n time complexity
	 */
	private TreeMap<String,Pair> ts;
	private int ct;
	public TreeSetTest(){
		ts=new TreeMap<String,Pair>();
		ct=0;
	}
	
	//adds words to the next 2nd tree using the key
	public void add(String first, String second) {
		if (!ts.containsKey(first)){
			ct++;
			ts.put(first,new Pair());
		}
		ts.get(first).add(second);
	}
	
	//puts all words from a file into the trees
	public void feedFromFile(File f1) {
		try (Scanner sc = new Scanner(f1)) {
			long initial = System.nanoTime();
			String s1 = sc.next();
			String s2="";
			int i=0;
			if (!sc.hasNext())
				throw new Exception("only 1 token inserted");
			while (sc.hasNext()) {
				s2=sc.next();
				i++;
				add(s1,s2);
				s1=s2;
			}
			System.out.println("Words fed: "+i+" Time taken: "+((System.nanoTime()-initial)/1000000)+"ms");
		} catch (Exception e) {e.printStackTrace();}
	}
	
	//prints paragraph of size of parameter
	public void generateParagraph(int count) {
		String s1=getFirst();
		for (int i=1;i<count+1;i++) {
			System.out.print(s1+" ");
			s1=getNext(s1);
			if (i%10==0)
				System.out.println();
		}
	}
	
	
	
	//gives user the first word
	public String getFirst() {
		Optional<String> op = randomFirst();
		if (op.isEmpty())
			throw new RuntimeException("Nothing in set");
		return op.get();
	}
	
	//uses a key to get a word from the next tree
	public String getNext(String key) {
		Pair t=ts.get(key);
		if (t==null)
			return getFirst();
		Optional<String> op = t.getRandom();
		if (op.isEmpty())
			return getFirst();
		return op.get();
	}
	
	//gets a random element from the first tree
	private Optional<String> randomFirst(){
		if (ct==0)
			return Optional.empty();
		Iterator<String> it = ts.keySet().iterator();
		Random rand = new Random();
		int j = rand.nextInt(ct);
		for (int i=0;i<j;i++) {
			it.next();
		}
		return Optional.of(it.next());
	}
}

