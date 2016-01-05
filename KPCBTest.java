import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;
import java.lang.Exception;

public class KPCBTest {
	public static final String CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	public static final int RANDOM_SEED = 156234;
	public static final int MIN_STRING_SIZE = 0;
	public static final int MAX_STRING_SIZE = 100;
	public static final int SMALL_KEYSET_SIZE = 10000;
	public static final int LARGE_KEYSET_SIZE = 1000000;
	private String[] smallTestKeys;
	private String[] largeTestKeys;
	private Random generator = new Random();
	private Object[] smallTestValues; // Values can be any objects, but integers are used in test for simplicity
	private Object[] largeTestValues; // Type of values does not have to be uniform and does not matter, but test uses all ints for simplicity
	private KPCB small;
	private KPCB large;
	
	private long tStart;
	private long tSmall;
	private long tLarge;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		smallTestKeys = new String[SMALL_KEYSET_SIZE];
		smallTestValues = new Object[SMALL_KEYSET_SIZE];
		largeTestKeys = new String[LARGE_KEYSET_SIZE];
		largeTestValues = new Object[LARGE_KEYSET_SIZE];
		generator.setSeed(RANDOM_SEED);
		for (int stringNum = 0; stringNum < SMALL_KEYSET_SIZE; stringNum ++){
			int stringSize = generator.nextInt(MAX_STRING_SIZE + 1);
			String current = "";
			for (int letterNum = 0; letterNum < stringSize; letterNum ++) {
				int choice = generator.nextInt(CHARS.length());
				current += CHARS.charAt(choice);
			}
			smallTestKeys[stringNum] = current;
			smallTestValues[stringNum] = generator.nextInt();
		}
		
		for (int stringNum = 0; stringNum < LARGE_KEYSET_SIZE; stringNum ++){
			int stringSize = generator.nextInt(MAX_STRING_SIZE + 1);
			String current = "";
			for (int letterNum = 0; letterNum < stringSize; letterNum ++) {
				int choice = generator.nextInt(CHARS.length());
				current += CHARS.charAt(choice);
			}
			largeTestKeys[stringNum] = current;
			largeTestValues[stringNum] = generator.nextInt();
		}
		tStart = System.currentTimeMillis();
		tSmall = System.currentTimeMillis();
		tLarge = System.currentTimeMillis();
	}

	@Test
	public void testKPCB() {
		tStart = System.currentTimeMillis();
		small = new KPCB(SMALL_KEYSET_SIZE);
		tSmall = System.currentTimeMillis();
		large = new KPCB(LARGE_KEYSET_SIZE);
		tLarge = System.currentTimeMillis();
		assert(small != null && large != null);
	}

	@Test
	public void testSet() {
		testKPCB();
		tStart = System.currentTimeMillis();
		for (int index = 0; index < SMALL_KEYSET_SIZE; index ++) {
			assert(small.set(smallTestKeys[index], smallTestValues[index]) == true);
		}
		tSmall = System.currentTimeMillis();	
		assert(small.set("Excess Key", "Excess Value") == false);
		int randSmallIndex = generator.nextInt(SMALL_KEYSET_SIZE);
		assert(small.set(smallTestKeys[randSmallIndex], "Can still reset existing key when at full capacity") == true);
		assert(small.set(smallTestKeys[randSmallIndex], smallTestValues[randSmallIndex]) == true);
		for (int index = 0; index < LARGE_KEYSET_SIZE; index ++) {
			assert(large.set(largeTestKeys[index], largeTestValues[index]) == true);
		}
		tLarge = System.currentTimeMillis();
		assert(large.set("Excess Key", "Excess Value") == false);
		int randLargeIndex = generator.nextInt(LARGE_KEYSET_SIZE);
		assert(large.set(largeTestKeys[randLargeIndex], "Can still reset existing key when at full capacity") == true);
		assert(large.set(largeTestKeys[randLargeIndex], largeTestValues[randLargeIndex]) == true);
	}

	@Test
	public void testGet() {
		testSet();
		tStart = System.currentTimeMillis();
		for (int index = 0; index < SMALL_KEYSET_SIZE; index ++) {
			assert(small.get(smallTestKeys[index]) == smallTestValues[index]);
		}
		tSmall = System.currentTimeMillis();
		for (int index = 0; index < LARGE_KEYSET_SIZE; index ++) {
			assert(large.get(largeTestKeys[index]) == largeTestValues[index]);
		}
		tLarge = System.currentTimeMillis();
		assert(small.get(null) == null && large.get(null) == null);
		assert(small.get("Absent String") == null && large.get("Another absent string") == null);
	}

	@Test
	public void testDelete() {
		testSet();
		tStart = System.currentTimeMillis();
		int keyIndexSmall = generator.nextInt(SMALL_KEYSET_SIZE);
		int keyIndexLarge = generator.nextInt(LARGE_KEYSET_SIZE);
		String randKeySmall = smallTestKeys[keyIndexSmall];
		String randKeyLarge = largeTestKeys[keyIndexLarge];
		assert(small.get(randKeySmall) != null);
		assert(large.get(randKeyLarge) != null);
		assert(small.delete(randKeySmall) == smallTestValues[keyIndexSmall]);
		assert(large.delete(randKeyLarge) == largeTestValues[keyIndexLarge]);
		assert(small.get(randKeySmall) == null);
		assert(large.get(randKeyLarge) == null);
		assert(small.delete("Non-existent key") == null && large.delete("Another non-existent key") == null);
		small.set(randKeySmall, smallTestValues[keyIndexSmall]);
		large.set(randKeyLarge, largeTestValues[keyIndexLarge]);
		for (String s : smallTestKeys) {
			small.delete(s);
		}
		tSmall = System.currentTimeMillis();
		
		for (String s : largeTestKeys) {
			large.delete(s);
		}
		tLarge = System.currentTimeMillis();
		testSet();
	}

	@Test
	public void testLoad() {
		testSet();
		assert(small.load() == 1.0 && large.load() == 1.0);
		int keyIndexSmall = generator.nextInt(SMALL_KEYSET_SIZE);
		int keyIndexLarge = generator.nextInt(LARGE_KEYSET_SIZE);
		small.delete(smallTestKeys[keyIndexSmall]);
		large.delete(largeTestKeys[keyIndexLarge]);
		assert(small.load() < 1.0);
		assert(large.load() < 1.0);
		small.set(smallTestKeys[keyIndexSmall], smallTestValues[keyIndexSmall]);
		large.set(largeTestKeys[keyIndexLarge], largeTestValues[keyIndexLarge]);
		assert(small.load() == 1.0);
		assert(large.load() == 1.0);
		small.set("Excess Key", "This should not increase load");
		large.set("Excess Key", "This should not increase load");
		assert(small.load() == 1.0);
		assert(large.load() == 1.0);
		small.set(smallTestKeys[keyIndexSmall], "Changing value of existing key should not increase load");
		large.set(largeTestKeys[keyIndexLarge], "Changing value of existing key should not increase load");
		assert(small.load() == 1.0);
		assert(large.load() == 1.0);
	}
	
	@Test
	public void testRunTimes() {
		testKPCB();
		long smallInitTime = (tSmall - tStart);
		long largeInitTime = (tLarge - tStart);
		System.out.println("Time taken in milliseconds for initialization of " + SMALL_KEYSET_SIZE + "-size hash map: " + smallInitTime);
		System.out.println("Time taken in milliseconds for initialization of " + LARGE_KEYSET_SIZE + "-size hash map: " + largeInitTime);
		
		testSet();
		long smallSetTime = (tSmall - tStart);
		long largeSetTime = (tLarge - tStart);
		System.out.println("Time taken in milliseconds for setting all keys of " + SMALL_KEYSET_SIZE + "-size hash map: " + smallSetTime);
		System.out.println("Time taken in milliseconds for setting all keys of " + LARGE_KEYSET_SIZE + "-size hash map: " + largeSetTime);
		
		testGet();
		long smallGetTime = (tSmall - tStart);
		long largeGetTime = (tLarge - tStart);
		System.out.println("Time taken in milliseconds for getting all keys of " + SMALL_KEYSET_SIZE + "-size hash map: " + smallGetTime);
		System.out.println("Time taken in milliseconds for getting all keys of " + LARGE_KEYSET_SIZE + "-size hash map: " + largeGetTime);
		
		testDelete();
		long smallDeleteTime = (tSmall - tStart);
		long largeDeleteTime = (tLarge - tStart);
		System.out.println("Time taken in milliseconds for deleting all keys of " + SMALL_KEYSET_SIZE + "-size hash map: " + smallDeleteTime);
		System.out.println("Time taken in milliseconds for deleting all keys of " + LARGE_KEYSET_SIZE + "-size hash map: " + largeDeleteTime);
	}
	
	public static void main(String[] args) {
		KPCBTest tester = new KPCBTest();
		try {
			tester.setUp();
		}
		catch (Exception e){
			System.out.println("Sorry, unable to set up tests");
		}
		tester.testRunTimes();
	}	
}
