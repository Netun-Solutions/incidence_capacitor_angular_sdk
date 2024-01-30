package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.getcapacitor.BridgeActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import es.incidence.IncidenceMain;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import es.incidence.core.domain.ColorType;
import es.incidence.core.domain.IdentityType;
import es.incidence.core.domain.Incidence;
import es.incidence.core.domain.IncidenceType;
import es.incidence.core.domain.Policy;
import es.incidence.core.domain.User;
import es.incidence.core.domain.Vehicle;
import es.incidence.core.domain.VehicleType;
import es.incidence.library.IncidenceLibraryManager;

import java.util.ArrayList;
public class MainActivity extends BridgeActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) { //
    super.onCreate(savedInstanceState);
  }
}
