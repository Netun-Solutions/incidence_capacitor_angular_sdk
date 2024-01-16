import { WebPlugin } from '@capacitor/core';
import type { IncidencePlugin } from './definitions';

export class IncidenceWeb extends WebPlugin implements IncidencePlugin {
  async initConfig(): Promise<string> {
    return "ok";
  }
  async addDevice(options: {user: any, vehicle: any}): Promise<string> {
    console.log("addDevice", options);
    return "ok";
  }
  async checkDevice(options: {user: any, vehicle: any}): Promise<string> {
    console.log("checkDevice", options);
    return "ok";
  }
}