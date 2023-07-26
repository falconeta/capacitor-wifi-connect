#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(CapacitorWifiConnectPlugin, "CapacitorWifiConnect",
          CAP_PLUGIN_METHOD(checkPermission, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(requestPermission, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(disconnect, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(getAppSSID, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(getDeviceSSID, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(connect, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(prefixConnect, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(secureConnect, CAPPluginReturnPromise);
          CAP_PLUGIN_METHOD(securePrefixConnect, CAPPluginReturnPromise);
)
