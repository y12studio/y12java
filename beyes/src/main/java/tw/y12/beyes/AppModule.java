package tw.y12.beyes;

import tw.y12.beyes.rest.DefaultGreeter;
import tw.y12.beyes.rest.Greeter;
import tw.y12.beyes.rest.HelloResource;
import tw.y12.beyes.rest.WalletResource;

import com.google.inject.Binder;
import com.google.inject.Module;

public class AppModule implements Module {

	public void configure(Binder binder) {
		binder.bind(AppBaseSrv.class).to(AppBaseSrvImpl.class).asEagerSingleton();
		binder.bind(WalletWatcher.class).to(WalletWatcherImpl.class).asEagerSingleton();
		binder.bind(BlockActer.class).to(BlockActerImpl.class).asEagerSingleton();
		binder.bind(HelloResource.class);
		binder.bind(WalletResource.class);
		binder.bind(Greeter.class).to(DefaultGreeter.class);
		binder.bind(SrvCtrl.class).to(SrvCtrlImpl.class).asEagerSingleton();
		
	}
}
