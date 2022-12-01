package com.falconeta.capacitor_wifi_connect

import android.Manifest
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import kotlin.coroutines.suspendCoroutine


private const val PERMISSION_ACCESS_FINE_LOCATION = "access-fine-location";
private const val PERMISSION_ACCESS_COARSE_LOCATION = "access-coarse-location";
private const val PERMISSION_ACCESS_WIFI_STATE = "access-wifi-state";
private const val PERMISSION_CHANGE_WIFI_STATE = "change-wifi-state";
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
      alias = PERMISSION_ACCESS_WIFI_STATE,
      strings = [Manifest.permission.ACCESS_WIFI_STATE]
    ),
    Permission(
      alias = PERMISSION_CHANGE_WIFI_STATE,
      strings = [Manifest.permission.CHANGE_WIFI_STATE]
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
  fun getSSID(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "getSSIDCallback");
      return;
    }
    val ret = JSObject()
    ret.put("value", implementation.getSSID())
    call.resolve(ret)
  }

  @PermissionCallback
  private fun getSSIDCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      getSSID(call);
    } else {
      val ret = JSObject()
      ret.put("value", -5);
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
    if (ssid != null) {
      implementation.prefixConnect(ssid!!, call);
    } else {
      call.reject("SSID is mandatory")
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
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  @PluginMethod
  fun getSSIDs(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "getSSIDsPermsCallback");
      return;
    }

    implementation.getSSIDs(call)
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

  @PermissionCallback
  private fun getSSIDsPermsCallback(call: PluginCall) {
    if (isPermissionGranted()) {
      getSSIDs(call);
    } else {
      val ret = JSObject()
      ret.put("value", -2);
      call.resolve(ret)
    }
  }

  fun securePrefixConnect(call: PluginCall) {
    if (!isPermissionGranted()) {
      checkPermission(call, "securePrefixConnectPermsCallback");
      return;
    }
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
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

  private fun checkPermission(call: PluginCall, callbackName: String) {
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

    if (getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_NETWORK_STATE,
        call,
        callbackName
      );
    }

  }

  private fun isPermissionGranted(): Boolean {
    return getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.GRANTED;
  }

  private fun isPermissionPrompt(): Boolean {
    return getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.PROMPT ||
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.PROMPT;
  }

}
