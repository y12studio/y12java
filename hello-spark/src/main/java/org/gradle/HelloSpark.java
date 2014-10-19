package org.gradle;

import static spark.Spark.*;
import java.util.Date;

public class HelloSpark {
	public static void main(String[] args) {
		
		staticFileLocation("/public");
		
		setPort(8000);
		
		get("/hello", (request, respone) -> "Hello World at " + new Date());

		get("/hellojson", (req, res) -> {
			return new MyMessage("hello date : " + new Date(), 100);
		}, new JsonTransformer());

	}
}
