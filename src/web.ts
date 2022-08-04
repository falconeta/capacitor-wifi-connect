import { WebPlugin } from '@capacitor/core';

import type { CapacitorWifiConnectPlugin } from './definitions';

export class CapacitorWifiConnectPluginWeb
  extends WebPlugin
  implements CapacitorWifiConnectPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
