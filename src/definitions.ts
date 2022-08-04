export interface CapacitorWifiConnectPluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}
