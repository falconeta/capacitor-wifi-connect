export interface CapacitorWifiConnectPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
