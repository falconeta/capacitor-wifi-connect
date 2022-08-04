import Foundation

@objc public class CapacitorWifiConnectPlugin: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
