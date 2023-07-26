import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(CapacitorWifiConnectPlugin)
public class CapacitorWifiConnectPlugin: CAPPlugin {
    private let implementation = CapacitorWifiConnect()

    @objc func checkPermission(_ call: CAPPluginCall) {
        implementation.checkPermission(resolve: call.resolve);
    }
    
    @objc func requestPermission(_ call: CAPPluginCall) {
        implementation.requestPermission(resolve: call.resolve, reject: call.reject);
    }
    
    @objc func disconnect(_ call: CAPPluginCall) {
        implementation.disconnect(resolve: call.resolve, reject: call.reject);
    }
    
    @objc func getAppSSID(_ call: CAPPluginCall) {
        implementation.getAppSSID(resolve: call.resolve, reject: call.reject);
    }
    
    @available(iOS 14.0, *)
    @objc func getDeviceSSID(_ call: CAPPluginCall) {
        implementation.getDeviceSSID(resolve: call.resolve, reject: call.reject);
    }
    
    @objc func connect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let saveNetwork = call.getBool("saveNetwork") ?? false;
        if(ssid != nil) {
            implementation.connect(ssid: ssid!, saveNetwork: saveNetwork, resolve: call.resolve, reject: call.reject);
        } else {
            call.reject("SSID is mandatory")
        }
    }
    
    @objc func prefixConnect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let saveNetwork =  call.getBool("saveNetwork") ?? false;
        if(ssid != nil) {
      
            implementation.prefixConnect(ssid: ssid!, saveNetwork: saveNetwork, resolve: call.resolve, reject: call.reject);
        } else {
            call.reject("SSID is mandatory")
        }
    }
    
    @objc func secureConnect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let password = call.getString("password");
        let saveNetwork = call.getBool("saveNetwork") ?? false;
        let isWep = call.getBool("isWep") ?? false;
        if(ssid != nil && password != nil) {
            implementation.secureConnect(ssid: ssid!, password: password!, saveNetwork: saveNetwork, isWep: isWep, resolve: call.resolve, reject: call.reject);
        } else {
            call.reject("SSID and password are mandatory")
        }
    }
    
    @objc func securePrefixConnect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let password = call.getString("password");
        let saveNetwork = call.getBool("saveNetwork") ?? false;
        let isWep = call.getBool("isWep") ?? false;
        if(ssid != nil && password != nil) {
            
            implementation.securePrefixConnect(ssid:ssid!, password:password!, saveNetwork:saveNetwork, isWep: isWep, resolve: call.resolve, reject: call.reject);
        } else {
            call.reject("SSID and password are mandatory")
        }
    }
}
