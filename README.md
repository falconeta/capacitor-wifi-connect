# @falconeta/capacitor-wifi-connect

plugin used for connect the device trhought Wifi also with prefix

## Install

```bash
npm install @falconeta/capacitor-wifi-connect
npx cap sync
```

iOS Functions
For functionality, you need to note the following:

* Connect/Disconnect only works for iOS11+

* prefixConnect/securePrefixConnect only works for iOS13+

* Can't run in the simulator so you need to attach an actual device when building with xCode

* Will ensure 'HotspotConfiguration' and 'Wifi Information' capabilities are added to your xCode project

## API

<docgen-index>

* [`disconnect()`](#disconnect)
* [`getSSID()`](#getssid)
* [`connect(...)`](#connect)
* [`prefixConnect(...)`](#prefixconnect)
* [`secureConnect(...)`](#secureconnect)
* [`securePrefixConnect(...)`](#secureprefixconnect)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### disconnect()

```typescript
disconnect() => Promise<{ value: boolean; }>
```

**Returns:** <code>Promise&lt;{ value: boolean; }&gt;</code>

--------------------


### getSSID()

```typescript
getSSID() => Promise<{ value: string; }>
```

**Returns:** <code>Promise&lt;{ value: string; }&gt;</code>

--------------------


### connect(...)

```typescript
connect(options: { ssid: string; saveNetwork?: boolean; }) => Promise<{ value: number; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ ssid: string; saveNetwork?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: number; }&gt;</code>

--------------------


### prefixConnect(...)

```typescript
prefixConnect(options: { ssid: string; saveNetwork?: boolean; }) => Promise<{ value: number; }>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code>{ ssid: string; saveNetwork?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: number; }&gt;</code>

--------------------


### secureConnect(...)

```typescript
secureConnect(options: { ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }) => Promise<{ value: number; }>
```

| Param         | Type                                                                                     |
| ------------- | ---------------------------------------------------------------------------------------- |
| **`options`** | <code>{ ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: number; }&gt;</code>

--------------------


### securePrefixConnect(...)

```typescript
securePrefixConnect(options: { ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }) => Promise<{ value: number; }>
```

| Param         | Type                                                                                     |
| ------------- | ---------------------------------------------------------------------------------------- |
| **`options`** | <code>{ ssid: string; password: string; saveNetwork?: boolean; isWep?: boolean; }</code> |

**Returns:** <code>Promise&lt;{ value: number; }&gt;</code>

--------------------

</docgen-api>
