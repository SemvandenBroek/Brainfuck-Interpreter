import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class BFCKinterpreter {
	
	int programLength;
	int ip = 0;
	int mp = 0;
	int memory_size = 256;
	int mem_max_value = 255;					//Is always 255 because there are only 255 ASCII characters
	
	int[] program;
	int[] memory;
	int[] targets;
	
	String filename;
	
	BufferedReader reader;
	Scanner keyboard;
	

	public BFCKinterpreter(String filename) {
		this.filename = filename;
		
		initialize();
	}
	
	public BFCKinterpreter(String filename, int memory_size) {
		this.filename = filename;
		this.memory_size = memory_size;
		
		initialize();
	}
	
	public void run() {
		while (ip < programLength) {
			int op = program[ip];
			executeOpcode(op);
			ip++;
		}
		System.out.print("\n\n");
		keyboard.close();
	}
	
	private void initialize() {
		memory = new int[memory_size];
		targets = new int[memory_size];
		keyboard = new Scanner(System.in);
		try {
			readFile();
			getProgramLength();
			readFile();
			initProgram();
			initMemory();
			initTargets();
		} catch (IOException e) {
			System.out.println("[" + filename +"] IOException: " + e.getMessage());
		} catch (ParseException e) {
			System.out.println("[" + filename +"] ParseException: " + e.getMessage());
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("[" + filename +"] Not enough memory available for this program");
		}
	}
	
	private void readFile() throws IOException {
		reader = new BufferedReader(new FileReader(this.filename));
	}
	
	private void getProgramLength() throws IOException {
		for (int i=1; true; i++) {
			if (reader.read() == -1) break;
			this.programLength = i;
		}
	}
	
	private void initProgram() throws IOException {
		program = new int[programLength];
		for (int i=0; i<programLength; i++) {
			program[i] = reader.read();
		}
	}
	
	private void initMemory() {
		for (int i=0; i<memory_size; i++) {
			memory[i] = 0;
		}
		mp = 0;
	}
	
	private void initTargets() throws ParseException {
		ArrayList<Integer> tempStack = new ArrayList<Integer>();
		for (int i=0; i<programLength; i++) {
			int op = program[i];
			if (op == '[') {
				tempStack.add(i);
			}
			if (op == ']') {
				if (tempStack.isEmpty()) throw new ParseException("] was not preceded by a [ (incorrect loop)", 0);
				int target = tempStack.remove(tempStack.size()-1);
				targets[i] = target;
				targets[target] = i;
			}
		}
		if (!tempStack.isEmpty()) throw new ParseException("[ was not closed off by a ] (incorrect loop)", 0);
	}
	
	private void executeOpcode(int op) {
		switch(op){
		case '+':
			memory[mp]++;
			if (memory[mp] > mem_max_value) memory[mp] = 0;
			break;
		case '-':
			memory[mp]--;
			if (memory[mp] < 0) memory[mp] = mem_max_value;
			break;
		case '>':
			mp++;
			if (mp >= memory_size) mp = 0;
			break;
		case '<':
			mp--;
			if (mp < 0) mp = memory_size - 1;
			break;
		case '[':
			if (memory[mp] == 0) ip = targets[ip];
			break;
		case ']':
			ip = targets[ip] - 1;
			break;
		case '.':
			System.out.print(Character.toString((char) memory[mp]));
			break;
		case ',':
			if (keyboard.hasNext())
				memory[mp] = keyboard.next().charAt(0);
			else
				memory[mp] = 0;
			break;
		}
	}
}