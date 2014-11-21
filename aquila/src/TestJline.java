
import jline.ConsoleReader;

public class TestJline {

	public static void main(String[] args) throws Exception {
		ConsoleReader reader = new ConsoleReader();
		String line = reader.readLine(">Name:");
//		reader.printString("Name:");
		System.out.println(line);
		String line2 = reader.readLine(">");
		System.out.println(line2);
	}
}
