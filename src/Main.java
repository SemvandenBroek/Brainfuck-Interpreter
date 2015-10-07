public class Main {

	public static void main(String[] args) {
		BFCKinterpreter helloWorld = new BFCKinterpreter("HelloWorld.bfck");
		helloWorld.run();
		
		BFCKinterpreter cat = new BFCKinterpreter("cat.bfck");
		cat.run();
		
		
	}

}
