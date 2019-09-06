package com.konka.lee.gponsetting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.View;

public class NetTool {
	public String WifigetIP(Context context, View v) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}

	private String intToIp(int i) {
		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
				+ "." + (i >> 24 & 0xFF);
	}

	public static String getGateway() {
		BufferedReader bufferedReader = null;
		String str2 = "";
		String str3 = "getprop  net.dns1";
		LogUtil.i("lipan", str3);
		Process exec;
		BufferedReader bufferedReader2 = null;
		try {
			exec = Runtime.getRuntime().exec(str3);
			try {
				bufferedReader2 = new BufferedReader(new InputStreamReader(
						exec.getInputStream()));
			} catch (Throwable th3) {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (exec != null) {
					exec.exitValue();
				}
			}
			try {
				str3 = bufferedReader2.readLine();
				if (str3 != null) {
					TextUtils.isEmpty(str3);
				}
				try {
					bufferedReader2.close();
				} catch (IOException iOException222) {
					iOException222.printStackTrace();
				}
				if (exec != null) {
					try {
						exec.exitValue();
					} catch (Exception e5) {
					}
				}
			} catch (IOException e6) {
				str3 = str2;
				if (bufferedReader2 != null) {
					bufferedReader2.close();
				}
				if (exec != null) {
					exec.exitValue();
				}
				return str3;
			}
		} catch (IOException e62) {
			bufferedReader2 = null;
			exec = null;
			str3 = str2;
			if (bufferedReader2 != null) {
				try {
					bufferedReader2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (exec != null) {
				exec.exitValue();
			}
			return str3;
		} catch (Throwable th4) {
			exec = null;
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (exec != null) {
				exec.exitValue();
			}
		}
		return str3;
	}

	public static String getVersion() {
		BufferedReader bufferedReader = null;
		String str2 = "";
		String str3 = "getprop ro.vendor.build.fingerprint";
		LogUtil.i("lipan", str3);
		Process exec;
		BufferedReader bufferedReader2 = null;
		try {
			exec = Runtime.getRuntime().exec(str3);
			try {
				bufferedReader2 = new BufferedReader(new InputStreamReader(
						exec.getInputStream()));
			} catch (Throwable th3) {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (exec != null) {
					exec.exitValue();
				}
			}
			try {
				str3 = bufferedReader2.readLine();
				if (str3 != null) {
					TextUtils.isEmpty(str3);
				}
				try {
					bufferedReader2.close();
				} catch (IOException iOException222) {
					iOException222.printStackTrace();
				}
				if (exec != null) {
					try {
						exec.exitValue();
					} catch (Exception e5) {
					}
				}
			} catch (IOException e6) {
				str3 = str2;
				if (bufferedReader2 != null) {
					bufferedReader2.close();
				}
				if (exec != null) {
					exec.exitValue();
				}
				return str3;
			}
		} catch (IOException e62) {
			bufferedReader2 = null;
			exec = null;
			str3 = str2;
			if (bufferedReader2 != null) {
				try {
					bufferedReader2.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (exec != null) {
				exec.exitValue();
			}
			return str3;
		} catch (Throwable th4) {
			exec = null;
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (exec != null) {
				exec.exitValue();
			}
		}
		return str3;
	}
}
