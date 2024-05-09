import { PermissionState } from '@capacitor/core';
export interface CapacitorWifiConnectPlugin {
  /**
   * method that check if the app has autorization or not to use the location capability.
   * @since 1.0.0
   */
  checkPermission(): Promise<{ value: PermissionState }>;

  /**
   * method that request (if the status of authorization is prompt) autorization to use the location capability.
   * @since 1.0.0
   */
  requestPermission(): Promise<{ value: PermissionState }>;

  /**
   * method that disconnects from the wifi network if the network was connected to using one of the connect methods.
   * @since 1.0.0
   */
  disconnect(): Promise<{ value: boolean }>;

  /**
   * ONLY ANDROID
   * returns the current SSID connected by Application
   * WARNING: app is restricted to 4 scans every 2 minutes
   * @since 5.1.0
   */
  getSSIDs(): Promise<{ value: string[]; status: ConnectState }>;

  /**
   * returns the current SSID connected by Application
   * @since 5.1.0
   */
  getAppSSID(): Promise<{ value: string; status: ConnectState }>;

  /**
   * iOS >= 14, Android >=9:
   *
   * returns the current SSID connected by device
   * @since 5.1.0
   */
  getDeviceSSID(): Promise<{ value: string; status: ConnectState }>;

  /**
   * method attempts to connect to wifi matching explicitly the ssid parameter
   * WARNING: saveNetwork is enabled by default on iOS and cannot be disabled due Apple's bug. (https://forums.developer.apple.com/forums/thread/700612)
   * @since 1.0.0
   */
  connect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: ConnectState }>;

  /**
   * method attempts to connect to the nearest wifi network with the ssid prefix matching the ssidPrefix parameter.
   * WARNING: saveNetwork is enabled by default on iOS and cannot be disabled due Apple's bug. (https://forums.developer.apple.com/forums/thread/700612)
   * @since 1.0.0
   */
  prefixConnect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: ConnectState }>;

  /**
   * method attempts to connect to wifi matching explicitly the ssid parameter.
   * This will fail if the password doesn't match or the isWep parameter isn't set correctly.
   * Android does not support WEP Networks.
   * WARNING: saveNetwork is enabled by default on iOS and cannot be disabled due Apple's bug. (https://forums.developer.apple.com/forums/thread/700612)
   * @since 1.0.0
   */
  secureConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
    // isWpa3?: boolean; TODO: to be introduced on iOS
  }): Promise<{ value: ConnectState }>;

  /**
   * method attempts to connect to the nearest
   * wifi network with the ssid prefix matching the ssidPrefix parameter.
   * This will fail if the password doesn't match or the isWep parameter
   * isn't set correctly. Android does not support WEP Networks.
   * WARNING: saveNetwork is enabled by default on iOS and cannot be disabled due Apple's bug. (https://forums.developer.apple.com/forums/thread/700612)
   * @since 1.0.0
   */
  securePrefixConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
    // isWpa3?: boolean; TODO: to be introduced on iOS
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
