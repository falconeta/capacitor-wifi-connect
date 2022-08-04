import { registerPlugin } from '@capacitor/core';

import type { CapacitorWifiConnectPluginPlugin } from './definitions';

const CapacitorWifiConnectPlugin =
  registerPlugin<CapacitorWifiConnectPluginPlugin>(
    'CapacitorWifiConnectPlugin',
    {
      web: () =>
        import('./web').then(m => new m.CapacitorWifiConnectPluginWeb()),
    },
  );

export * from './definitions';
export { CapacitorWifiConnectPlugin };
