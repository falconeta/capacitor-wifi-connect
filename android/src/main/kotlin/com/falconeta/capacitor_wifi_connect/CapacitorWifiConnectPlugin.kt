package com.falconeta.capacitor_wifi_connect

import com.getcapacitor.annotation.CapacitorPlugin
import com.falconeta.capacitor_wifi_connect.CapacitorWifiConnect
import com.getcapacitor.PluginMethod
import com.getcapacitor.PluginCall
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin

@CapacitorPlugin(name = "CapacitorWifiConnect")
class CapacitorWifiConnectPlugin : Plugin() {
    private val implementation = CapacitorWifiConnect()
    @PluginMethod
    fun echo(call: PluginCall) {
        val value = call.getString("value")
        val ret = JSObject()
        ret.put("value", implementation.echo(value!!))
        call.resolve(ret)
    }
}
