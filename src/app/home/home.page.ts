import { Component } from '@angular/core';
import { Incidence } from 'incidence-sdk';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  private apiKey: string = "aW8uaW9uaWMuc3RhcnRlcjpkOTBlMTA3ZjdhNGU1NmQyYzlkMTJhMHM3ZTQ1ZDQ1MQ==";
  private environment: string = "pre"; // pre / pro
  private user: any;
  private vehicle: any;
  private incidence: any;

  constructor() {
    this.user = {};
    this.user.externalUserId = "15001"; // (identificador externo del usuario)
    this.user.name = "Nombre TEST"; // (nombre del usuario)
    this.user.phone = "647893678"; // (teléfono)
    this.user.email = "sdkm1@tridenia.com"; // (e-mail)
    this.user.identityType = "dni";
    this.user.dni = "84283234X"; // (número del documento de identidad)
    this.user.birthday = "1979-09-29"; // (fecha de Nacimiento)
    this.user.checkTerms = "1"; // (aceptación de la privacidad)

    this.vehicle = {};
    this.vehicle.externalVehicleId = "15001";
    this.vehicle.licensePlate = "1511XXX"; // (matrícula del vehículo)
    this.vehicle.registrationYear = "2022"; // (fecha de matriculación)
    this.vehicle.vehicleType = "Coche"; // (tipo del vehículo)
    this.vehicle.brand = "Seat"; // (marca del vehículo)
    this.vehicle.model = "Laguna"; // (modelo del vehículo)
    this.vehicle.color = "Rojo"; // (color del vehículo)

    this.vehicle.policy = {};
    this.vehicle.policyNumber = "111111111"; // (número de la póliza)
    this.vehicle.policyEnd = "2024-10-09"; // (fecha caducidad de la póliza)
    this.vehicle.identityType = this.user.identityType; // (tipo de documento identidad del asegurador)
    this.vehicle.dni = this.user.dni; // (documento de identidad del asegurador)

    this.incidence = {};
    this.incidence.typeExternalId = "B0"; // Pinchazo
    this.incidence.street = "Carrer Major, 2";
    this.incidence.city = "Barcelona";
    this.incidence.country = "España";
    this.incidence.latitude = "41.4435945";
    this.incidence.longitude = "2.2319534";
    this.incidence.externalIncidenceId = "15001";
  }
  
  ionViewDidEnter(){

    Incidence.initConfig({apikey: this.apiKey, environment: this.environment}).then((res: any) => 
      alert(JSON.stringify(res))
    );
  }

  addDevice(){
    Incidence.addDevice({user: this.user,vehicle: this.vehicle}).then(res => 
      alert(JSON.stringify(res))
    );
  }

  checkDevice(){
    Incidence.checkDevice({user: this.user,vehicle: this.vehicle}).then(res => 
      alert(JSON.stringify(res))
    );
  }

  deleteDevice(){
    Incidence.deleteDevice({user: this.user,vehicle: this.vehicle}).then(res => 
      alert(JSON.stringify(res))
    );
  }

  createIncidence(){
    Incidence.createIncidence({user: this.user,vehicle: this.vehicle,incidence: this.incidence}).then(res => 
      alert(JSON.stringify(res))
    );
  }

  createIncidenceFlow(){
    Incidence.createIncidenceFlow({user: this.user,vehicle: this.vehicle}).then(res => 
      alert(JSON.stringify(res))
    );
  }

  closeIncidence(){
    Incidence.closeIncidence({user: this.user,vehicle: this.vehicle,incidence: this.incidence}).then(res => 
      alert(JSON.stringify(res))
    );
  }
}
