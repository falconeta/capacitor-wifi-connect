package com.falconeta.capacitor_wifi_connect

import android.Manifest
import android.os.Build
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback


private const val PERMISSION_ACCESS_FINE_LOCATION = "access-fine-location";
private const val PERMISSION_ACCESS_COARSE_LOCATION = "access-coarse-location";
private const val PERMISSION_NEARBY_WIFI_DEVICES = "nearby-wifi-devices";
private const val PERMISSION_ACCESS_WIFI_STATE = "access-wifi-state";
private const val PERMISSION_CHANGE_WIFI_STATE = "change-wifi-state";
private const val PERMISSION_ACCESS_NETWORK_STATE = "access-network-state";
private const val PERMISSION_CHANGE_NETWORK_STATE = "change-network-state";


@CapacitorPlugin(
  name = "CapacitorWifiConnect",
  permissions = [
    Permission(
      alias = PERMISSION_ACCESS_FINE_LOCATION,
      strings = [Manifest.permission.ACCESS_FINE_LOCATION]
    ),
    Permission(
      alias = PERMISSION_ACCESS_COARSE_LOCATION,
      strings = [Manifest.permission.ACCESS_COARSE_LOCATION]
    ),
    Permission(
      alias = PERMISSION_NEARBY_WIFI_DEVICES,
      strings = ["android.permission.NEARBY_WIFI_DEVICES"]
    ),
    Permission(
      alias = PERMISSION_ACCESS_WIFI_STATE,
      strings = [Manifest.permission.ACCESS_WIFI_STATE]
    ),
    Permission(
      alias = PERMISSION_CHANGE_WIFI_STATE,
      strings = [Manifest.permission.CHANGE_WIFI_STATE]
    ),
    Permission(
      alias = PERMISSION_ACCESS_NETWORK_STATE,
      strings = [Manifest.permission.ACCESS_NETWORK_STATE]
    ),
    Permission(
      alias = PERMISSION_CHANGE_NETWORK_STATE,
      strings = [Manifest.permission.CHANGE_NETWORK_STATE]
    )
  ]
)
class CapacitorWifiConnectPlugin : Plugin() {
  private lateinit var implementation: CapacitorWifiConnect;

  override fun load() {
    super.load();
    implementation = CapacitorWifiConnect(context);
  }

  @PluginMethod
  fun checkPermission(call: PluginCall) {
    val ret = JSObject();

    if(isPermissionGranted()) {
      ret.put("value", PermissionState.GRANTED);
    } else if(isPermissionPrompt()) {
      ret.put("value", PermissionState.PROMPT);
    } else {
      ret.put("value", PermissionState.DENIED);
    }

    call.resolve(ret);
  }

  @PluginMethod
  fun requestPermission(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "requestPermissionCallback");
      return;
    }
    val ret = JSObject();
    ret.put("value", PermissionState.GRANTED);
    call.resolve(ret);
  }

  @PermissionCallback
  private fun requestPermissionCallback(call: PluginCall) {
    val ret = JSObject();
    if(isPermissionGranted()) {
      ret.put("value", PermissionState.GRANTED);
    } else if(isPermissionPrompt()) {
      ret.put("value", PermissionState.PROMPT);
    } else {
      ret.put("value", PermissionState.DENIED);
    }
    call.resolve(ret);
  }

  @PluginMethod
  fun disconnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "disconnectCallback");
      return;
    }
    val ret = JSObject()
    implementation.disconnect(call)
  }

  @PermissionCallback
  private fun disconnectCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      disconnect(call);
    } else {
        val ret = JSObject()
        ret.put("value", -5);
        call.resolve(ret)
    }
  }

  @PluginMethod
  fun getAppSSID(call: PluginCall) {
    // Reading SSID requires location permissions on ALL Android versions
    if (!isScanPermissionGranted()) {
      checkScanPermission(call, "getAppSSIDCallback");
      return;
    }
    val ret = JSObject()
    ret.put("value", implementation.getAppSSID())
    ret.put("status", 0)
    call.resolve(ret)
  }

  @PluginMethod
  fun getDeviceSSID(call: PluginCall) {
    // Reading SSID requires location permissions on ALL Android versions
    if (!isScanPermissionGranted()) {
      checkScanPermission(call, "getDeviceSSIDCallback");
      return;
    }
    val ret = JSObject()
    ret.put("value", implementation.getDeviceSSID())
    ret.put("status", 0)
    call.resolve(ret)
  }

  @PermissionCallback
  private fun getAppSSIDCallback(call: PluginCall) {
    if (isScanPermissionGranted()) {
      getAppSSID(call);
    } else {
      val ret = JSObject()
      ret.put("value", "");
      ret.put("status", -5);
      call.resolve(ret)
    }
  }

  @PermissionCallback
  private fun getDeviceSSIDCallback(call: PluginCall) {
    if (isScanPermissionGranted()) {
      getDeviceSSID(call);
    } else {
      val ret = JSObject()
      ret.put("value", "");
      ret.put("status", -5);
      call.resolve(ret)
    }
  }

  @PluginMethod
  fun connect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "connectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")

    if (ssid != null) {
      implementation.connect(ssid, call);
    } else {
      call.reject("SSID is mandatory")
    }
  }

  @PermissionCallback
  private fun connectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      connect(call);
    } else {
      val ret = JSObject()
      ret.put("value", -5);
      call.resolve(ret)
    }
  }

  @PluginMethod
  fun prefixConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "prefixConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")

    if (ssid == null) {
      call.reject("SSID is mandatory")
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      implementation.prefixConnect(ssid, call)
    } else {
      call.reject("Android SDK >= 23 compatible")
    }
  }

  @PermissionCallback
  private fun prefixConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      prefixConnect(call);
    } else {
      val ret = JSObject()
      ret.put("value", -5);
      call.resolve(ret)
    }
  }

  @PluginMethod
  fun secureConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "secureConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    val isWpa3 = call.getBoolean("isWpa3") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, isWpa3, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  @PermissionCallback
  private fun secureConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      secureConnect(call);
    } else {
      val ret = JSObject()
      ret.put("value", -5);
      call.resolve(ret)
    }
  }

  @PluginMethod
  fun getSSIDs(call: PluginCall) {
    // getSSIDs uses WiFi scanning which requires location permissions on ALL Android versions
    if (!isScanPermissionGranted()) {
      checkScanPermission(call, "getSSIDsPermsCallback");
      return;
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      implementation.getSSIDs(call)
    } else {
      call.reject("Android SDK >= 23 compatible")
    }

  }

  @PermissionCallback
  private fun getSSIDsPermsCallback(call: PluginCall) {
    if (isScanPermissionGranted()) {
      getSSIDs(call);
    } else {
      val ret = JSObject()
      ret.put("value", -2);
      call.resolve(ret)
    }
  }

  @PluginMethod
  fun securePrefixConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "securePrefixConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    val isWpa3 = call.getBoolean("isWpa3") ?: false;
    if (ssid != null && password != null) {
      implementation.securePrefixConnect(ssid!!, password!!, isWep, isWpa3, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  @PermissionCallback
  private fun securePrefixConnectPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      securePrefixConnect(call);
    } else {
      val ret = JSObject()
      ret.put("value", -5);
      call.resolve(ret)
    }
  }

  // Permission check for connect operations (connect, secureConnect, prefixConnect, securePrefixConnect)
  // On Android 13+, only NEARBY_WIFI_DEVICES is needed
  // On Android < 13, location permissions are needed
  private fun checkPermission(call: PluginCall, callbackName: String) {
    // For Android 13+, check NEARBY_WIFI_DEVICES instead of location permissions
    // For Android < 13, check ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION
    // Note: Android 12+ requires both FINE and COARSE location permissions
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      if (getPermissionState(PERMISSION_NEARBY_WIFI_DEVICES) != PermissionState.GRANTED) {
        return requestPermissionForAlias(
          PERMISSION_NEARBY_WIFI_DEVICES,
          call,
          callbackName
        );
      }
    } else {
      if (getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) != PermissionState.GRANTED) {
        return requestPermissionForAlias(
          PERMISSION_ACCESS_FINE_LOCATION,
          call,
          callbackName
        );
      }
      if (getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) != PermissionState.GRANTED) {
        return requestPermissionForAlias(
          PERMISSION_ACCESS_COARSE_LOCATION,
          call,
          callbackName
        );
      }
    }

    checkWifiPermissions(call, callbackName)
  }

  // Permission check for scan operations (getSSIDs)
  // Location permissions are ALWAYS required for WiFi scanning, even on Android 13+
  private fun checkScanPermission(call: PluginCall, callbackName: String) {
    // WiFi scanning (startScan, getScanResults) requires location permissions on ALL Android versions
    if (getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_FINE_LOCATION,
        call,
        callbackName
      );
    }
    if (getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_COARSE_LOCATION,
        call,
        callbackName
      );
    }

    checkWifiPermissions(call, callbackName)
  }

  // Check WiFi-related permissions (used by both connect and scan operations)
  private fun checkWifiPermissions(call: PluginCall, callbackName: String) {
    if (getPermissionState(PERMISSION_ACCESS_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_WIFI_STATE,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_WIFI_STATE,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_ACCESS_NETWORK_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_NETWORK_STATE,
        call,
        callbackName
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_NETWORK_STATE,
        call,
        callbackName
      );
    }
  }

  // Check if permissions for connect operations are granted
  private fun isPermissionGranted(): Boolean {
    val locationOrWifiPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getPermissionState(PERMISSION_NEARBY_WIFI_DEVICES) == PermissionState.GRANTED
    } else {
      // Android 12+ requires both FINE and COARSE location permissions
      getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) == PermissionState.GRANTED
    }

    return locationOrWifiPermission &&
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_NETWORK_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.GRANTED;
  }

  // Check if permissions for scan operations are granted (location always required)
  private fun isScanPermissionGranted(): Boolean {
    // WiFi scanning requires location permissions on ALL Android versions, including 13+
    return getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_NETWORK_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.GRANTED;
  }

  private fun isPermissionPrompt(): Boolean {
    val locationOrWifiPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      getPermissionState(PERMISSION_NEARBY_WIFI_DEVICES) == PermissionState.PROMPT
    } else {
      getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) == PermissionState.PROMPT
    }

    return locationOrWifiPermission ||
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_ACCESS_NETWORK_STATE) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.PROMPT;
  }

}
