# @falconeta/capacitor-wifi-connect

plugin used for connect the device trhought Wifi also with prefix

## Install

```bash
npm install @falconeta/capacitor-wifi-connect
npx cap sync
```

## Example usage
```typescript
import { CapacitorWifiConnect } from "@falconeta/capacitor-wifi-connect";

  async secureConnect() {
    let { value } = await CapacitorWifiConnect.checkPermission();
    if (value === 'prompt') {
      const data = await CapacitorWifiConnect.requestPermission();
      value = data.value;
    }
    if (value === 'granted') {
      CapacitorWifiConnect.secureConnect({
        ssid: 'SSID', 
        password: 'PWD', 
      }).then((data) => alert(data.value));
    } else {
      throw new Error('permission denied');
    }
  }
```

# iOS Functions
For functionality, you need to note the following:

* Connect/Disconnect only works for iOS11+

* prefixConnect/securePrefixConnect only works for iOS13+

* Can't run in the simulator so you need to attach an actual device when building with xCode

* Will ensure 'HotspotConfiguration' and 'Wifi Information' capabilities are added to your xCode project

* Will ensure that these permission are described in info.plist
```plist
  <key>NSLocationAlwaysAndWhenInUseUsageDescription</key>
  <string>...</string>
  <key>NSLocationAlwaysUsageDescription</key>
  <string>...</string>
  <key>NSLocationWhenInUseUsageDescription</key>
  <string>...</string>
  ```

## API

<docgen-index>

- [@falconeta/capacitor-wifi-connect](#falconetacapacitor-wifi-connect)
  - [Install](#install)
  - [Example usage](#example-usage)
- [iOS Functions](#ios-functions)
  - [API](#api)
    - [checkPermission()](#checkpermission)
    - [requestPermission()](#requestpermission)
    - [disconnect()](#disconnect)
    - [getSSIDs()](#getssids)
    - [getAppSSID()](#getappssid)
    - [getDeviceSSID()](#getdevicessid)
    - [connect(...)](#connect)
    - [prefixConnect(...)](#prefixconnect)
    - [secureConnect(...)](#secureconnect)
    - [securePrefixConnect(...)](#secureprefixconnect)
    - [Type Aliases](#type-aliases)
      - [PermissionState](#permissionstate)
    - [Enums](#enums)
      - [ConnectState](#connectstate)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermission()

```typescript
checkPermission() => Promise<{ value: PermissionState; }>
```

**Returns:** <code>Promise&lt;{ value: <a href="#permissionstate">PermissionState</a>; }&gt;</code>

--------------------


### requestPermission()

```typescript
requestPermission() => Promise<{ value: PermissionState; }>
```

**Returns:** <code>Promise&lt;{ value: <a href="#permissionstate">PermissionState</a>; }&gt;</code>

--------------------


### disconnect()

```typescript
disconnect() => Promise<{ value: boolean; }>
```

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### getSSIDs()

```typescript
getSSIDs() => Promise<{ value: string[]; status: ConnectState; }>
```

ONLY ANDROID
returns the current SSID connected by Application
WARNING: app is restricted to 4 scans every 2 minutes

**Returns:** <code>Promise&lt;{ value: string[]; status: <a href="#connectstate">ConnectState</a>; }&gt;</code>

**Since:** 5.1.0

--------------------


### getAppSSID()

```typescript
getAppSSID() => Promise<{ value: string; status: ConnectState; }>
```

returns the current SSID connected by Application

**Returns:** <code>Promise&lt;{ value: string; status: <a href="#connectstate">ConnectState</a>; }&gt;</code>

**Since:** 5.1.0

--------------------


### getDeviceSSID()

```typescript
getDeviceSSID() => Promise<{ value: string; status: ConnectState; }>
```

iOS &gt;= 14, Android &gt;=9: 

returns the current SSID connected by device

**Returns:** <code>Promise&lt;{ value: string; status: <a href="#connectstate">ConnectState</a>; }&gt;</code>

**Since:** 5.1.0

--------------------


### connect(...)

```typescript
connect(options: { ssid: string; saveNetwork?: boolean; }) => Promise<{ value: ConnectState; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ ssid: string; saveNetwork?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: <a href="#connectstate">ConnectState</a>; }&gt;</code>

--------------------


### prefixConnect(...)

```typescript
prefixConnect(options: { ssid: string; saveNetwork?: boolean; }) => Promise<{ value: ConnectState; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ ssid: string; saveNetwork?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: <a href="#connectstate">ConnectState</a>; }&gt;</code>

--------------------


### secureConnect(...)

```typescript
secureConnect(options: { ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }) => Promise<{ value: ConnectState; }>
```

| Param         | Type                                                                                     |
| ------------- | ---------------------------------------------------------------------------------------- |
| **`options`** | <code>{ ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: <a href="#connectstate">ConnectState</a>; }&gt;</code>

--------------------


### securePrefixConnect(...)

```typescript
securePrefixConnect(options: { ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }) => Promise<{ value: ConnectState; }>
```

| Param         | Type                                                                                     |
| ------------- | ---------------------------------------------------------------------------------------- |
| **`options`** | <code>{ ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: <a href="#connectstate">ConnectState</a>; }&gt;</code>

--------------------


### Type Aliases


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


### Enums


#### ConnectState

| Members                            | Value           |
| ---------------------------------- | --------------- |
| **`Ok`**                           | <code>0</code>  |
| **`Denied`**                       | <code>-1</code> |
| **`Ko`**                           | <code>-2</code> |
| **`UnknowSsid`**                   | <code>-3</code> |
| **`WifiDisabled`**                 | <code>-4</code> |
| **`AppLocalizationPermission`**    | <code>-5</code> |
| **`SystemLocalizationPermission`** | <code>-6</code> |

</docgen-api>
