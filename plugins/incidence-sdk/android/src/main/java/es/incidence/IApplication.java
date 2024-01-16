package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.util.Log;
import android.app.Application;

import es.incidence.library.IncidenceLibraryManager;
import es.incidence.library.config.Environment;
import es.incidence.library.config.IncidenceLibraryConfig;

public class IApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("IncidencePlugin", "Pre Config");
    String apiKey = "bWFwZnJlX2NsaWVudDpkOTBlMTA3ZjdhNGU1NmQyYzlkMTJhMHM3ZTQ1ZDQ1MQ==";
    IncidenceLibraryConfig config = new IncidenceLibraryConfig.Builder()
      .setApikey(apiKey)
      .setEnvironment(Environment.PRE)
      .createIncidenceLibraryConfig();
    IncidenceLibraryManager.setup(this, config);
  }
}
