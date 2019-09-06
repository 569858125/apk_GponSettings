package com.konka.lee.gponsetting;

public class HUrlConfig {
	public static final String ONU_SERVER = "http://192.168.55.1:8080/goform/";
	public static final String ONU_POST_SETWAN=ONU_SERVER+"set_wan";
	//{"wan_index":"1","wan_mode":"PPPOE", "username":"test", "password":"test"}
	//RESPONSE:
	//reply: {"result":"success"} or {"result":"error"}
	public static final String ONU_POST_GETWAN=ONU_SERVER+"get_wan_status";
	//{"wan_index":"1"}
	//reply: {"wan_index":"1","wan_mode":"DHCP", "wan_ip":"", "wan_netmask":"", "wan_gw":"", "wan_dns1":"", "wan_dns2":"", "username":"", "password":""}
	public static final String ONU_POST_SETWIFI=ONU_SERVER+"set_wifi";
    //{"enable":"1", "ssid1":"GPONWIFI_XXXXXX", "password1":"12345678", "channel":"6"}
	//reply: {"result":"success"}|{"result":"error"}
	public static final String ONU_GET_WIFI=ONU_SERVER+"get_wifi";
	//reply: {"enable":"1", "ssid1":"GPON_XXXXXX", "password1":"12345678", "channel":"6","result":"success"}
	public static final String ONU_POST_SETREBOOT=ONU_SERVER+"set_reboot";
	//reply:{"result":"success"}|{"result":"error"}
	public static final String ONU_POST_SETRESET=ONU_SERVER+"set_reset";
	//reply:{"result":"success"}|{"result":"error"}
	public static final String ONU_POST_SETPING=ONU_SERVER+"set_ping";
	//{"wan_index":"1","diag_addr":"www.google.com"}
	//reply:{"result":"success"}|{"result":"error"}
	public static final String ONU_GET_PING=ONU_SERVER+"get_ping_result";
	//reply:{ "result": "PingFinished","content": 
	//"\nping -c 4 192.168.188.1 -I wan0\n
	//\nPING 192.168.188.1 (192.168.188.1): 56 data bytes\n
	//64 bytes from 192.168.188.1: seq=0 ttl=62 time=2.000 ms\n
	//64 bytes from 192.168.188.1: seq=1 ttl=62 time=1.000 ms\n
	//64 bytes from 192.168.188.1: seq=2 ttl=62 time=1.000 ms\n
	//64 bytes from 192.168.188.1: seq=3 ttl=62 time=1.000 ms\n
	//\n--- 192.168.188.1 ping statistics ---\n4 packets transmitted, 4 packets received, 0% packet loss\nround-trip min/avg/max = 1.000/1.250/2.000 ms\nPing finished.\n\n"}
	public static final String ONU_POST_SETTRACERT=ONU_SERVER+"set_tracert";
	//{"wan_index":"1","diag_addr":"www.google.com"}
	//reply:{"result":"success"}|{"result":"error"}
	public static final String ONU_GET_TRACERT=ONU_SERVER+"get_tracert_result";
	//reply:{"result": "TracertFinished","content": "\ntraceroute -n -I 192.168.188.1 -i wan0\n\ntraceroute to 192.168.188.1 (192.168.188.1), 30 hops max, 38 byte packets\n 1  192.168.0.1  2.000 ms  1.000 ms  2.000 ms\n 2  192.168.0.1  1.000 ms  1.000 ms  2.000 ms\n 3  192.168.188.1  1.000 ms  1.000 ms  2.000 ms\nTracert finished.\n\n"}
	public static final String ONU_GET_PONSTATUS=ONU_SERVER+"get_pon_status";
	//{"pon_regstatus":"Unregisted","tx_powoer":"2.13 dBm", "rx_power":"-18.48 dBm","temperature":"29.00 C", "supply_voltage":"3.38 V", "bias_current":"11 mA", "tx_packs":"0","rx_packs":"0","tx_drop":"0","rx_drop":"0","error_total":"0","result":"success"}
	public static final String ONU_GET_ETHSTATUS=ONU_SERVER+"get_eth_status";
	public static final String ONU_GET_VOIPSTATUS=ONU_SERVER+"get_voip_status";
	// {"LanList":[
	//{"eth_index":"1","mode":"Full-Duplex", "rate":"100M","eth_linkstatus":"Connected", "rx_packs":"0", "tx_packs":"0"},
	//{"eth_index":"2","mode":"Full-Duplex", "rate":"100M","eth_linkstatus":"Connected", "rx_packs":"0", "tx_packs":"0"},
	//{"eth_index":"3","mode":"Full-Duplex", "rate":"100M","eth_linkstatus":"Connected", "rx_packs":"0", "tx_packs":"0"},
	//],"result":"success"}
	public static final String ONU_GET_DEVICEINFO=ONU_SERVER+"get_device_info";
    //{"name":"KH383","mode":"GPON","hw_ver":"1.0","sw_ver":"V1.0",dev_mac":"11:22:33:44:55:66","gpon_sn":"KNOK12345678","run_time":"5509","result":"success"}
	public static final String ONU_POST_REBOOT=ONU_SERVER+"set_reboot";
	public static final String ONU_POST_RESET=ONU_SERVER+"set_reset";
	
}
