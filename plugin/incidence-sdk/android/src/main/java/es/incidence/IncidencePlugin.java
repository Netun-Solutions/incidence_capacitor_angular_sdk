package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.ActivityCallback;
import com.getcapacitor.annotation.CapacitorPlugin;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCaller;
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
import es.incidence.library.config.IncidenceEnvironment;
import es.incidence.library.config.IncidenceLibraryConfig;


@CapacitorPlugin(name = "Incidence")
public class IncidencePlugin extends Plugin {
    @PluginMethod
    public void initConfig(PluginCall call) {
        Log.i("IncidencePlugin", "initConfig OK");
        String apiKey = call.getString("apikey", "");
        String environment = call.getString("environment", "");
        JSObject result = new JSObject();

        Log.i("IncidencePlugin", "Config");
        if(apiKey.equals("")){
          result.put("status", "error");
          result.put("message", "Incorrect apikey");
        }else {
          if (environment.equals("test")) {
            IncidenceLibraryConfig config = new IncidenceLibraryConfig.Builder()
              .setApikey(apiKey)
              .setEnvironment(IncidenceEnvironment.TEST)
              .createIncidenceLibraryConfig();
            IncidenceLibraryManager.setup((Application) this.getActivity().getApplicationContext(), config);
            result.put("status", "success");

          } else if (environment.equals("pre")) {
            IncidenceLibraryConfig config = new IncidenceLibraryConfig.Builder()
              .setApikey(apiKey)
              .setEnvironment(IncidenceEnvironment.PRE)
              .createIncidenceLibraryConfig();
            IncidenceLibraryManager.setup((Application) this.getActivity().getApplicationContext(), config);
            result.put("status", "success");
          } else if (environment.equals("pro")) {
            IncidenceLibraryConfig config = new IncidenceLibraryConfig.Builder()
              .setApikey(apiKey)
              .setEnvironment(IncidenceEnvironment.PRO)
              .createIncidenceLibraryConfig();
            IncidenceLibraryManager.setup((Application) this.getActivity().getApplicationContext(), config);
            result.put("status", "success");
          } else {
            result.put("status", "error");
            result.put("message", "Incorrect environment");
          }
        }

        call.resolve(result);
    }
    @PluginMethod
    public void addDevice(PluginCall call) {

        Log.i("IncidencePlugin", "Into addDevice");
        JSObject result = new JSObject();

        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
        }else{
            /*Intent activity = IncidenceLibraryManager.instance.getDeviceCreateViewController(user, vehicle);
            getActivity().startActivity(activity);
            result.put("status", "success");*/

            Intent activity = IncidenceLibraryManager.instance.getDeviceCreateViewController(user, vehicle);
            startActivityForResult(call, activity, "resultGetDeviceCreateViewController");
        }
        //call.resolve(result);
    }
    @ActivityCallback
    private void resultGetDeviceCreateViewController(PluginCall call, ActivityResult resultActivity) {
      JSObject result = new JSObject();
      /*if (resultActivity.getResultCode() == Activity.RESULT_OK) {
        Intent data = resultActivity.getData();
        Incidence incidence = data.getParcelableExtra("incidence");
        result.put("status", "success");
      } else {
        result.put("status", "error");
        result.put("message", "No se ha podido vincular la baliza");
      }*/
      result.put("status", "success");
      call.resolve(result);
    }
    @PluginMethod
    public void checkDevice(PluginCall call) {

        Log.i("IncidencePlugin", "Into checkDevice");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
        }else{
            Intent activity = IncidenceLibraryManager.instance.getDeviceReviewViewController(user, vehicle);
            getActivity().startActivity(activity);
            result.put("status", "success");
        }

        call.resolve(result);
    }
    @PluginMethod
    public void deleteDevice(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> deleteDevice");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
            call.resolve(result);
        }else{
            IncidenceLibraryManager.instance.deleteBeaconFunc(user, vehicle, response -> {
                // if (response.isSuccess()) {
                //     result.put("status", "success");
                // } else {
                //     result.put("status", "error");
                //     result.put("message", "Can't delete the beacon or none is associated.");
                // }
                result.put("status", response.status);
                result.put("message", response.message);
                call.resolve(result);
            });
        }
    }
    @PluginMethod
    public void getDevice(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> getDevice");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
            call.resolve(result);
        }else{
            IncidenceLibraryManager.instance.getBeaconFunc(user, vehicle, response -> {
                if (response.isSuccess()) {
                    result.put("status", "success");
                    result.put("device", true);
                } else {
                    result.put("status", "success");
                    result.put("device", false);                    
                }
                result.put("message", response.message);
                call.resolve(result);
            });
        }
    }
    @PluginMethod
    public void createIncidence(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> createIncidence");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());
        JSObject incidenceCall = call.getObject("incidence", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        IncidenceType incidenceType = new IncidenceType();
        // if (incidenceCall.has("typeId")) {
        //     incidenceType.id = incidenceCall.getInteger("typeId"); // Pinchazo
        // }else{ Log.i("IncidencePlugin", "Error no incidence typeId"); error = true; }
        if (incidenceCall.has("typeExternalId")) {
            incidenceType.externalId = incidenceCall.getString("typeExternalId"); // Pinchazo
        }else{ Log.i("IncidencePlugin", "Error no incidence typeExternalId"); error = true; }

        Incidence incidence = new Incidence();
        incidence.incidenceType = incidenceType;
        if (incidenceCall.has("street")) {
            incidence.street = incidenceCall.getString("street");
        }else{ Log.i("IncidencePlugin", "Error no incidence street"); error = true; }
        if (incidenceCall.has("city")) {
            incidence.city = incidenceCall.getString("city");
        }else{ Log.i("IncidencePlugin", "Error no incidence city"); error = true; }
        if (incidenceCall.has("country")) {
            incidence.country = incidenceCall.getString("country");
        }else{ Log.i("IncidencePlugin", "Error no incidence country"); error = true; }
        if (incidenceCall.has("latitude")) {
            incidence.latitude = Double.parseDouble(incidenceCall.getString("latitude"));
        }else{ Log.i("IncidencePlugin", "Error no incidence latitude"); error = true; }
        if (incidenceCall.has("longitude")) {
            incidence.longitude = Double.parseDouble(incidenceCall.getString("longitude"));
        }else{ Log.i("IncidencePlugin", "Error no incidence longitude"); error = true; }
        if (incidenceCall.has("externalIncidenceId")) {
            incidence.externalIncidenceId = incidenceCall.getString("externalIncidenceId");
        }else{ Log.i("IncidencePlugin", "Error no incidence externalIncidenceId"); error = true; }

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
            call.resolve(result);
        }else{
            IncidenceLibraryManager.instance.createIncidenceFunc(user, vehicle, incidence, response -> {
                result.put("status", response.status);
                result.put("message", response.message);
                call.resolve(result);
            });
        }
    }
    @PluginMethod
    public void createIncidenceFlow(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> createIncidenceFlow");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
            call.resolve(result);
        }else{
            Intent activity = IncidenceLibraryManager.instance.getReportIncViewControllerFlowSimple(user, vehicle);
            startActivityForResult(call, activity, "resultReportIncViewControllerFlowSimple");
        }
    }
    @ActivityCallback
    private void resultReportIncViewControllerFlowSimple(PluginCall call, ActivityResult resultActivity) {
        JSObject result = new JSObject();
        if (resultActivity.getResultCode() == Activity.RESULT_OK) {
            Intent data = resultActivity.getData();
            Incidence incidence = data.getParcelableExtra("incidence");
            result.put("status", "success");
            result.put("externalId", incidence.incidenceType.externalId);
            result.put("street", incidence.street);
            result.put("city", incidence.city);
            result.put("country", incidence.country);
            result.put("latitude", incidence.latitude);
            result.put("longitude", incidence.longitude);
            result.put("externalIncidenceId", incidence.externalIncidenceId);
        } else {
            result.put("status", "error");
            result.put("message", "No se ha podido crear la incidencia");
        }
        call.resolve(result);
    }
    @PluginMethod
    public void closeIncidence(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> createIncidence");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());
        JSObject incidenceCall = call.getObject("incidence", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        IncidenceType incidenceType = new IncidenceType();
        // if (incidenceCall.has("typeId")) {
        //     incidenceType.id = incidenceCall.getInteger("typeId"); // Pinchazo
        // }else{ Log.i("IncidencePlugin", "Error no incidence typeId"); error = true; }
        if (incidenceCall.has("typeExternalId")) {
            incidenceType.externalId = incidenceCall.getString("typeExternalId"); // Pinchazo
        }else{ Log.i("IncidencePlugin", "Error no incidence typeExternalId"); error = true; }

        Incidence incidence = new Incidence();
        incidence.incidenceType = incidenceType;
        if (incidenceCall.has("street")) {
            incidence.street = incidenceCall.getString("street");
        }else{ Log.i("IncidencePlugin", "Error no incidence street"); error = true; }
        if (incidenceCall.has("city")) {
            incidence.city = incidenceCall.getString("city");
        }else{ Log.i("IncidencePlugin", "Error no incidence city"); error = true; }
        if (incidenceCall.has("country")) {
            incidence.country = incidenceCall.getString("country");
        }else{ Log.i("IncidencePlugin", "Error no incidence country"); error = true; }
        if (incidenceCall.has("latitude")) {
            incidence.latitude = Double.parseDouble(incidenceCall.getString("latitude"));
        }else{ Log.i("IncidencePlugin", "Error no incidence latitude"); error = true; }
        if (incidenceCall.has("longitude")) {
            incidence.longitude = Double.parseDouble(incidenceCall.getString("longitude"));
        }else{ Log.i("IncidencePlugin", "Error no incidence longitude"); error = true; }
        if (incidenceCall.has("externalIncidenceId")) {
            incidence.externalIncidenceId = incidenceCall.getString("externalIncidenceId");
        }else{ Log.i("IncidencePlugin", "Error no incidence externalIncidenceId"); error = true; }

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
            call.resolve(result);
        }else{
            IncidenceLibraryManager.instance.closeIncidenceFunc(user, vehicle, incidence, response -> {
                result.put("status", response.status);
                result.put("message", response.message);
                call.resolve(result);
            });
        }
    }
    @PluginMethod
    public void ecommerce(PluginCall call) {

        Log.i("IncidencePlugin", "Entraa -> ecommerce");
        JSObject result = new JSObject();
        boolean error = false;
        JSObject userCall = call.getObject("user", new JSObject());
        JSObject vehicleCall = call.getObject("vehicle", new JSObject());

        IdentityType dniIdentityType = new IdentityType();
        if (userCall.has("identityType")) {
            dniIdentityType.name = userCall.getString("identityType"); // (tipo de documento de identidad: dni, nie, cif)
        }else{
            Log.i("IncidencePlugin", "Error no identityType");
            dniIdentityType.name = "";
            //error = true;
        }

        User user = new User();
        if (userCall.has("externalUserId")) {
            user.externalUserId = userCall.getString("externalUserId"); // (identificador externo del usuario)
        }else{ Log.i("IncidencePlugin", "Error no externalUserId"); error = true; }
        if (userCall.has("name")) {
            user.name = userCall.getString("name"); // (nombre del usuario)
        }else{ Log.i("IncidencePlugin", "Error no name"); error = true; }
        if (userCall.has("phone")) {
            user.phone = userCall.getString("phone"); // (teléfono)
        }else{ Log.i("IncidencePlugin", "Error no phone"); error = true; }
        if (userCall.has("email")) {
            user.email = userCall.getString("email"); // (e-mail)
        }else{ Log.i("IncidencePlugin", "Error no email"); error = true; }
        user.identityType = dniIdentityType;
        if (userCall.has("dni")) {
            user.dni = userCall.getString("dni"); // (número del documento de identidad)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            user.dni = "";
        }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{
            Log.i("IncidencePlugin", "Error no birthday");
            user.birthday = "";
            //error = true;
        }
        if (userCall.has("checkTerms")) {
            user.checkTerms = userCall.getString("checkTerms"); // (aceptación de la privacidad)
        }else{ Log.i("IncidencePlugin", "Error no checkTerms"); error = true; }

        VehicleType vehicleType = new VehicleType();
        if (vehicleCall.has("vehicleType")) {
            vehicleType.name = vehicleCall.getString("vehicleType");
        }else{ Log.i("IncidencePlugin", "Error no vehicleType"); error = true; }

        ColorType color = new ColorType();
        if (vehicleCall.has("color")) {
            color.name = vehicleCall.getString("color");
        }else{
            Log.i("IncidencePlugin", "Error no color");
            //error = true;
            color.name = "";
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyNumber");
            policy.policyNumber = "";
            //error = true;
        }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{
            Log.i("IncidencePlugin", "Error no policyEnd");
            //error = true;
            policy.policyEnd = "";
        }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{
            Log.i("IncidencePlugin", "Error no dni");
            //error = true;
            policy.dni = "";
        }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{
            Log.i("IncidencePlugin", "Error no registrationYear");
            //error = true;
            vehicle.registrationYear = "";
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no brand");
            //error = true;
            vehicle.brand = "";
        }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{
            Log.i("IncidencePlugin", "Error no model");
            //error = true;
            vehicle.model = "";
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
        }else{
            Intent activity = IncidenceLibraryManager.instance.getEcommerceViewController(user, vehicle);
            getActivity().startActivity(activity);
            result.put("status", "success");
        }

        call.resolve(result);
    }
}
