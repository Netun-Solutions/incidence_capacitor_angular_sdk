import { WebPlugin } from '@capacitor/core';
import type { IncidencePlugin } from './definitions';

export class IncidenceWeb extends WebPlugin implements IncidencePlugin {
  async initConfig(options: {apikey: any, environment: any}): Promise<string> {
    console.log("initConfig", options);
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
  async deleteDevice(options: {user: any, vehicle: any}): Promise<string> {
    console.log("deleteDevice", options);
    return "ok";
  }
  async getDevice(options: {user: any, vehicle: any}): Promise<string> {
    console.log("getDevice", options);
    return "ok";
  }
  async createIncidenceFlow(options: {user: any, vehicle: any}): Promise<string> {
    console.log("createIncidenceFlow", options);
    return "ok";
  }
  async createIncidence(options: {user: any, vehicle: any, incidence: any}): Promise<string> {
    console.log("createIncidence", options);
    return "ok";
  }
  async closeIncidence(options: {user: any, vehicle: any, incidence: any}): Promise<string> {
    console.log("closeIncidence", options);
    return "ok";
  }
  async ecommerce(options: {user: any, vehicle: any}): Promise<string> {
    console.log("ecommerce", options);
    return "ok";
  }
}
