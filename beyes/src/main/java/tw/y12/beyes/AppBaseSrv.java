package tw.y12.beyes;

import com.google.common.eventbus.EventBus;
import com.typesafe.config.Config;

public interface AppBaseSrv {
	
	public EventBus getEventBus();

	Config getConf();
	
	NonPersistConf getNonPersistConf();

}
