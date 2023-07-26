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
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import com.getcapacitor.JSArray
import com.getcapacitor.JSObject
import com.getcapacitor.PluginCall


class CapacitorWifiConnect : LifecycleObserver {

  private lateinit var _context: Context;
  private var isWifiConnected = false;

  constructor(context: Context) {
    _context = context;
    val nr = NetworkRequest.Builder()
      .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
      .build()

    val networkCallback: NetworkCallback = object : NetworkCallback() {
      override fun onAvailable(network: Network) {
        super.onAvailable(network);
        isWifiConnected = true;
      }

      override fun onLost(network: Network) {
        super.onLost(network)
        isWifiConnected = false;
      }
    }

    connectivityManager.registerNetworkCallback(
      nr,
      networkCallback
    );
  }


  // holds the call while connected using ConnectivityManager.requestNetwork API
  private var networkCallback: ConnectivityManager.NetworkCallback? = null

  // holds the network id returned by WifiManager.addNetwork, required to disconnect (API < 29)
  private var networkId: Int? = null

  private val connectivityManager: ConnectivityManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
  }

  private val locationManager: LocationManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
  }

  private val wifiManager: WifiManager by lazy(LazyThreadSafetyMode.NONE) {
    _context.getSystemService(Context.WIFI_SERVICE) as WifiManager
  }

  private var _call: PluginCall? = null;


  fun echo(value: String): String {
    Log.i("Echo", value)
    return value
  }

  @SuppressLint("MissingPermission")
  fun getAppSSID(): String = wifiManager.connectionInfo.ssid

  @SuppressLint("MissingPermission")
  fun getDeviceSSID(): String = wifiManager.connectionInfo.ssid

  fun disconnect(
    @NonNull call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6);
      call.resolve(ret)
      return
    }

    _call = call;
    when {
      Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
        val ret = JSObject()
        ret.put("value", execDisconnect());
        _call?.let { it.resolve(ret) };
        _call = null;
        return
      }
      else -> {
        execDisconnectLegacy()
        return
      }
    }
    return
  }

  fun connect(
    @NonNull ssid: String,
    @NonNull call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6);
      call.resolve(ret)
      return
    }

    if (!isWifiConnected) {
      val ret = JSObject()
      ret.put("value", -4);
      call.resolve(ret)
      return
    }

    _call = call;
    ssid.let {
      when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
          val specifier = WifiNetworkSpecifier.Builder()
            .setSsid(it)
            .build()
          execConnect(specifier)
          return
        }
        else -> {
          val wifiConfig = createWifiConfig(it)
          execConnect(wifiConfig)
          return
        }
      }
    }
  }

  fun prefixConnect(
    @NonNull ssid: String,
    @NonNull call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6);
      call.resolve(ret)
      return
    }

    if (!isWifiConnected) {
      val ret = JSObject()
      ret.put("value", -4);
      call.resolve(ret)
      return
    }

    _call = call;
    ssid.let {
      when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
          val specifier = WifiNetworkSpecifier.Builder()
            // .setBssidPattern(MacAddress.fromString("7c:df:a1:00:00:00"), MacAddress.fromString("ff:ff:ff:00:00:00"))
            .setSsidPattern(PatternMatcher(it, PATTERN_PREFIX))
            .build()
          execConnect(specifier)
          return
        }
        else -> {
          val wifiConfig = createWifiConfig(it)
          connectByPrefix(it, wifiConfig)
          return
        }
      }
    }
  }

  fun secureConnect(
    @NonNull ssid: String,
    @NonNull password: String,
    @NonNull isWep: Boolean,
    @NonNull call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6);
      call.resolve(ret)
      return
    }

    if (!isWifiConnected) {
      val ret = JSObject()
      ret.put("value", -4);
      call.resolve(ret)
      return
    }

    _call = call;
    if (isWep || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
      val wifiConfig = isWep.let {
        if (it) {
          createWEPConfig(ssid, password)
        } else {
          createWifiConfig(ssid, password)
        }
      }
      execConnect(wifiConfig)
      return
    }
    val specifier = WifiNetworkSpecifier.Builder()
      .setSsid(ssid)
      .apply {
//        if (isWpa3 != null && isWpa3) {
//          setWpa3Passphrase(password)
//        } else {
        setWpa2Passphrase(password)
//        }
      }
      .build()
    execConnect(specifier)
    return
  }


  fun securePrefixConnect(
    @NonNull ssid: String,
    @NonNull password: String,
    @NonNull isWep: Boolean,
    @NonNull call: PluginCall
  ) {

    val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    if (!networkEnabled) {
      val ret = JSObject()
      ret.put("value", -6);
      call.resolve(ret)
      return
    }

    if (!isWifiConnected) {
      val ret = JSObject()
      ret.put("value", -4);
      call.resolve(ret)
      return
    }

    _call = call;
    if (ssid == null || password == null || isWep == null) {
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

      connectByPrefix(ssid, wifiConfig)
      return
    }
    val specifier = WifiNetworkSpecifier.Builder()
      .setSsidPattern(PatternMatcher(ssid, PATTERN_PREFIX))
      .apply {
//        if (isWpa3 != null && isWpa3) {
//          setWpa3Passphrase(password)
//        } else {
        setWpa2Passphrase(password)
//        }
      }
      .build()
    execConnect(specifier)
    return
  }

  fun getSSIDs(call: PluginCall) {
    val wifiScanReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)

        if (success) {
          val ssids = wifiManager.scanResults
            .map { result -> result.SSID }
            .filter { ssid -> ssid !== "" }
            .distinct();


          val jsArray = JSArray()
          for (ssid in ssids)
            jsArray.put(ssid)

          val jsObject = JSObject()
          jsObject.put("value", jsArray)
          _call?.resolve(jsObject)
          _call = null
        } else {
          val ret = JSObject()
          ret.put("value", -1);
          _call?.resolve(ret);
          _call = null;
        }

        context.unregisterReceiver(this)
      }
    }
    _call = call;
    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    _context.registerReceiver(wifiScanReceiver, intentFilter)

    val success = wifiManager.startScan()
    if (!success) {
      _call?.reject("error on startScan");
      _call = null;
      _context.unregisterReceiver(wifiScanReceiver)
    }
  }

  @SuppressLint("MissingPermission")
  @Suppress("DEPRECATION")
  private fun connectByPrefix(
    @NonNull ssidPrefix: String,
    @NonNull config: WifiConfiguration
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
              })
            }
            else -> {
              val ret = JSObject()
              ret.put("value", -2);
              _call?.let { it.resolve(ret) };
              _call = null;
            }
          }
        } else {
          val ret = JSObject()
          ret.put("value", -1);
          _call?.let { it.resolve(ret) };
          _call = null;
        }
        context?.unregisterReceiver(this)
      }
    }

    val intentFilter = IntentFilter()
    intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    _context.registerReceiver(wifiScanReceiver, intentFilter)

    val success = wifiManager.startScan()
    if (!success) {
      _call?.let { it.reject("error on startScan") };
      _call = null;
      _context?.unregisterReceiver(wifiScanReceiver)
    }
  }

  @SuppressLint("MissingPermission")
  private fun getNearbySsid(@NonNull ssidPrefix: String): String? {
    val results = wifiManager.scanResults
    return results.filter { scanResult -> scanResult.SSID.startsWith(ssidPrefix) }
      .maxByOrNull { scanResult -> scanResult.level }?.SSID
  }

  @Suppress("DEPRECATION")
  private fun createWifiConfig(@NonNull ssid: String): WifiConfiguration {
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
    @NonNull ssid: String,
    @NonNull password: String
  ): WifiConfiguration {
    return createWifiConfig(ssid).apply {
      preSharedKey = "\"" + password + "\""
      status = WifiConfiguration.Status.ENABLED

      allowedKeyManagement.clear()
      allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK)
    }
  }

  @Suppress("DEPRECATION")
  private fun createWEPConfig(@NonNull ssid: String, @NonNull password: String): WifiConfiguration {
    return createWifiConfig(ssid).apply {
      wepKeys[0] = "\"" + password + "\""
      wepTxKeyIndex = 0

      allowedGroupCiphers.clear()
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
      allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

      allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN)
      allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED)
    }
  }

  @SuppressLint("MissingPermission")
  private fun execConnect(@NonNull wifiConfiguration: WifiConfiguration) {
    val network = wifiManager.addNetwork(wifiConfiguration)
    if (network == -1) {
      val ret = JSObject()
      ret.put("value", -1);
      _call?.let { it.resolve(ret) };
      _call = null;
      return
    }
    wifiManager.saveConfiguration()

    val wifiChangeReceiver = object : BroadcastReceiver() {
      var count = 0
      override fun onReceive(context: Context, intent: Intent) {
        count++;
        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (info != null && info.isConnected) {
          if (info.extraInfo == wifiConfiguration.SSID || getAppSSID() == wifiConfiguration.SSID) {
            val ret = JSObject()
            ret.put("value", 0);
            _call?.let { it.resolve(ret) };
            _call = null;
            context?.unregisterReceiver(this)
          } else if (count > 1) {
            // Ignore first callback if not success. It may be for the already connected SSID
            val ret = JSObject()
            ret.put("value", -1);
            _call?.let { it.resolve(ret) };
            _call = null;
            context?.unregisterReceiver(this)
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
  private fun execConnect(@NonNull specifier: WifiNetworkSpecifier) {
    if (!wifiManager.isWifiEnabled) {
      if (_call != null) {
        val ret = JSObject()
        ret.put("value", -4);
        _call?.let { it.resolve(ret) };
        _call = null;
      }
      return;
    }

    if (networkCallback != null) {
      // there was already a connection, unregister to disconnect before proceeding
      connectivityManager.unregisterNetworkCallback(networkCallback!!)
      networkCallback = null;
    }
    val request = NetworkRequest.Builder()
      .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
      .removeCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
      .setNetworkSpecifier(specifier)
      .build()

    networkCallback = object : ConnectivityManager.NetworkCallback() {
      override fun onAvailable(network: Network) {
        super.onAvailable(network)
        connectivityManager.bindProcessToNetwork(network)
        if (_call != null) {
          Handler().postDelayed(
            {
              val ret = JSObject()
              ret.put("value", 0);
              _call?.let { it.resolve(ret) };
              _call = null;
            },
            100 // value in milliseconds
          );
        } else {
          connectivityManager.unregisterNetworkCallback(this);
          networkCallback = null;
        }

        // cannot unregister callback here since it would disconnect form the network
      }

      override fun onUnavailable() {
        super.onUnavailable();
        if (_call != null) {
          Handler().postDelayed(
            {
              val ret = JSObject()
              ret.put("value", -1);
              _call?.let { it.resolve(ret) };
              _call = null;
            },
            100 // value in milliseconds
          );
        } else {
          connectivityManager.unregisterNetworkCallback(this);
          networkCallback = null;
        }

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
  private fun execDisconnectLegacy() {
    val network = networkId
    if (network == null) {
      val ret = JSObject()
      ret.put("value", false);
      _call?.let { it.resolve(ret) };
      _call = null;
      return
    }
    val wifiChangeReceiver = object : BroadcastReceiver() {
      override fun onReceive(context: Context, intent: Intent) {
        val info = intent.getParcelableExtra<NetworkInfo>(WifiManager.EXTRA_NETWORK_INFO)
        if (info != null && !info.isConnected) {
          val ret = JSObject()
          ret.put("value", true);
          _call?.let { it.resolve(ret) };
          _call = null;
          context?.unregisterReceiver(this)
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
