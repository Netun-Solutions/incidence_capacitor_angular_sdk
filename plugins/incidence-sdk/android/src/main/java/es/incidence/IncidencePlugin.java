package es.incidence;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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


@CapacitorPlugin(name = "Incidence")
public class IncidencePlugin extends Plugin {

    @PluginMethod
    public void initConfig(PluginCall call) {
        Log.i("IncidencePlugin", "initConfig OK");
        JSObject result = new JSObject();
        result.put("status", "success");
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
        }else{ Log.i("IncidencePlugin", "Error no identityType"); error = true; }

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
        }else{ Log.i("IncidencePlugin", "Error no dni"); error = true; }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{ Log.i("IncidencePlugin", "Error no birthday"); error = true; }
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
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{ Log.i("IncidencePlugin", "Error no policyNumber"); error = true; }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{ Log.i("IncidencePlugin", "Error no policyEnd"); error = true; }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{ Log.i("IncidencePlugin", "Error no dni"); error = true; }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{ Log.i("IncidencePlugin", "Error no registrationYear"); error = true; }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no brand"); error = true; }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no model"); error = true; }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;

        if(error){
            result.put("status", "error");
            result.put("message", "Missing parameters");
        }else{
            Intent activity = IncidenceLibraryManager.instance.getDeviceCreateViewController(user, vehicle);
            getActivity().startActivity(activity);
            result.put("status", "success");
        }
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
        }else{ Log.i("IncidencePlugin", "Error no identityType"); error = true; }

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
        }else{ Log.i("IncidencePlugin", "Error no dni"); error = true; }
        if (userCall.has("birthday")) {
            user.birthday = userCall.getString("birthday"); // (fecha de Nacimiento)
        }else{ Log.i("IncidencePlugin", "Error no birthday"); error = true; }
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
        }

        Policy policy = new Policy();
        if (vehicleCall.has("policyNumber")) {
            policy.policyNumber = vehicleCall.getString("policyNumber"); // (número de la póliza)
        }else{ Log.i("IncidencePlugin", "Error no policyNumber"); error = true; }
        if (vehicleCall.has("policyEnd")) {
            policy.policyEnd = vehicleCall.getString("policyEnd"); // (fecha caducidad de la póliza)
        }else{ Log.i("IncidencePlugin", "Error no policyEnd"); error = true; }
        policy.identityType = dniIdentityType; // (tipo de documento identidad del asegurador)
        if (vehicleCall.has("dni")) {
            policy.dni = vehicleCall.getString("dni"); // (documento de identidad del asegurador)
        }else{ Log.i("IncidencePlugin", "Error no dni"); error = true; }

        Vehicle vehicle = new Vehicle();
        if (vehicleCall.has("externalVehicleId")) {
            vehicle.externalVehicleId = vehicleCall.getString("externalVehicleId");
        }else{ Log.i("IncidencePlugin", "Error no externalVehicleId"); error = true; }
        if (vehicleCall.has("licensePlate")) {
            vehicle.licensePlate = vehicleCall.getString("licensePlate"); // (matrícula del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no licensePlate"); error = true; }
        if (vehicleCall.has("registrationYear")) {
            vehicle.registrationYear = vehicleCall.getString("registrationYear"); // (fecha de matriculación)
        }else{ Log.i("IncidencePlugin", "Error no registrationYear"); error = true; }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if (vehicleCall.has("brand")) {
            vehicle.brand = vehicleCall.getString("brand"); // (marca del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no brand"); error = true; }
        if (vehicleCall.has("model")) {
            vehicle.model = vehicleCall.getString("model"); // (modelo del vehículo)
        }else{ Log.i("IncidencePlugin", "Error no model"); error = true; }
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
}
