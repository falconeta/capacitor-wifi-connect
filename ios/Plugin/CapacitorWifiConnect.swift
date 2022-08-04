import Foundation

import NetworkExtension
import SystemConfiguration.CaptiveNetwork

public typealias PluginResultData = [String: Any]

@objc public class CapacitorWifiConnect: NSObject {
    @objc public func disconnect() -> Bool {
        let ssid: String? = getSSID()
        if(ssid == nil){
            return false
        }
        NEHotspotConfigurationManager.shared.removeConfiguration(forSSID: ssid ?? "")
        return true
    }

    @objc public func getSSID() -> String? {
        var ssid: String?
        if let interfaces = CNCopySupportedInterfaces() as NSArray? {
            for interface in interfaces {
                if let interfaceInfo = CNCopyCurrentNetworkInfo(interface as! CFString) as NSDictionary? {
                    ssid = interfaceInfo[kCNNetworkInfoKeySSID as String] as? String
                    break
                }
            }
        }
        return ssid
    }

    @objc public func connect(ssid: String, saveNetwork: Bool, resolve: @escaping (PluginResultData) -> Void) -> Void {
        let hotspotConfig = NEHotspotConfiguration.init(ssid: ssid)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig, resolve: resolve);
    }

    @objc public func prefixConnect(ssid: String, saveNetwork: Bool, resolve: @escaping (PluginResultData) -> Void) -> Void {
        let hotspotConfig = NEHotspotConfiguration.init(ssidPrefix: ssid)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig, resolve: resolve);
    }

    @objc public func secureConnect(ssid: String, password: String, saveNetwork: Bool, isWep: Bool, resolve: @escaping (PluginResultData) -> Void) -> Void {
        let hotspotConfig = NEHotspotConfiguration.init(ssid: ssid, passphrase: password, isWEP: isWep)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig, resolve: resolve);

    }

    @objc public func securePrefixConnect(ssid: String, password: String, saveNetwork: Bool, isWep: Bool, resolve: @escaping (PluginResultData) -> Void) -> Void {
        let hotspotConfig = NEHotspotConfiguration.init(ssidPrefix: ssid, passphrase: password, isWEP: isWep)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig, resolve: resolve);
    }

    private func execConnect(hotspotConfig: NEHotspotConfiguration, resolve: @escaping (PluginResultData) -> Void) -> Void {
        NEHotspotConfigurationManager.shared.apply(hotspotConfig) { [weak self] (error) in

            if let error = error as NSError? {
                switch(error.code) {
                case NEHotspotConfigurationError.alreadyAssociated.rawValue:
                    resolve(["value": 0]); // success
                    break
                case NEHotspotConfigurationError.userDenied.rawValue:
                    resolve(["value": -1]); // button deny
                    break
                default:
                    resolve(["value": -2]); // no connection
                    break
                }
                return
            }
            guard let this = self else {
                resolve(["value": -3]);
                return
            }

            if let currentSsid = this.getSSID(), currentSsid.hasPrefix(hotspotConfig.ssid){
                resolve(["value": 0]);
                return;
            }
            resolve(["value": -4]);
        }
    }
}
