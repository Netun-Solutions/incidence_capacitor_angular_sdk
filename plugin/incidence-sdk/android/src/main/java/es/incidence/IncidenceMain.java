package es.incidence;

import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.BridgeActivity;

import es.incidence.core.domain.ColorType;
import es.incidence.core.domain.IdentityType;
import es.incidence.core.domain.IncidenceType;
import es.incidence.core.domain.Policy;
import es.incidence.core.domain.User;
import es.incidence.core.domain.Vehicle;
import es.incidence.core.domain.VehicleType;
import es.incidence.library.IncidenceLibraryManager;

public class IncidenceMain extends Plugin {

    private Button btnDeviceCreate;
    private Button btnDeviceDelete;
    private Button btnDeviceReview;
    private Button btnIncidenceCreate;
    private Button btnIncidenceClose;
    private Button btnEcommerce;
    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }

    public void test(PluginCall call) {

    }
}
