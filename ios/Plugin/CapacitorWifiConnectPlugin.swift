import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(CapacitorWifiConnectPlugin)
public class CapacitorWifiConnectPlugin: CAPPlugin {
    private let implementation = CapacitorWifiConnect()

    @objc func disconnect(_ call: CAPPluginCall) {
        call.resolve([
            "value": implementation.disconnect()
        ])
    }

    @objc func getSSID(_ call: CAPPluginCall) {
        call.resolve([
            "value": implementation.getSSID() ?? ""
        ])
    }

    @objc func connect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let saveNetwork = call.getBool("saveNetwork") ?? false;
        if(ssid != nil) {
            call.resolve([
                "value": implementation.connect(ssid: ssid!, saveNetwork: saveNetwork)
            ])
        } else {
            call.reject("SSID is mandatory")
        }
    }

    @objc func prefixConnect(_ call: CAPPluginCall) {
        let ssid = call.getString("ssid");
        let saveNetwork =  call.getBool("saveNetwork") ?? false;
        if(ssid != nil) {
            call.resolve([
                "value": implementation.prefixConnect(ssid: ssid!, saveNetwork: saveNetwork)
            ])
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
            call.resolve([
                "value": implementation.secureConnect(ssid: ssid!, password: password!, saveNetwork: saveNetwork, isWep: isWep)
            ])
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
            call.resolve([
                "value": implementation.securePrefixConnect(ssid:ssid!, password:password!, saveNetwork:saveNetwork, isWep: isWep)
            ])
        } else {
            call.reject("SSID and password are mandatory")
        }
    }
}
