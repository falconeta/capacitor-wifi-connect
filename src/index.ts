import { registerPlugin } from '@capacitor/core';

import type { CapacitorWifiConnectPlugin } from './definitions';

const CapacitorWifiConnect =
  registerPlugin<CapacitorWifiConnectPlugin>(
    'CapacitorWifiConnect',
    {
      web: () =>
        import('./web').then(m => new m.CapacitorWifiConnectPluginWeb()),
    },
  );

export * from './definitions';
export { CapacitorWifiConnect };
