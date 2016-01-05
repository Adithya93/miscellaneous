/**
* Class that implements HashMap as fixed-size array of linked-lists
* Each element of array corresponds to a Hash bucket
* String keys are assigned to buckets based on simple mapping of hashCode range to range of array indices
* Hashing collisions (Items assigned to same bucket) handled by iteration through linked-list bucket
* Average-Case Asymptotic Run-Time : O(1)
* (Worst-Case Asymptotic Run-Time: O(N) // Only if size assigned to array is very small and string keys have very close hashCodes, such that every addition leads to collision
* 
* @author Adithya Raghunathan
*/
public class KPCB {
	/**
	 * Nested class used to construct linked-list buckets
	 * Each node is a pointer that contains 3 other pointers: 
	 * One to the string key's hashCode, one to the corresponding value and one to the memory address of the next Node pointer 
	 */
	class Node {
		private int hash;
		private Object val;
		private Node next;
		Node(int hash, Object val, Node next){
			this.hash = hash;
			this.val = val;
			this.next = next;			
		}
	}
	
	private Node[] map;
	public static final int HASH_MAX = Integer.MAX_VALUE;
	double halfSize;
	double scaleRatio;
	double items = 0;
	
	/**
	 * Constructor that initializes HashMap with fixed capacity of chosen size
	 */
	public KPCB(int size){
		map = new Node[size];
		halfSize = (double)(size)/2;
		scaleRatio = HASH_MAX/halfSize;
	}
	
	/**
	 * 
	 * @param key String to be saved as key
	 * @param value Object to be associated with above key
	 * @return true if key-value pair was saved, false if failed (usually due to exceeding of capacity)
	 */
	public boolean set(String key, Object value){
		if (items >= map.length && get(key) == null) {
			return false;
		}
		int hash = key.hashCode();
		int index = indexOf(hash);
		if (map[index] != null){	
			if (map[index].hash == 0) {
				map[index].hash = hash;
				map[index].val = value;				
				items ++;
				return true;
			}
			Node current = map[index];
			while (current.next != null) {
				if (current.hash == hash) {
					current.val = value;
					return true;					
				}
				current = current.next;
			}
			current.next = new Node(hash, value, null);
			items ++;
			return true;
		}
		else {
			map[index] = new Node(hash, value, null);
			items ++;
			return true;
		}
	}
	
	/**
	 * 
	 * @param key String key whose Object value is to be retrieved
	 * @return Object value associated with key if key was found, else null
	 */
	public Object get(String key){
		if (key == null) {
			return null;
		}
		int hash = key.hashCode();
		int index = indexOf(hash);
		if (index < 0 || index > map.length || map[index] == null) {
			return null;
		}
		if (map[index].hash == hash) {
			return map[index].val;
		}
		else {
			Node current = map[index];
			while (current != null) {
				if (current.hash == hash) {
					return current.val;
				}
				current = current.next;
			}
			return null;
		}
	}
	
	/**
	 * 
	 * @param key String key to be deleted
	 * @return Object previously associated with key if key was found, else null
	 */
	public Object delete(String key){
		int hash = key.hashCode();
		int index = indexOf(hash);
		if (index < 0 || index > map.length || map[index] == null) {
			return null;
		}
		if (map[index].hash == hash) {
			Object val = map[index].val;
			map[index].hash = 0;
			map[index].val = null;
			items --;
			return val;
		}
		else {
			Node current = map[index];
			while (current != null) {
				if (current.hash == hash) {
					Object val = map[index].val;
					map[index].hash = 0;
					map[index].val = null;
					items --;
					return val;
				}
				current = current.next;
			}
			return null;
		}
	}
	
	/**
	 * Ratio of current capacity to maximum capacity assigned in constructor
	 * @return float value between 0.0 (empty) and 1.0 (full) inclusive
	 */
	public float load(){
		return (float)items/map.length;
	}
	
	/**
	 * Convert hashCode into an integer index 
	 * Normalizes range of hashCode values (INT_MIN to INT_MAX) to range of indices (0 to size)
	 @param hashCode the hashCode calculated by Java String hashCode method
	 @return integer index from 0 to size
	 */
	private int indexOf(int hashCode){	
		return (int)(hashCode/scaleRatio + halfSize);
	}
		
	public static void main(String[] args){
		System.out.println("Welcome to Adithya Raghunathan's naive implementation of HashMaps! Try running KPCBTest for some performance metrics. Have fun!");
		/*** Interactive Elements disabled since they would require importing java.util.Scanner library which is forbidden by rules of application
		if (args.length < 1) {
			System.out.println("How big do you want your HashMap to be? Enter a positive integer capacity.");
		}
		else {
			try {
				size = Integer.parseInt(args[0]);
			}
			catch (Exception e) {
				System.out.println("Input parameter is invalid. Please enter a positive integer for capcity of HashMap.");
			}
			KPCB Demo = new KPCB(size);
			System.out.println("Successfully initialized a HashMap of capacity " + size);
		}
		***/
		String testStr1 = "booya";
		String testStr2 = "yakshemesh";
		String testStr3 = "wicked";
		String testStr4 = "Check it before you wreck it";
		String testStr5 = "Lolz";
		String trouble = "Extra";
		String testVal = "Big up yourself";
		Integer testVal2 = 34;
		int testVal3 = 47;
		boolean testVal4 = true;
		String testVal5 = "blah";
		String troubleVal = "Extra Val";
		KPCB Tester = new KPCB(5);
		System.out.println(Tester.set(testStr1, testVal));
		System.out.println(Tester.set(testStr2, testVal2));
		System.out.println(Tester.set(testStr3, testVal3));
		System.out.println("Current load: " + Tester.load());
		System.out.println(Tester.set(testStr4, testVal4));
		System.out.println(Tester.set(testStr5, testVal5));
		System.out.println("Current load: " + Tester.load());
		System.out.println(Tester.set(trouble, troubleVal));
		System.out.println("Current load: " + Tester.load());
		System.out.println(Tester.get(testStr1));
		System.out.println(Tester.get(testStr2));
		System.out.println(Tester.get(testStr3));
		System.out.println(Tester.get(testStr4));
		System.out.println(Tester.get(testStr5));
		System.out.println(Tester.get(trouble));
		System.out.println(Tester.delete(testStr3));
		System.out.println("Current load: " + Tester.load());
		System.out.println(Tester.set(trouble, troubleVal));
		System.out.println("Current load: " + Tester.load());
		System.out.println(Tester.get(trouble));
		System.out.println(Tester.set(testStr1, "Booyakasha!"));		
	}
}
