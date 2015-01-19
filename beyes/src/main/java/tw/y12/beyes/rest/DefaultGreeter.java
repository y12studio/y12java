package tw.y12.beyes.rest;


public class DefaultGreeter implements Greeter {

	public String greet(String name) {
		return "Hello " + name;
	}

}
