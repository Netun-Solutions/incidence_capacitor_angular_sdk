package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.util.Log;
import android.app.Application;

import es.incidence.library.IncidenceLibraryManager;
import es.incidence.library.config.IncidenceEnvironment;
import es.incidence.library.config.IncidenceLibraryConfig;

public class IApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
  }
}
