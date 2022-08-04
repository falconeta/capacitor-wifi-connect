import Foundation

import NetworkExtension
import SystemConfiguration.CaptiveNetwork

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

    @objc public func connect(ssid: String, saveNetwork: Bool) -> Int {
        let hotspotConfig = NEHotspotConfiguration.init(ssid: ssid)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig);
    }

    @objc public func prefixConnect(ssid: String, saveNetwork: Bool) -> Int {
        let hotspotConfig = NEHotspotConfiguration.init(ssidPrefix: ssid)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig);
    }

    @objc public func secureConnect(ssid: String, password: String, saveNetwork: Bool, isWep: Bool) -> Int {
        let hotspotConfig = NEHotspotConfiguration.init(ssid: ssid, passphrase: password, isWEP: isWep)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig);

    }

    @objc public func securePrefixConnect(ssid: String, password: String, saveNetwork: Bool, isWep: Bool) -> Int {
        let hotspotConfig = NEHotspotConfiguration.init(ssidPrefix: ssid, passphrase: password, isWEP: isWep)
        hotspotConfig.joinOnce = !saveNetwork;
        return execConnect(hotspotConfig: hotspotConfig);
    }

    private func execConnect(hotspotConfig: NEHotspotConfiguration) -> Int {
        return 0;
//        NEHotspotConfigurationManager.shared.apply(hotspotConfig) { [weak self] (error) in
//
//            if let error = error as NSError? {
//                switch(error.code) {
//                case NEHotspotConfigurationError.alreadyAssociated.rawValue:
//                    return 0 // success
//                case NEHotspotConfigurationError.userDenied.rawValue:
//                    return -1 // button deny
//                default:
//                    return -2 // no connection
//                }
//            }
//            guard let this = self else {
//                return -3
//            }
//
//            if let currentSsid = this.getSSID(), currentSsid.hasPrefix(hotspotConfig.ssid){
//                return 0;
//            }
//            return 4
//        }
    }
}
