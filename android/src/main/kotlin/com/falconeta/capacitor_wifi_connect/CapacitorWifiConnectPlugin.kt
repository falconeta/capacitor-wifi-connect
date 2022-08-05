package com.falconeta.capacitor_wifi_connect

import android.Manifest
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback


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
  fun disconnect(call: PluginCall) {
    val ret = JSObject()
    implementation.disconnect(call)
  }

  @PluginMethod
  fun getSSID(call: PluginCall) {
    val ret = JSObject()
    ret.put("value", implementation.getSSID())
    call.resolve(ret)
  }

  @PluginMethod
  fun connect(call: PluginCall) {
    val ssid = call.getString("ssid")

    if (ssid != null) {
      implementation.connect(ssid!!, call);
    } else {
      call.reject("SSID is mandatory")
    }
  }

  @PluginMethod
  fun prefixConnect(call: PluginCall) {
    if (getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_FINE_LOCATION,
        call,
        "prefixConnectPermsCallback"
      );
    }

    if (getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_COARSE_LOCATION,
        call,
        "prefixConnectPermsCallback"
      );
    }

    if (getPermissionState(PERMISSION_ACCESS_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_ACCESS_WIFI_STATE,
        call,
        "prefixConnectPermsCallback"
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_WIFI_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_WIFI_STATE,
        call,
        "prefixConnectPermsCallback"
      );
    }

    if (getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) != PermissionState.GRANTED) {
      return requestPermissionForAlias(
        PERMISSION_CHANGE_NETWORK_STATE,
        call,
        "prefixConnectPermsCallback"
      );
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
    if (
      getPermissionState(PERMISSION_ACCESS_FINE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_COARSE_LOCATION) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_ACCESS_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_WIFI_STATE) == PermissionState.GRANTED &&
      getPermissionState(PERMISSION_CHANGE_NETWORK_STATE) == PermissionState.GRANTED
    ) {
      prefixConnect(call);
    } else {
      call.reject("Permission is required")
    }
  }

  @PluginMethod
  fun secureConnect(call: PluginCall) {
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }

  fun securePrefixConnect(call: PluginCall) {
    val ssid = call.getString("ssid")
    val password = call.getString("password")
    val isWep = call.getBoolean("isWep") ?: false;
    if (ssid != null && password != null) {
      implementation.secureConnect(ssid!!, password!!, isWep, call);
    } else {
      call.reject("SSID and password are mandatory")
    }
  }
}
