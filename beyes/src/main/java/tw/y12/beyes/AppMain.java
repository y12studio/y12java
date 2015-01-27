package tw.y12.beyes;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class AppMain {

	public static void main(String[] args) throws Exception {
		Config conf = ConfigFactory.load();
		new AppServer().startRest(conf);

	}

}
