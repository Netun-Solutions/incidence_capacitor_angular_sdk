export interface IncidencePlugin {
  initConfig(options: {apikey: string, environment: string}): Promise<string>;
  addDevice(options: {user: any, vehicle: any}): Promise<string>;
  checkDevice(options: {user: any, vehicle: any}): Promise<string>;
  deleteDevice(options: {user: any, vehicle: any}): Promise<string>;
  getDevice(options: {user: any, vehicle: any}): Promise<string>;
  createIncidenceFlow(options: {user: any, vehicle: any}): Promise<string>;
  createIncidence(options: {user: any, vehicle: any, incidence: any}): Promise<string>;
  closeIncidence(options: {user: any, vehicle: any, incidence: any}): Promise<string>;
  ecommerce(options: {user: any, vehicle: any}): Promise<string>;
}
