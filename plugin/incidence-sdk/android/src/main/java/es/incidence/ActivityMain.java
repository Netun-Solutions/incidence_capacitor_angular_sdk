package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;

import com.e510.commons.utils.FontUtils;

import es.incidence.R;
import es.incidence.core.Constants;
import es.incidence.core.domain.ColorType;
import es.incidence.core.domain.IdentityType;
import es.incidence.core.domain.Incidence;
import es.incidence.core.domain.IncidenceType;
import es.incidence.core.domain.Policy;
import es.incidence.core.domain.User;
import es.incidence.core.domain.Vehicle;
import es.incidence.core.domain.VehicleType;
import es.incidence.library.IncidenceLibraryManager;

public class ActivityMain extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);

    // setContentView(R.layout.simple_activity);

  }
}
