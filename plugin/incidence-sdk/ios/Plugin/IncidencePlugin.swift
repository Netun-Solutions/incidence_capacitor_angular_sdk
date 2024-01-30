import Foundation
import Capacitor
import os
import IncidenceIOSSDK

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(IncidencePlugin)
public class IncidencePlugin: CAPPlugin, ReportTypeViewControllerDelegate {
    static var callResponse: CAPPluginCall? = nil;

    static let shared: AppDelegate = {
        guard let appD = UIApplication.shared.delegate as? AppDelegate else {
          return AppDelegate()
        }
        return appD
      }()

    var window: UIWindow?

    @objc func initConfig(_ call: CAPPluginCall) {
        var result = [String: Any]()
        let apikey = call.getString("apikey")
        print("apikey")
        print(apikey!)
        let environment = call.getString("environment")
        print("environment")
        print(environment!)
        if(apikey != nil && apikey != ""){
            if(environment! == "test"){
                let config = IncidenceLibraryConfig(apiKey: .init(apikey!), env: .TEST)
                IncidenceLibraryManager.setup(config)
                result["status"] =  "success"
            }else if(environment! == "pre"){
                let config = IncidenceLibraryConfig(apiKey: .init(apikey!), env: .PRE)
                IncidenceLibraryManager.setup(config)
                result["status"] =  "success"
            }else if(environment! == "pro"){
                let config = IncidenceLibraryConfig(apiKey: .init(apikey!), env: .PRO)
                IncidenceLibraryManager.setup(config)
                result["status"] =  "success"
            }else{
                result["status"] =  "error"
                result["message"] = "Incorrect environment"
            }
        }else{
            result["status"] =  "error"
            result["message"] = "Incorrect apikey"
        }
        call.resolve(result);
    }

    @objc func addDevice(_ call: CAPPluginCall) {
        var result = [String: Any]()
        IncidencePlugin.callResponse = call;
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                let vc = IncidenceLibraryManager.shared.getDeviceCreateViewController(user: user as! User, vehicle: vehicle as! Vehicle, delegate: self)
                
                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                // result["status"] =  "success"
                // call.resolve(result);
            }
        }
    }
    
    @objc func checkDevice(_ call: CAPPluginCall) {
        
        var result = [String: Any]()

        IncidencePlugin.callResponse = call;
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
            print("Error no policyNumber")
            //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
            print("Error no policyEnd")
            //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                let vc = IncidenceLibraryManager.shared.getDeviceReviewViewController(user: user as! User, vehicle: vehicle as! Vehicle, delegate: self)
                //, delegate: self
                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                // result["status"] =  "success"
                // call.resolve(result);
            }
        }
    }  

    @objc func deleteDevice(_ call: CAPPluginCall) {
        var result = [String: Any]()
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            IncidenceLibraryManager.shared.deleteBeaconFunc(user: user, vehicle: vehicle, completion: { resultFunction in
                print(resultFunction)
                if (resultFunction.status) {
                    result["status"] =  "success"
                } else {
                    result["status"] =  "error"
                    result["message"] = resultFunction.message
                }
                call.resolve(result);
            })
        }
    }

    @objc func getDevice(_ call: CAPPluginCall) {
        var result = [String: Any]()
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            IncidenceLibraryManager.shared.hasBeaconFunc(user: user, vehicle: vehicle, completion: { resultFunction in
                print(resultFunction)
                if (resultFunction.status) {
                    result["status"] =  "success"
                    result["device"] = true
                } else {
                    result["status"] =  "success"
                    result["device"] = false
                    
                }
                result["message"] = resultFunction.message
                call.resolve(result);
            })
        }
    }

    @objc func createIncidenceFlow(_ call: CAPPluginCall) {
        var result = [String: Any]()
        IncidencePlugin.callResponse = call;
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                let vc = IncidenceLibraryManager.shared.getReportIncSimpViewController(user: user as! User, vehicle: vehicle as! Vehicle, delegate: self)

                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                // result["status"] =  "success"
                // call.resolve(result);
            }
        }
    }

    public func onResult(response: IncidenceIOSSDK.IActionResponse) {
        print("Reponse onResult")
        print(response)
        var result = [String: Any]()
        if (response.status) {
            result["status"] =  "success"
            result["message"] =  response.message!
            if(response.data != nil){
                let data = response.data as! Incidence?
                result["externalId"] = data?.incidenceType?.externalId;
                result["street"] = data?.street
                result["city"] = data?.city;
                result["country"] = data?.country;
                result["latitude"] = data?.latitude;
                result["longitude"] = data?.longitude;
                result["externalIncidenceId"] = data?.externalIncidenceId;
            }
        } else {
            result["status"] =  "error"
        }
        IncidencePlugin.callResponse?.resolve(result);
    }
    
    @objc func createIncidence(_ call: CAPPluginCall) {
        var result = [String: Any]()
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        let incidenceCall = call.getObject("incidence")
        
        print("incidenceCall")
        print(incidenceCall)
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        let incidenceType = IncidenceType();
        if let incidenceCall = incidenceCall, let typeExternalId = incidenceCall["typeExternalId"] as? String {
            incidenceType.externalId = typeExternalId
        }else{
            print("Error no incidence externalId")
            error = true
        }
        
        let incidence = Incidence();
        incidence.incidenceType = incidenceType;
        if let incidenceCall = incidenceCall, let street = incidenceCall["street"] as? String {
            incidence.street = street
        }else{
            print("Error no incidence street")
            error = true
        }
        if let incidenceCall = incidenceCall, let city = incidenceCall["city"] as? String {
            incidence.city = city
        }else{
            print("Error no incidence city")
            error = true
        }
        if let incidenceCall = incidenceCall, let country = incidenceCall["country"] as? String {
            incidence.country = country
        }else{
            print("Error no incidence country")
            error = true
        }
        if let incidenceCall = incidenceCall, let latitude = incidenceCall["latitude"] as? String {
            incidence.latitude = Double(latitude)
        }else{
            print("Error no incidence latitude")
            error = true
        }
        if let incidenceCall = incidenceCall, let longitude = incidenceCall["longitude"] as? String {
            incidence.longitude = Double(longitude)
        }else{
            print("Error no incidence longitude")
            error = true
        }
        if let incidenceCall = incidenceCall, let externalIncidenceId = incidenceCall["externalIncidenceId"] as? String {
            incidence.externalIncidenceId = externalIncidenceId
        }else{
            print("Error no incidence externalIncidenceId")
            error = true
        }
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            IncidenceLibraryManager.shared.createIncidenceFunc(user: user, vehicle: vehicle, incidence: incidence, completion: { resultFunction in
                print(resultFunction)
                if (resultFunction.status) {
                    result["status"] =  "success"
                } else {
                    result["status"] =  "error"
                    result["message"] = resultFunction.message
                }
                call.resolve(result);
            })
        }
    }
    
    @objc func closeIncidence(_ call: CAPPluginCall) {
        var result = [String: Any]()
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        let incidenceCall = call.getObject("incidence")
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
           print("Error no policyNumber")
           //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
           print("Error no policyEnd")
           //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        let incidenceType = IncidenceType();
        if let incidenceCall = incidenceCall, let typeExternalId = incidenceCall["typeExternalId"] as? String {
            incidenceType.externalId = typeExternalId
        }else{
            print("Error no incidence externalId")
            error = true
        }
        
        let incidence = Incidence();
        incidence.incidenceType = incidenceType;
        if let incidenceCall = incidenceCall, let street = incidenceCall["street"] as? String {
            incidence.street = street
        }else{
            print("Error no incidence street")
            error = true
        }
        if let incidenceCall = incidenceCall, let city = incidenceCall["city"] as? String {
            incidence.city = city
        }else{
            print("Error no incidence city")
            error = true
        }
        if let incidenceCall = incidenceCall, let country = incidenceCall["country"] as? String {
            incidence.country = country
        }else{
            print("Error no incidence country")
            error = true
        }
        if let incidenceCall = incidenceCall, let latitude = incidenceCall["latitude"] as? String {
            incidence.latitude = Double(latitude)
        }else{
            print("Error no incidence latitude")
            error = true
        }
        if let incidenceCall = incidenceCall, let longitude = incidenceCall["longitude"] as? String {
            incidence.longitude = Double(longitude)
        }else{
            print("Error no incidence longitude")
            error = true
        }
        if let incidenceCall = incidenceCall, let externalIncidenceId = incidenceCall["externalIncidenceId"] as? String {
            incidence.externalIncidenceId = externalIncidenceId
        }else{
            print("Error no incidence externalIncidenceId")
            error = true
        }
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            IncidenceLibraryManager.shared.closeIncidenceFunc(user: user, vehicle: vehicle, incidence: incidence, completion: { resultFunction in
                print(resultFunction)
                if (resultFunction.status) {
                    result["status"] =  "success"
                } else {
                    result["status"] =  "error"
                    result["message"] = resultFunction.message
                }
                call.resolve(result);
            })
        }
    }  

    @objc func ecommerce(_ call: CAPPluginCall) {
        
        var result = [String: Any]()

        IncidencePlugin.callResponse = call;
        
        var error = false
        let userCall = call.getObject("user")
        let vehicleCall = call.getObject("vehicle")!
        
        let user = User();
        
        if let userCall = userCall, let externalUserId = userCall["externalUserId"] as? String {
            user.externalUserId = externalUserId
        }else{
           print("Error no externalUserId")
           error = true
        }
        
        if let userCall = userCall, let name = userCall["name"] as? String {
            user.name = name
        }else{
           print("Error no name")
           error = true
        }
        
        if let userCall = userCall, let phone = userCall["phone"] as? String {
            user.phone = phone
        }else{
           print("Error no phone")
           error = true
        }
        
        if let userCall = userCall, let email = userCall["email"] as? String {
            user.email = email
        }else{
           print("Error no email")
           error = true
        }
        
        let dniIdentityType = IdentityType();
        if let userCall = userCall, let identityType = userCall["identityType"] as? String {
            dniIdentityType.name = identityType
        }else{
            dniIdentityType.name = ""
           //print("Error no identityType")
           //error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
            user.dni = ""
           //print("Error no dni")
           //error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
            user.birthday = ""
           //print("Error no birthday")
           //error = true
        }
        
        if let userCall = userCall, let checkTerms = userCall["checkTerms"] as? String {
            user.checkTerms = Int(checkTerms)
        }else{
           print("Error no checkTerms")
           error = true
        }
        
        let vehicleType = VehicleType();
        if let vehicleTypeName = vehicleCall["vehicleType"] as? String {
            vehicleType.name = vehicleTypeName
        }else{
           print("Error no vehicleType")
           error = true
        }

        let color = ColorType();
        if let colorName = vehicleCall["color"] as? String {
            color.name = colorName
        }else{
            color.name = ""
           print("Error no color")
           //error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
            policy.policyNumber = ""
            print("Error no policyNumber")
            //error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
            policy.policyEnd = ""
            print("Error no policyEnd")
            //error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
            policy.dni = ""
            print("Error no dni")
            //error = true
        }
        policy.identityType = dniIdentityType
        
        let vehicle = Vehicle();
        if let externalVehicleId = vehicleCall["externalVehicleId"] as? String {
            vehicle.externalVehicleId = externalVehicleId
        }else{
           print("Error no externalVehicleId")
           error = true
        }
        if let licensePlate = vehicleCall["licensePlate"] as? String {
            vehicle.licensePlate = licensePlate
        }else{
           print("Error no licensePlate")
           error = true
        }
        if let registrationYear = vehicleCall["registrationYear"] as? String {
            vehicle.registrationYear = Int(registrationYear)
        }else{
            vehicle.registrationYear = 0
            print("Error no registrationYear")
           //error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
            vehicle.brand = ""
            print("Error no brand")
            //error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
            vehicle.model = ""
            print("Error no model")
            //error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                let vc = IncidenceLibraryManager.shared.getEcommerceViewController(user: user as! User, vehicle: vehicle as! Vehicle)
                //, delegate: self
                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                result["status"] =  "success"
                call.resolve(result);
            }
        }
    }  
}
