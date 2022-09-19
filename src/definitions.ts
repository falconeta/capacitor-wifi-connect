import { PermissionState } from '@capacitor/core';
export interface CapacitorWifiConnectPlugin {
  checkPermission(): Promise<{ value: PermissionState }>;
  requestPermission(): Promise<{ value: PermissionState }>;
  disconnect(): Promise<{ value: boolean }>;

  getSSID(): Promise<{ value: string }>;

  connect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: ConnectState }>;

  prefixConnect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: ConnectState }>;

  secureConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
  }): Promise<{ value: ConnectState }>;

  securePrefixConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
  }): Promise<{ value: ConnectState }>;
}

export enum ConnectState {
  Ok = 0,
  Denied = -1,
  Ko = -2,
  UnknowSsid = -3,
}
