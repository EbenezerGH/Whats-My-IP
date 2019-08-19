package com.ipaddress

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.wifi.WifiManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {
    companion object {
        const val NO_CONNECTION_DETECTED = 0
        const val MOBILE_NETWORK_DETECTED = 1
        const val WIFI_NETWORK_DETECTED = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setIpAddressText()

        lbl_refresh.setOnClickListener {
            setIpAddressText()
        }
    }

    private fun setIpAddressText(){
        lbl_ip_address.text = when (this.getNetworkConnectionType()) {
            NO_CONNECTION_DETECTED -> getString(R.string.not_connected)
            MOBILE_NETWORK_DETECTED -> getString(R.string.connetected_mobile)
            WIFI_NETWORK_DETECTED -> retrieveWifiIpAddress()
            else -> ""
        }
    }

    private fun retrieveWifiIpAddress(): String? {
        val wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress

        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(ipAddress)
        }

        val ipByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()

        var ipAddressString: String?
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).hostAddress
        } catch (ex: UnknownHostException) {
            Toast.makeText(this, getString(R.string.bad_host_exception), Toast.LENGTH_LONG).show()
            ipAddressString = null
        }

        return ipAddressString
    }

}
