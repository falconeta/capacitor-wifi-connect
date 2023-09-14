import { PermissionState } from '@capacitor/core';
export interface CapacitorWifiConnectPlugin {
  checkPermission(): Promise<{ value: PermissionState }>;
  requestPermission(): Promise<{ value: PermissionState }>;
  disconnect(): Promise<{ value: boolean }>;

  /** 
   * ONLY ANDROID
   * returns the current SSID connected by Application
   * WARNING: app is restricted to 4 scans every 2 minutes
   * @since 5.1.0
   */
  getSSIDs(): Promise<{ value: string[], status: ConnectState }>

    /** 
   *
   * returns the current SSID connected by Application
   * @since 5.1.0
   */
  getAppSSID(): Promise<{ value: string, status: ConnectState }>;

  /**
   * iOS >= 14, Android >=9: 
   *
   * returns the current SSID connected by device
   * @since 5.1.0
   */
  getDeviceSSID(): Promise<{ value: string, status: ConnectState }>;

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
  WifiDisabled = -4,
  AppLocalizationPermission = -5,
  SystemLocalizationPermission = -6,
}
