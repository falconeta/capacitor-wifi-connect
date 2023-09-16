package com.falconeta.capacitor_wifi_connect

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.LocationManager
import android.net.*
import android.net.ConnectivityManager.NetworkCallback
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.PatternMatcher
import android.os.PatternMatcher.PATTERN_PREFIX
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall


class CapacitorWifiConnect(context: Context) : LifecycleObserver {

  private var _context: Context = context
//  private val TAG = "CapacitorWifiConnect"


  // holds the call while connected using ConnectivityManager.requestNetwork API
  private var networkCallback: NetworkCallback? = null

  // holds the network id returned by WifiManager.addNetwork, required to disconnect (API < 29)
  private var networkId: Int? = null

  private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  }

  private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
  }

  fun getAppSSID(): String = wifiManager.connectionInfo.ssid.removeSurrounding("\"")

  @SuppressLint("MissingPermission")
  fun getDeviceSSID(): String = wifiManager.connectionInfo.ssid.removeSurrounding("\"")

  fun disconnect(
    call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6)
      call.resolve(ret)
      return
    }

    when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
        val ret = JSObject()
        ret.put("value", execDisconnect())
        call.resolve(ret)
        return
      }
      else -> {
        execDisconnectLegacy(call)
        return
      }
    }
  }

  fun connect(
    ssid: String,
    call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6)
      call.resolve(ret)
      return
    }

    if(!wifiManager.isWifiEnabled) {
      val ret = JSObject()
      ret.put("value", -4)
      call.resolve(ret)
      return
    }

    ssid.let {
      when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
          val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(it)
            .build()
          execConnect(specifier, call)
          return
        }
        else -> {
          val wifiConfig = createWifiConfig(it)
          execConnect(wifiConfig, call)
          return
        }
      }
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun prefixConnect(
    ssid: String,
    call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6)
      call.resolve(ret)
      return
    }

    if(!wifiManager.isWifiEnabled) {
      val ret = JSObject()
      ret.put("value", -4)
      call.resolve(ret)
      return
    }

    ssid.let {
      when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
          val specifier = WifiNetworkSpecifier.Builder()
            // .setBssidPattern(MacAddress.fromString("7c:df:a1:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
            .setSsidPattern(PatternMatcher(it, PATTERN_PREFIX))
            .build()
          execConnect(specifier, call)
          return
        }
        else -> {
          val wifiConfig = createWifiConfig(it)
          connectByPrefix(it, wifiConfig, call)
          return
        }
      }
    }
  }

  fun secureConnect(
    ssid: String,
    password: String,
    isWep: Boolean,
    isWpa3: Boolean,
    call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6)
      call.resolve(ret)
      return
    }

    if(!wifiManager.isWifiEnabled) {
      val ret = JSObject()
      ret.put("value", -4)
      call.resolve(ret)
      return
    }

    if (isWep || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      val wifiConfig = isWep.let {
        if (it) {
          createWEPConfig(ssid, password)
        } else {
          createWifiConfig(ssid, password)
        }
      }
      execConnect(wifiConfig, call)
      return
    }
    val specifier = WifiNetworkSpecifier.Builder()
      .setSsid(ssid)
      .apply {
        if (isWpa3) {
          setWpa3Passphrase(password)
        } else {
        setWpa2Passphrase(password)
        }
      }
      .build()
    execConnect(specifier, call)
    return
  }

  @RequiresApi(Build.VERSION_CODES.M)
  fun securePrefixConnect(
    ssid: String,
    password: String,
    isWep: Boolean,
    isWpa3: Boolean,
    call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6)
      call.resolve(ret)
      return
    }

    if(!wifiManager.isWifiEnabled) {
      val ret = JSObject()
      ret.put("value", -4)
      call.resolve(ret)
      return
    }

    if (isWep || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      val wifiConfig = when {
        isWep -> {
          createWEPConfig(ssid, password)
        }
        else -> {
          createWifiConfig(ssid, password)
        }
      }

      connectByPrefix(ssid, wifiConfig, call)
      return
    }
    val specifier = WifiNetworkSpecifier.Builder()
      .setSsidPattern(PatternMatcher(ssid, PATTERN_PREFIX))
      .apply {
        if (isWpa3) {
          setWpa3Passphrase(password)
        } else {
        setWpa2Passphrase(password)
        }
      }
      .build()
    execConnect(specifier, call)
    return
  }

  @RequiresApi(Build.VERSION_CODES.M)
  @SuppressLint("MissingPermission")
  fun getSSIDs(call: PluginCall) {
    val wifiScanReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        val jsArray = JSArray()
        val jsObject = JSObject()
        if (success) {
          val ssids = wifiManager.scanResults
            .map { result -> result.SSID }
            .filter { ssid -> ssid !== "" }
            .distinct()

          for (ssid in ssids)
            jsArray.put(ssid)

          jsObject.put("status", 0)
        } else {
          jsObject.put("status", -2)
        }

        jsObject.put("value", jsArray)
        call.resolve(jsObject)

        context.unregisterReceiver(this)

      }
    }
    if(!wifiManager.isWifiEnabled) {
      val ret = JSObject()
      val jsArray = JSArray()
      ret.put("value", jsArray)
      ret.put("status", -4)
      call.resolve(ret)
      return
    }
    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    _context.registerReceiver(wifiScanReceiver, intentFilter)

    wifiManager.startScan()
  }

  @SuppressLint("MissingPermission")
  @Suppress("DEPRECATION")
  @RequiresApi(Build.VERSION_CODES.M)
  private fun connectByPrefix(
    ssidPrefix: String,
    config: WifiConfiguration,
    call: PluginCall
  ) {
    val wifiScanReceiver = object : BroadcastReceiver() {

      override fun onReceive(context: Context, intent: Intent) {
        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
        if (success) {
          val ssid = getNearbySsid(ssidPrefix)
          when {
            ssid != null -> {
              execConnect(config.apply {
                SSID = "\"" + ssid + "\""
              }, call)
            }
            else -> {
              val ret = JSObject()
              ret.put("value", -1)
              call.resolve(ret)
            }
          }
        } else {
          val ret = JSObject()
          ret.put("value", -2)
          call.resolve(ret)
        }
        context.unregisterReceiver(this)
      }
    }

    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    _context.registerReceiver(wifiScanReceiver, intentFilter)

    wifiManager.startScan()
  }

  @SuppressLint("MissingPermission")
  private fun getNearbySsid(ssidPrefix: String): String? {
    val results = wifiManager.scanResults
    return results.filter { scanResult -> scanResult.SSID.startsWith(ssidPrefix) }
      .maxByOrNull { scanResult -> scanResult.level }?.SSID
  }

  @Suppress("DEPRECATION")
  private fun createWifiConfig(ssid: String): WifiConfiguration {
    return WifiConfiguration().apply {
      SSID = "\"" + ssid + "\""
      allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)

      allowedProtocols.set(WifiConfiguration.Protocol.RSN)
      allowedProtocols.set(WifiConfiguration.Protocol.WPA)

      allowedAuthAlgorithms.clear()

      allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP)
      allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP)

      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP)
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP)
    }
  }

  @Suppress("DEPRECATION")
  private fun createWifiConfig(
    ssid: String,
    password: String
  ): WifiConfiguration {
    return createWifiConfig(ssid).apply {
      preSharedKey = "\"" + password + "\""
      status = WifiConfiguration.Status.ENABLED

      allowedKeyManagement.clear()
      allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
    }
  }

  @Suppress("DEPRECATION")
  private fun createWEPConfig(ssid: String, password: String): WifiConfiguration {
    return createWifiConfig(ssid).apply {
      wepKeys[0] = "\"" + password + "\""
      wepTxKeyIndex = 0

      allowedGroupCiphers.clear()
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104)

      allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
      allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
    }
  }

  @Suppress("DEPRECATION")
  private fun execConnect(wifiConfiguration: WifiConfiguration, call: PluginCall) {
    val network = wifiManager.addNetwork(wifiConfiguration)
    if (network == -1) {
      val ret = JSObject()
      ret.put("value", -1)
      call.resolve(ret)
      return
    }
    wifiManager.saveConfiguration()

    val wifiChangeReceiver = object : BroadcastReceiver() {
      var count = 0
      override fun onReceive(context: Context, intent: Intent) {
        count++
        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (info != null && info.isConnected) {
          if ((info.extraInfo == wifiConfiguration.SSID) || (getAppSSID() == wifiConfiguration.SSID)) {
            val ret = JSObject()
            ret.put("value", 0)
            call.resolve(ret)
            context.unregisterReceiver(this)
          } else if (count > 1) {
            // Ignore first callback if not success. It may be for the already connected SSID
            val ret = JSObject()
            ret.put("value", -1)
            call.resolve(ret)
            context.unregisterReceiver(this)
          }
        }
      }
    }

    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
    _context.registerReceiver(wifiChangeReceiver, intentFilter)

    // enable the new network and attempt to connect to it
    wifiManager.enableNetwork(network, true)
    networkId = network
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun execConnect(specifier: WifiNetworkSpecifier, call: PluginCall) {
    if (!wifiManager.isWifiEnabled) {
        val ret = JSObject()
        ret.put("value", -4)
        call.resolve(ret)

      return
    }

    if (networkCallback != null) {
      // there was already a connection, unregister to disconnect before proceeding
      connectivityManager.unregisterNetworkCallback(networkCallback!!)
      networkCallback = null
    }
    val request = NetworkRequest.Builder()
      .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
      .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .setNetworkSpecifier(specifier)
      .build()

    networkCallback = object : NetworkCallback() {
      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        connectivityManager.bindProcessToNetwork(network)
          Handler(Looper.getMainLooper()).postDelayed(
            {
              val ret = JSObject()
              ret.put("value", 0)
              call.resolve(ret)
            },
            100 // value in milliseconds
          )
        // cannot unregister callback here since it would disconnect from the network
      }

      override fun onUnavailable() {
        super.onUnavailable()
          Handler(Looper.getMainLooper()).postDelayed(
            {
              val ret = JSObject()
              ret.put("value", -1)
              call.resolve(ret)
            },
            100 // value in milliseconds
          )
      }
    }

    val handler = Handler(Looper.getMainLooper())
    connectivityManager.requestNetwork(request, networkCallback!!, handler)
  }

  @RequiresApi(Build.VERSION_CODES.Q)
  private fun execDisconnect(): Boolean {
    if (this.networkCallback == null) {
      return false
    }

    connectivityManager.unregisterNetworkCallback(this.networkCallback!!)
    connectivityManager.bindProcessToNetwork(null)
    this.networkCallback = null

    return true
  }

  @SuppressLint("MissingPermission")
  @Suppress("DEPRECATION")
  private fun execDisconnectLegacy(call: PluginCall) {
    val network = networkId
    if (network == null) {
      val ret = JSObject()
      ret.put("value", false)
      call.resolve(ret)
      return
    }
    val wifiChangeReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (!(info?.isConnected)!!) {
          val ret = JSObject()
          ret.put("value", true)
          call.resolve(ret)
          context.unregisterReceiver(this)
        }
      }
    }

    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION)
    _context.registerReceiver(wifiChangeReceiver, intentFilter)
    // remove network to emulate a behavior as close as possible to new Android API
    wifiManager.removeNetwork(network)
    wifiManager.reconnect()
    networkId = null
  }
}
