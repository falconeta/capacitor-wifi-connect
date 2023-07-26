# @falconeta/capacitor-wifi-connect

plugin used for connect the device trhought Wifi also with prefix

## Install

```bash
npm install @falconeta/capacitor-wifi-connect
npx cap sync
```

# iOS Functions
For functionality, you need to note the following:

* Connect/Disconnect only works for iOS11+

* prefixConnect/securePrefixConnect only works for iOS13+

* Can't run in the simulator so you need to attach an actual device when building with xCode

* Will ensure 'HotspotConfiguration' and 'Wifi Information' capabilities are added to your xCode project

## API

<docgen-index>

* [`checkPermission()`](#checkpermission)
* [`requestPermission()`](#requestpermission)
* [`disconnect()`](#disconnect)
* [`getSSIDs()`](#getssids)
* [`getAppSSID()`](#getappssid)
* [`getDeviceSSID()`](#getdevicessid)
* [`connect(...)`](#connect)
* [`prefixConnect(...)`](#prefixconnect)
* [`secureConnect(...)`](#secureconnect)
* [`securePrefixConnect(...)`](#secureprefixconnect)
* [Type Aliases](#type-aliases)
* [Enums](#enums)

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
getSSIDs() => Promise<{ value: string[]; }>
```

**Returns:** <code>Promise&lt;{ value: string[]; }&gt;</code>

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
