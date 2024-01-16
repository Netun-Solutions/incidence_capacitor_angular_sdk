export interface IncidencePlugin {
  initConfig(): Promise<string>;
  addDevice(options: {user: any, vehicle: any}): Promise<string>;
  checkDevice(options: {user: any, vehicle: any}): Promise<string>;
}
