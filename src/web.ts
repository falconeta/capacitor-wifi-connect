import { WebPlugin, PermissionState } from '@capacitor/core';

import type { CapacitorWifiConnectPlugin } from './definitions';

export class CapacitorWifiConnectPluginWeb
  extends WebPlugin
  implements CapacitorWifiConnectPlugin
{

  checkPermission(): Promise<{ value: PermissionState }>{
    throw new Error('Method not implemented.');
  }
  requestPermission(): Promise<{ value: PermissionState }>{
    throw new Error('Method not implemented.');
  }

  disconnect(): Promise<{ value: boolean }> {
    throw new Error('Method not implemented.');
  }
  getSSID(): Promise<{ value: string }> {
    throw new Error('Method not implemented.');
  }
  getSSIDs(): Promise<string[]> {
    throw new Error('Method not implemented.');
  }
  connect(options: {
    ssid: string;
    saveNetwork?: boolean | undefined;
  }): Promise<{ value: number }> {
    options;
    throw new Error('Method not implemented.');
  }
  prefixConnect(options: {
    ssid: string;
    saveNetwork?: boolean | undefined;
  }): Promise<{ value: number }> {
    options;
    throw new Error('Method not implemented.');
  }
  secureConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean | undefined;
    isWep?: boolean | undefined;
  }): Promise<{ value: number }> {
    options;
    throw new Error('Method not implemented.');
  }
  securePrefixConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean | undefined;
    isWep?: boolean | undefined;
  }): Promise<{ value: number }> {
    options;
    throw new Error('Method not implemented.');
  }
}
