export interface CapacitorWifiConnectPlugin {
  disconnect(): Promise<{ value: boolean }>;

  getSSID(): Promise<{ value: string }>;

  connect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: number }>;

  prefixConnect(options: {
    ssid: string;
    saveNetwork?: boolean;
  }): Promise<{ value: number }>;

  secureConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
  }): Promise<{ value: number }>;

  securePrefixConnect(options: {
    ssid: string;
    password: string;
    saveNetwork?: boolean;
    isWep?: boolean;
  }): Promise<{ value: number }>;
}
