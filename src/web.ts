import { WebPlugin } from '@capacitor/core';

import type { CapacitorWifiConnectPluginPlugin } from './definitions';

export class CapacitorWifiConnectPluginWeb
  extends WebPlugin
  implements CapacitorWifiConnectPluginPlugin
{
  async echo(options: { value: string }): Promise<{ value: string }> {
    console.log('ECHO', options);
    return options;
  }
}
