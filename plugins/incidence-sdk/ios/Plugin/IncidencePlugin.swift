import Foundation
import Capacitor
import os
import IncidenceLibraryIOS

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(IncidencePlugin)
public class IncidencePlugin: CAPPlugin {
    private let apiKey = "bWFwZnJlX2NsaWVudDpkOTBlMTA3ZjdhNGU1NmQyYzlkMTJhMHM3ZTQ1ZDQ1MQ=="
    
    static let shared: AppDelegate = {
        guard let appD = UIApplication.shared.delegate as? AppDelegate else {
          return AppDelegate()
        }
        return appD
      }()

    var window: UIWindow?

    @objc func initConfig(_ call: CAPPluginCall) {
        let config = IncidenceLibraryConfig(apiKey: .init(apiKey), env: .PRE)
        IncidenceLibraryManager.setup(config)
        var result = [String: Any]()
        result["status"] =  "success"
        call.resolve(result);
    }

    @objc func addDevice(_ call: CAPPluginCall) {
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
           print("Error no identityType")
           error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
           print("Error no dni")
           error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
           print("Error no birthday")
           error = true
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
           print("Error no color")
           error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
           print("Error no policyNumber")
           error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
           print("Error no policyEnd")
           error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
           print("Error no dni")
           error = true
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
           print("Error no registrationYear")
           error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
           print("Error no brand")
           error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
           print("Error no model")
           error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                let vc = IncidenceLibraryManager.shared.getDeviceCreateViewController(user: user as! User, vehicle: vehicle as! Vehicle)
                
                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                result["status"] =  "success"
                call.resolve(result);
            }
        }
    }
    
    @objc func checkDevice(_ call: CAPPluginCall) {
        
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
           print("Error no identityType")
           error = true
        }
        user.identityType = dniIdentityType
        
        if let userCall = userCall, let dni = userCall["dni"] as? String {
            user.dni = dni
        }else{
           print("Error no dni")
           error = true
        }
        
        if let userCall = userCall, let birthday = userCall["birthday"] as? String {
            user.birthday = birthday
        }else{
           print("Error no birthday")
           error = true
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
           print("Error no color")
           error = true
        }
        
        let policy = Policy();
        if let policyNumber = vehicleCall["policyNumber"] as? String {
            policy.policyNumber = policyNumber
        }else{
           print("Error no policyNumber")
           error = true
        }
        if let policyEnd = vehicleCall["policyEnd"] as? String {
            policy.policyEnd = policyEnd
        }else{
           print("Error no policyEnd")
           error = true
        }
        if let dni = vehicleCall["dni"] as? String {
            policy.dni = dni
        }else{
           print("Error no dni")
           error = true
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
           print("Error no registrationYear")
           error = true
        }
        vehicle.vehicleType = vehicleType; // (tipo del vehículo)
        if let brand = vehicleCall["brand"] as? String {
            vehicle.brand = brand
        }else{
           print("Error no brand")
           error = true
        }
        if let model = vehicleCall["model"] as? String {
            vehicle.model = model
        }else{
           print("Error no model")
           error = true
        }
        vehicle.color = color; // (color del vehículo)
        vehicle.policy = policy;
        
        if error {
            result["status"] =  "error"
            result["message"] = "Missing parameters"
            call.resolve(result);
        }else{
            DispatchQueue.main.async {
                 let vc = IncidenceLibraryManager.shared.getDeviceReviewViewController(user: user as! User, vehicle: vehicle as! Vehicle)

                let navigationController = UINavigationController(rootViewController: vc)
                navigationController.modalPresentationStyle = .fullScreen
                self.bridge?.viewController?.present(navigationController, animated: true, completion: nil)

                result["status"] =  "success"
                call.resolve(result);
            }
        }
    }    
}
