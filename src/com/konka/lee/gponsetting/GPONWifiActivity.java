package com.konka.lee.gponsetting;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.JsonObject;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GPONWifiActivity extends Activity implements
		GPONServerEventListener, View.OnClickListener {

	private GPONServerManager mServer = GPONServerManager.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mcontext=this;
		initview();
		iswhilereadwifi = true;
	}

	EditText inputssid, inputpasswd, inputchannel, input_ping_path, input_path;
	Button btn_savewifi, btn_gpon_reboot, btn_gpon_reset, btn_ping,
			btn_tracert,btn_getpon,btn_getwan,btn_getvoip;
	CheckBox cb_showpsw;
	TextView tv_devinfo_name, tv_devinfo_mode, tv_devinfo_hwver,
			tv_devinfo_swver, tv_devinfo_devmac, tv_devinfo_gponsn,
			tv_path_state,tv_devinfo_runtime,tv_ott_version;
	LinearLayout layout_1,layout_2,layout_3,layout_4;
    Button btn_menu_1,btn_menu_2,btn_menu_3,btn_menu_4;
    Context mcontext;
    
    EditText input_wanindex,input_tracert_wanindex,input_ping_wanindex;
	private void initview() {
		btn_menu_1=(Button) findViewById(R.id.btn_menu_1);
		btn_menu_2=(Button) findViewById(R.id.btn_menu_2);
		btn_menu_3=(Button) findViewById(R.id.btn_menu_3);
		btn_menu_4=(Button) findViewById(R.id.btn_menu_4);
		
		layout_1=(LinearLayout) findViewById(R.id.layout_device_state);
		layout_2=(LinearLayout) findViewById(R.id.layout_wifi_state);
		layout_3=(LinearLayout) findViewById(R.id.layout_tool);
		layout_4=(LinearLayout) findViewById(R.id.layout_other);
		
		tv_ott_version=(TextView) findViewById(R.id.tv_ott_version);
				
		inputssid = (EditText) findViewById(R.id.input_ssid);
		inputpasswd = (EditText) findViewById(R.id.input_passwd);
		inputchannel = (EditText) findViewById(R.id.input_channel);
		input_ping_path = (EditText) findViewById(R.id.input_ping_path);
		input_path = (EditText) findViewById(R.id.input_path);
		mServer.registerEventListener(this);
		btn_getvoip=(Button) findViewById(R.id.btn_getvoip);
		btn_getwan=(Button) findViewById(R.id.btn_getwan);
		btn_getpon=(Button) findViewById(R.id.btn_getpon);
		btn_savewifi = (Button) findViewById(R.id.btn_savewifi);
		btn_gpon_reboot = (Button) findViewById(R.id.btn_reboot);
		btn_gpon_reset = (Button) findViewById(R.id.btn_reset);
		btn_ping = (Button) findViewById(R.id.btn_ping);
		btn_tracert = (Button) findViewById(R.id.btn_tracert);
		tv_devinfo_name = (TextView) findViewById(R.id.tv_devinfo_name);
		tv_devinfo_mode = (TextView) findViewById(R.id.tv_devinfo_mode);
		tv_devinfo_hwver = (TextView) findViewById(R.id.tv_devinfo_hwver);
		tv_devinfo_swver = (TextView) findViewById(R.id.tv_devinfo_swver);
		tv_devinfo_devmac = (TextView) findViewById(R.id.tv_devinfo_devmac);
		tv_devinfo_gponsn = (TextView) findViewById(R.id.tv_devinfo_gponsn);
		tv_path_state = (TextView) findViewById(R.id.tv_path_state);
		tv_devinfo_runtime= (TextView) findViewById(R.id.tv_devinfo_runtime);
		btn_savewifi.setOnClickListener(this);
		btn_gpon_reboot.setOnClickListener(this);
		btn_gpon_reset.setOnClickListener(this);
		btn_getpon.setOnClickListener(this);
		btn_ping.setOnClickListener(this);
		btn_tracert.setOnClickListener(this);
		btn_getwan.setOnClickListener(this);
		btn_getvoip.setOnClickListener(this);
		btn_menu_1.setOnClickListener(this);
		btn_menu_2.setOnClickListener(this);
		btn_menu_3.setOnClickListener(this);
		btn_menu_4.setOnClickListener(this);
		
		cb_showpsw = (CheckBox) findViewById(R.id.show_passwd);
		cb_showpsw.setChecked(true);
		cb_showpsw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					inputpasswd
							.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());
				} else {
					inputpasswd
							.setTransformationMethod(PasswordTransformationMethod
									.getInstance());
				}
			}
		});
		tv_ott_version.setText(NetTool.getVersion());
		tv_ott_version.setSelected(true);
		
		input_wanindex=(EditText) findViewById(R.id.input_wanindex);
		input_tracert_wanindex=(EditText) findViewById(R.id.input_tracert_wanindex);
		input_ping_wanindex=(EditText) findViewById(R.id.input_ping_wanindex);
	}
	Handler timer=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				timer.removeMessages(0);
				dev_time_int++;
				tv_devinfo_runtime.setText(dev_time_int+"");
				timer.sendEmptyMessageDelayed(0, 1000);
			}
		};
	};
	boolean iswhilereadwifi = true;
	int mode = 0;
	String Ip="192.168.55.1";
	public  String ONU_SERVER = "http://192.168.55.1:8080/goform/";
	public  String ONU_POST_SETWAN=ONU_SERVER+"set_wan";
	public  String ONU_POST_GETWAN=ONU_SERVER+"get_wan_status";
	public  String ONU_POST_SETWIFI=ONU_SERVER+"set_wifi";
	public  String ONU_GET_WIFI=ONU_SERVER+"get_wifi";
	public  String ONU_POST_SETREBOOT=ONU_SERVER+"set_reboot";
	public  String ONU_POST_SETRESET=ONU_SERVER+"set_reset";
	public  String ONU_POST_SETPING=ONU_SERVER+"set_ping";
	public  String ONU_GET_PING=ONU_SERVER+"get_ping_result";
	public  String ONU_POST_SETTRACERT=ONU_SERVER+"set_tracert";
	public  String ONU_GET_TRACERT=ONU_SERVER+"get_tracert_result";
	public  String ONU_GET_PONSTATUS=ONU_SERVER+"get_pon_status";
	public  String ONU_GET_ETHSTATUS=ONU_SERVER+"get_eth_status";
	public  String ONU_GET_VOIPSTATUS=ONU_SERVER+"get_voip_status";
	public  String ONU_GET_DEVICEINFO=ONU_SERVER+"get_device_info";
	public  String ONU_POST_REBOOT=ONU_SERVER+"set_reboot";
	public  String ONU_POST_RESET=ONU_SERVER+"set_reset";
	Handler connectHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.what==0){
				NetworkInfo netIntfo =  cm.getActiveNetworkInfo();
				if(netIntfo!=null){
					Ip=NetTool.getGateway();
				    ONU_POST_SETWAN=ONU_SERVER+"set_wan";
					ONU_POST_GETWAN=ONU_SERVER+"get_wan_status";
					ONU_POST_SETWIFI=ONU_SERVER+"set_wifi";
					ONU_GET_WIFI=ONU_SERVER+"get_wifi";
					ONU_POST_SETREBOOT=ONU_SERVER+"set_reboot";
					ONU_POST_SETRESET=ONU_SERVER+"set_reset";
					ONU_POST_SETPING=ONU_SERVER+"set_ping";
					ONU_GET_PING=ONU_SERVER+"get_ping_result";
					ONU_POST_SETTRACERT=ONU_SERVER+"set_tracert";
				    ONU_GET_TRACERT=ONU_SERVER+"get_tracert_result";
					ONU_GET_PONSTATUS=ONU_SERVER+"get_pon_status";
					ONU_GET_ETHSTATUS=ONU_SERVER+"get_eth_status";
					ONU_GET_VOIPSTATUS=ONU_SERVER+"get_voip_status";
					ONU_GET_DEVICEINFO=ONU_SERVER+"get_device_info";
					ONU_POST_REBOOT=ONU_SERVER+"set_reboot";
					ONU_POST_RESET=ONU_SERVER+"set_reset";
					new Thread(new Runnable() {
		    			@Override
		    			public void run() {
		    				// while (iswhilereadwifi) {
		    				mode = 0;
							LogUtil.e("lipan","getInfo----------------");
		    				mServer.getInfo(ONU_GET_WIFI);
							LogUtil.e("lipan","getInfo----------------ONU_GET_WIFI");
		    				SystemClock.sleep(1000);
		    				mode = 1;
		    				mServer.getInfo(ONU_GET_DEVICEINFO);
							LogUtil.e("lipan","getInfo----------------ONU_GET_DEVICEINFO");
		    				// }
		    			}
		    		}).start();
				}else{
					connectHandler.removeMessages(0);
					connectHandler.sendEmptyMessageDelayed(0, 1000);
				}
			}
		};
	};
	ConnectivityManager cm;
	@Override
	protected void onResume() {
		super.onResume();
		NetworkInfo netIntfo = null;
        try {
		     cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		     netIntfo =  cm.getActiveNetworkInfo();
		} catch (Exception e) {
//		    Toast.makeText(this, "not net", Toast.LENGTH_LONG).show();
		}
		connectHandler.removeMessages(0);
        connectHandler.sendEmptyMessageDelayed(0, 500);
	}

	@Override
	protected void onStop() {
		iswhilereadwifi = false;
		super.onStop();
	}

	@Override
	public void onPostFailure(IOException e) {
		String failedinfo = e.getMessage();
		LogUtil.e("onPostFailure: " + failedinfo);
		e.printStackTrace();
		Message msg = mHandler.obtainMessage();
		msg.what = -1;
		msg.obj = failedinfo;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onPostResponse(String response) {
		LogUtil.e("onPostResponse: " + response);
		Message msg = mHandler.obtainMessage();
		msg.what = 1;
		msg.obj = response;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onGetFailure(IOException e) {
		String failedinfo = e.getMessage();
		LogUtil.e("onGetFailure: " + failedinfo);
		e.printStackTrace();
		Message msg = mHandler.obtainMessage();
		msg.what = -2;
		msg.obj = failedinfo;
		mHandler.sendMessage(msg);
	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					String data = (String) msg.obj;
					JSONObject jsonb = new JSONObject((String) msg.obj);
					if (data.contains("ssid1")) {
						String ssid = jsonb.getString("ssid1");
						if (!ssid.isEmpty()) {
							inputssid.setText(ssid);
						}
					}
					if (data.contains("password1")) {
						String passwd = jsonb.getString("password1");
						if (!passwd.isEmpty()) {
							inputpasswd.setText(passwd);
						}
					}
					if (data.contains("channel")) {
						String channel = jsonb.getString("channel");
						if (!channel.isEmpty()) {
							inputchannel.setText(channel);
						}
					}
					if (data.contains("name")) {
						String name = jsonb.getString("name");
						if (!name.isEmpty()) {
							tv_devinfo_name.setText(name);
						}
					}
					if (data.contains("mode")) {
						String mode = jsonb.getString("mode");
						if (!mode.isEmpty()) {
							tv_devinfo_mode.setText(mode);
						}
					}
					if (data.contains("hw_ver")) {
						String hw_ver = jsonb.getString("hw_ver");
						if (!hw_ver.isEmpty()) {
							tv_devinfo_hwver.setText(hw_ver);
						}
					}
					if (data.contains("sw_ver")) {
						String sw_ver = jsonb.getString("sw_ver");
						if (!sw_ver.isEmpty()) {
							tv_devinfo_swver.setText(sw_ver);
						}
					}
					if (data.contains("gpon_sn")) {
						String gpon_sn = jsonb.getString("gpon_sn");
						if (!gpon_sn.isEmpty()) {
							tv_devinfo_gponsn.setText(gpon_sn);
						}
					}
					if (data.contains("gpon_sn")) {
						String dev_mac = jsonb.getString("dev_mac");
						if (!dev_mac.isEmpty()) {
							tv_devinfo_devmac.setText(dev_mac);
						}
					}
					if (data.contains("gpon_sn")) {
						String dev_time = jsonb.getString("run_time");
						if (!dev_time.isEmpty()) {
							tv_devinfo_runtime.setText(dev_time);
							dev_time_int=Integer.parseInt(dev_time);
							timer.sendEmptyMessageDelayed(0, 1000);
						}
					}
					if(data.contains("voip_regstatus")){
						String voip_regstatus = jsonb.getString("voip_regstatus");
						String phonenumber= jsonb.getString("phonenumber");
						if (!voip_regstatus.isEmpty()) {
							tv_path_state.setText("voip_regstatus: "+voip_regstatus+"\n"+"phonenumber: "+phonenumber);
						}
					}
					if(data.contains("pon_regstatus")){
						String pon_regstatus=jsonb.getString("pon_regstatus");
						String tx_powoer="";
						try {
							tx_powoer=jsonb.getString("tx_powoer");
						} catch (Exception e) {
							tx_powoer=jsonb.getString("tx_power");
						}
						
						String rx_power=jsonb.getString("rx_power");
						String temperature=jsonb.getString("temperature");
						String supply_voltage=jsonb.getString("supply_voltage");
						String bias_current=jsonb.getString("bias_current");
						String tx_packs=jsonb.getString("tx_packs");
						String rx_packs=jsonb.getString("rx_packs");
						String tx_drop=jsonb.getString("tx_drop");
						String rx_drop=jsonb.getString("rx_drop");
						String error_total=jsonb.getString("error_total");
					    if(!pon_regstatus.isEmpty()){
					    	tv_path_state.setText("pon_regstatus: "+pon_regstatus+"\n"
					    						  +"tx_power: "+tx_powoer+"\n"
											      +"rx_power: "+rx_power+"\n"
											      +"temperature: "+temperature+"\n"
											      +"supply_voltage: "+supply_voltage+"\n"
											      +"bias_current: "+bias_current+"\n"
											      +"tx_packs: "+tx_packs+"\n"
											      +"rx_packs: "+rx_packs+"\n"
											      +"tx_drop: "+tx_drop+"\n"
											      +"rx_drop: "+rx_drop+"\n"
											      +"error_total: "+error_total+"\n"
					    						);
					    }
					}
					if (data.contains("result")) {
						String result = jsonb.getString("result");
						if (!result.isEmpty()) {
							LogUtil.e("TAG", "=======    "+result);
							Toast.makeText(GPONWifiActivity.this, result, 1000).show();
							if (result.contains("TracertFinished")) {
								istracert = false;
								String strcontent = jsonb.getString("content");
								if(!strcontent.isEmpty()){
									tv_path_state.setText(strcontent);
								}
								return;
							} else if (result.contains("PingFinished")) {
								isping = false;
								String strcontent = jsonb.getString("content");
								if(!strcontent.isEmpty()){
									tv_path_state.setText(strcontent);
								}
								return;
							}
							
							if(istracert||isping){
								testnum++;
								if(result.contains("TracertDoing")){
									String strcontent = jsonb.getString("content");
									if(!strcontent.isEmpty()){
										tv_path_state.setText(strcontent);
									}
								}else{
									tv_path_state.setText("");
								}
								if(testnum<5){
									mHandler.sendEmptyMessageDelayed(100, 5000);
								}else{
									testnum=0;
								}
							}
						}
					}
					
				} catch (JSONException e) {
					LogUtil.e("JSONException:" + e.getMessage());
					e.printStackTrace();
				}
				break;
			case 1:
				try {
					String data2 = (String) msg.obj;
					JSONObject jsonb = new JSONObject((String) msg.obj);
					String result = jsonb.getString("result");
					if (result.equals("success")) {
						mHandler.sendEmptyMessageDelayed(100, 5000);
					} else {
						mHandler.sendEmptyMessageDelayed(-100, 5000);
					}
					if(data2.contains("wan_index")){
						String wan_index=jsonb.getString("wan_index");
						String connect_mode=jsonb.getString("connect_mode");
						String wan_ip_mode=jsonb.getString("wan_ip_mode");
						String wan_ipv4=jsonb.getString("wan_ipv4");
						String wan_netmask_ipv4=jsonb.getString("wan_netmask_ipv4");
						String wan_gw_ipv4=jsonb.getString("wan_gw_ipv4");
						String wan_dns1_ipv4=jsonb.getString("wan_dns1_ipv4");
						String wan_dns2_ipv4=jsonb.getString("wan_dns2_ipv4");
						String username=jsonb.getString("username");
						String password=jsonb.getString("password");
					if(!connect_mode.isEmpty()){
						tv_path_state.setText(
								  "wan_index: "+wan_index+"\n"+
								  "connect_mode: "+connect_mode+"\n"
	    						  +"wan_ip_mode: "+wan_ip_mode+"\n"
							      +"wan_ipv4: "+wan_ipv4+"\n"
							      +"wan_netmask_ipv4: "+wan_netmask_ipv4+"\n"
							      +"wan_gw_ipv4: "+wan_gw_ipv4+"\n"
							      +"wan_dns1_ipv4: "+wan_dns1_ipv4+"\n"
							      +"wan_dns2_ipv4: "+wan_dns2_ipv4+"\n"
							      +"username: "+username+"\n"
							      +"password: "+password+"\n"
							      );
					}
					}
					Toast.makeText(GPONWifiActivity.this, result, 2000).show();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case 100:
				if (isping) {
					mServer.getInfo(ONU_GET_PING);
				}
				if (istracert) {
					mServer.getInfo(ONU_GET_TRACERT);
				}
				break;
			}
		};
	};

	int testnum=0;
	@Override
	public void onGetResponse(String response) {
		LogUtil.e("onGetResponse: " + response);
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		msg.obj = response;
		mHandler.sendMessage(msg);
	}

	boolean isping = false;
	boolean istracert = false;
    int dev_time_int=0;
	@Override
	public void onClick(View v) {
		isping = false;
		istracert = false;
		switch (v.getId()) {
		case R.id.btn_menu_1:
			layout_1.setVisibility(View.VISIBLE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.GONE);
			layout_4.setVisibility(View.GONE);
			connectHandler.sendEmptyMessage(0);
			break;
		case R.id.btn_menu_2:
			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.VISIBLE);
			layout_3.setVisibility(View.GONE);
			layout_4.setVisibility(View.GONE);
			connectHandler.sendEmptyMessage(0);
			break;
		case R.id.btn_menu_3:
			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.VISIBLE);
			layout_4.setVisibility(View.GONE);
			break;
		case R.id.btn_menu_4:
			layout_1.setVisibility(View.GONE);
			layout_2.setVisibility(View.GONE);
			layout_3.setVisibility(View.GONE);
			layout_4.setVisibility(View.VISIBLE);
			break;
			
		case R.id.btn_getvoip:
			new Thread(new Runnable() {
				@Override
				public void run() {
					LogUtil.e("TAG", "------------------");
					mServer.getInfo(ONU_GET_VOIPSTATUS);
				}
			}).start();
			break;
		case R.id.btn_getwan:
			JsonObject wan = new JsonObject();
			String wanindex=input_wanindex.getText().toString();
			int index=Integer.parseInt(wanindex);
			if(index>0&7>index){
				wan.addProperty("wan_index", wanindex);
				try {
					mServer.postInfo(ONU_POST_GETWAN, wan.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				input_wanindex.requestFocus();
				Toast.makeText(mcontext, "input text 1-6", 2000).show();
			}			
			break;
		case R.id.btn_getpon:
			new Thread(new Runnable() {
				@Override
				public void run() {
					mServer.getInfo(ONU_GET_PONSTATUS);
				}
			}).start();
			break;
		case R.id.btn_ping:
			JsonObject ping = new JsonObject();
			String pingpath = input_ping_path.getText().toString();
			String pwanindex=input_ping_wanindex.getText().toString();
			int pindex=Integer.parseInt(pwanindex);
			if(pindex>0&7>pindex){
				ping.addProperty("wan_index", pwanindex);
				ping.addProperty("diag_addr", pingpath);
				isping = true;
				try {
					mServer.postInfo(ONU_POST_SETPING, ping.toString());
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.e(e.getMessage());
				}
			}else{
				input_ping_wanindex.requestFocus();
				Toast.makeText(mcontext, "input text 1-6", 2000).show();
			}
			break;
		case R.id.btn_tracert:
			JsonObject tracert = new JsonObject();
			String tracertpath = input_path.getText().toString();
			String twanindex=input_tracert_wanindex.getText().toString();
			int tindex=Integer.parseInt(twanindex);
			if(tindex>0&7>tindex){
				tracert.addProperty("wan_index", twanindex);
				tracert.addProperty("diag_addr", tracertpath);
				istracert = true;
				try {
					mServer.postInfo(ONU_POST_SETTRACERT,tracert.toString());
				} catch (IOException e) {
					e.printStackTrace();
					LogUtil.e(e.getMessage());
				}
			}else{
				input_tracert_wanindex.requestFocus();
				Toast.makeText(mcontext, "input text 1-6", 2000).show();
			}
			break;
		case R.id.btn_savewifi:
			JsonObject jsonObject = new JsonObject();
			String channelid = inputchannel.getText().toString();
			String ssid = inputssid.getText().toString();
			String passwd = inputpasswd.getText().toString();
			jsonObject.addProperty("enable", "1");
			jsonObject.addProperty("ssid1", ssid);
			jsonObject.addProperty("password1", passwd);
			jsonObject.addProperty("channel", channelid);
			try {
				mServer.postInfo(ONU_POST_SETWIFI,
						jsonObject.toString());
			} catch (IOException e) {
				e.printStackTrace();
				LogUtil.e(e.getMessage());
			}
			break;
		case R.id.btn_reboot:
			try {
				mServer.postInfo(ONU_POST_REBOOT, "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_reset:
			try {
				mServer.postInfo(ONU_POST_RESET, "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		default:
			break;
		}
	}

}
