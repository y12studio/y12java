package org.gradle;

import static spark.Spark.*;
import java.util.Date;

public class HelloSpark {

	public static void main(String[] args) {
		get("/hello",(req,res)->"Hello World at " + new Date());
	}
}
