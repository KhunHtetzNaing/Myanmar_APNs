package com.htetznaing.mmanps;


import anywheresoftware.b4a.B4AMenuItem;
import android.app.Activity;
import android.os.Bundle;
import anywheresoftware.b4a.BA;
import anywheresoftware.b4a.BALayout;
import anywheresoftware.b4a.B4AActivity;
import anywheresoftware.b4a.ObjectWrapper;
import anywheresoftware.b4a.objects.ActivityWrapper;
import java.lang.reflect.InvocationTargetException;
import anywheresoftware.b4a.B4AUncaughtException;
import anywheresoftware.b4a.debug.*;
import java.lang.ref.WeakReference;

public class mptcdma extends Activity implements B4AActivity{
	public static mptcdma mostCurrent;
	static boolean afterFirstLayout;
	static boolean isFirst = true;
    private static boolean processGlobalsRun = false;
	BALayout layout;
	public static BA processBA;
	BA activityBA;
    ActivityWrapper _activity;
    java.util.ArrayList<B4AMenuItem> menuItems;
	public static final boolean fullScreen = false;
	public static final boolean includeTitle = true;
    public static WeakReference<Activity> previousOne;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (isFirst) {
			processBA = new BA(this.getApplicationContext(), null, null, "com.htetznaing.mmanps", "com.htetznaing.mmanps.mptcdma");
			processBA.loadHtSubs(this.getClass());
	        float deviceScale = getApplicationContext().getResources().getDisplayMetrics().density;
	        BALayout.setDeviceScale(deviceScale);
            
		}
		else if (previousOne != null) {
			Activity p = previousOne.get();
			if (p != null && p != this) {
                BA.LogInfo("Killing previous instance (mptcdma).");
				p.finish();
			}
		}
        processBA.runHook("oncreate", this, null);
		if (!includeTitle) {
        	this.getWindow().requestFeature(android.view.Window.FEATURE_NO_TITLE);
        }
        if (fullScreen) {
        	getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,   
        			android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		mostCurrent = this;
        processBA.sharedProcessBA.activityBA = null;
		layout = new BALayout(this);
		setContentView(layout);
		afterFirstLayout = false;
        WaitForLayout wl = new WaitForLayout();
        if (anywheresoftware.b4a.objects.ServiceHelper.StarterHelper.startFromActivity(processBA, wl, false))
		    BA.handler.postDelayed(wl, 5);

	}
	static class WaitForLayout implements Runnable {
		public void run() {
			if (afterFirstLayout)
				return;
			if (mostCurrent == null)
				return;
            
			if (mostCurrent.layout.getWidth() == 0) {
				BA.handler.postDelayed(this, 5);
				return;
			}
			mostCurrent.layout.getLayoutParams().height = mostCurrent.layout.getHeight();
			mostCurrent.layout.getLayoutParams().width = mostCurrent.layout.getWidth();
			afterFirstLayout = true;
			mostCurrent.afterFirstLayout();
		}
	}
	private void afterFirstLayout() {
        if (this != mostCurrent)
			return;
		activityBA = new BA(this, layout, processBA, "com.htetznaing.mmanps", "com.htetznaing.mmanps.mptcdma");
        
        processBA.sharedProcessBA.activityBA = new java.lang.ref.WeakReference<BA>(activityBA);
        anywheresoftware.b4a.objects.ViewWrapper.lastId = 0;
        _activity = new ActivityWrapper(activityBA, "activity");
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (BA.isShellModeRuntimeCheck(processBA)) {
			if (isFirst)
				processBA.raiseEvent2(null, true, "SHELL", false);
			processBA.raiseEvent2(null, true, "CREATE", true, "com.htetznaing.mmanps.mptcdma", processBA, activityBA, _activity, anywheresoftware.b4a.keywords.Common.Density, mostCurrent);
			_activity.reinitializeForShell(activityBA, "activity");
		}
        initializeProcessGlobals();		
        initializeGlobals();
        
        BA.LogInfo("** Activity (mptcdma) Create, isFirst = " + isFirst + " **");
        processBA.raiseEvent2(null, true, "activity_create", false, isFirst);
		isFirst = false;
		if (this != mostCurrent)
			return;
        processBA.setActivityPaused(false);
        BA.LogInfo("** Activity (mptcdma) Resume **");
        processBA.raiseEvent(null, "activity_resume");
        if (android.os.Build.VERSION.SDK_INT >= 11) {
			try {
				android.app.Activity.class.getMethod("invalidateOptionsMenu").invoke(this,(Object[]) null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	public void addMenuItem(B4AMenuItem item) {
		if (menuItems == null)
			menuItems = new java.util.ArrayList<B4AMenuItem>();
		menuItems.add(item);
	}
	@Override
	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		super.onCreateOptionsMenu(menu);
        try {
            if (processBA.subExists("activity_actionbarhomeclick")) {
                Class.forName("android.app.ActionBar").getMethod("setHomeButtonEnabled", boolean.class).invoke(
                    getClass().getMethod("getActionBar").invoke(this), true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (processBA.runHook("oncreateoptionsmenu", this, new Object[] {menu}))
            return true;
		if (menuItems == null)
			return false;
		for (B4AMenuItem bmi : menuItems) {
			android.view.MenuItem mi = menu.add(bmi.title);
			if (bmi.drawable != null)
				mi.setIcon(bmi.drawable);
            if (android.os.Build.VERSION.SDK_INT >= 11) {
				try {
                    if (bmi.addToBar) {
				        android.view.MenuItem.class.getMethod("setShowAsAction", int.class).invoke(mi, 1);
                    }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mi.setOnMenuItemClickListener(new B4AMenuItemsClickListener(bmi.eventName.toLowerCase(BA.cul)));
		}
        
		return true;
	}   
 @Override
 public boolean onOptionsItemSelected(android.view.MenuItem item) {
    if (item.getItemId() == 16908332) {
        processBA.raiseEvent(null, "activity_actionbarhomeclick");
        return true;
    }
    else
        return super.onOptionsItemSelected(item); 
}
@Override
 public boolean onPrepareOptionsMenu(android.view.Menu menu) {
    super.onPrepareOptionsMenu(menu);
    processBA.runHook("onprepareoptionsmenu", this, new Object[] {menu});
    return true;
    
 }
 protected void onStart() {
    super.onStart();
    processBA.runHook("onstart", this, null);
}
 protected void onStop() {
    super.onStop();
    processBA.runHook("onstop", this, null);
}
    public void onWindowFocusChanged(boolean hasFocus) {
       super.onWindowFocusChanged(hasFocus);
       if (processBA.subExists("activity_windowfocuschanged"))
           processBA.raiseEvent2(null, true, "activity_windowfocuschanged", false, hasFocus);
    }
	private class B4AMenuItemsClickListener implements android.view.MenuItem.OnMenuItemClickListener {
		private final String eventName;
		public B4AMenuItemsClickListener(String eventName) {
			this.eventName = eventName;
		}
		public boolean onMenuItemClick(android.view.MenuItem item) {
			processBA.raiseEvent(item.getTitle(), eventName + "_click");
			return true;
		}
	}
    public static Class<?> getObject() {
		return mptcdma.class;
	}
    private Boolean onKeySubExist = null;
    private Boolean onKeyUpSubExist = null;
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeydown", this, new Object[] {keyCode, event}))
            return true;
		if (onKeySubExist == null)
			onKeySubExist = processBA.subExists("activity_keypress");
		if (onKeySubExist) {
			if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK &&
					android.os.Build.VERSION.SDK_INT >= 18) {
				HandleKeyDelayed hk = new HandleKeyDelayed();
				hk.kc = keyCode;
				BA.handler.post(hk);
				return true;
			}
			else {
				boolean res = new HandleKeyDelayed().runDirectly(keyCode);
				if (res)
					return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	private class HandleKeyDelayed implements Runnable {
		int kc;
		public void run() {
			runDirectly(kc);
		}
		public boolean runDirectly(int keyCode) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keypress", false, keyCode);
			if (res == null || res == true) {
                return true;
            }
            else if (keyCode == anywheresoftware.b4a.keywords.constants.KeyCodes.KEYCODE_BACK) {
				finish();
				return true;
			}
            return false;
		}
		
	}
    @Override
	public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (processBA.runHook("onkeyup", this, new Object[] {keyCode, event}))
            return true;
		if (onKeyUpSubExist == null)
			onKeyUpSubExist = processBA.subExists("activity_keyup");
		if (onKeyUpSubExist) {
			Boolean res =  (Boolean)processBA.raiseEvent2(_activity, false, "activity_keyup", false, keyCode);
			if (res == null || res == true)
				return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void onNewIntent(android.content.Intent intent) {
        super.onNewIntent(intent);
		this.setIntent(intent);
        processBA.runHook("onnewintent", this, new Object[] {intent});
	}
    @Override 
	public void onPause() {
		super.onPause();
        if (_activity == null) //workaround for emulator bug (Issue 2423)
            return;
		anywheresoftware.b4a.Msgbox.dismiss(true);
        BA.LogInfo("** Activity (mptcdma) Pause, UserClosed = " + activityBA.activity.isFinishing() + " **");
        processBA.raiseEvent2(_activity, true, "activity_pause", false, activityBA.activity.isFinishing());		
        processBA.setActivityPaused(true);
        mostCurrent = null;
        if (!activityBA.activity.isFinishing())
			previousOne = new WeakReference<Activity>(this);
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        processBA.runHook("onpause", this, null);
	}

	@Override
	public void onDestroy() {
        super.onDestroy();
		previousOne = null;
        processBA.runHook("ondestroy", this, null);
	}
    @Override 
	public void onResume() {
		super.onResume();
        mostCurrent = this;
        anywheresoftware.b4a.Msgbox.isDismissing = false;
        if (activityBA != null) { //will be null during activity create (which waits for AfterLayout).
        	ResumeMessage rm = new ResumeMessage(mostCurrent);
        	BA.handler.post(rm);
        }
        processBA.runHook("onresume", this, null);
	}
    private static class ResumeMessage implements Runnable {
    	private final WeakReference<Activity> activity;
    	public ResumeMessage(Activity activity) {
    		this.activity = new WeakReference<Activity>(activity);
    	}
		public void run() {
			if (mostCurrent == null || mostCurrent != activity.get())
				return;
			processBA.setActivityPaused(false);
            BA.LogInfo("** Activity (mptcdma) Resume **");
		    processBA.raiseEvent(mostCurrent._activity, "activity_resume", (Object[])null);
		}
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	      android.content.Intent data) {
		processBA.onActivityResult(requestCode, resultCode, data);
        processBA.runHook("onactivityresult", this, new Object[] {requestCode, resultCode});
	}
	private static void initializeGlobals() {
		processBA.raiseEvent2(null, true, "globals", false, (Object[])null);
	}
    public void onRequestPermissionsResult(int requestCode,
        String permissions[], int[] grantResults) {
        Object[] o;
        if (permissions.length > 0)
            o = new Object[] {permissions[0], grantResults[0] == 0};
        else
            o = new Object[] {"", false};
        processBA.raiseEventFromDifferentThread(null,null, 0, "activity_permissionresult", true, o);
            
    }

public anywheresoftware.b4a.keywords.Common __c = null;
public static anywheresoftware.b4a.objects.Timer _ad1 = null;
public static anywheresoftware.b4a.objects.Timer _ad2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb1 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb2 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb3 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb4 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb5 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb6 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb7 = null;
public anywheresoftware.b4a.objects.LabelWrapper _lb8 = null;
public anywheresoftware.b4a.objects.ButtonWrapper _b = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti1 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti2 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti3 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti4 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti5 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti6 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti7 = null;
public anywheresoftware.b4a.objects.NotificationWrapper _noti8 = null;
public anywheresoftware.b4a.objects.ImageViewWrapper _imv = null;
public anywheresoftware.b4a.objects.drawable.ColorDrawable _bbg = null;
public b4a.util.BClipboard _copy = null;
public anywheresoftware.b4a.object.XmlLayoutBuilder _res = null;
public de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper _fb = null;
public de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper _fb1 = null;
public anywheresoftware.b4a.phone.Phone _ph = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper _banner = null;
public anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper _interstitial = null;
public com.htetznaing.mmanps.main _main = null;
public com.htetznaing.mmanps.telenor _telenor = null;
public com.htetznaing.mmanps.ooredoo _ooredoo = null;
public com.htetznaing.mmanps.mectel _mectel = null;
public com.htetznaing.mmanps.mptgsm _mptgsm = null;
public com.htetznaing.mmanps.starter _starter = null;

public static void initializeProcessGlobals() {
             try {
                Class.forName(BA.applicationContext.getPackageName() + ".main").getMethod("initializeProcessGlobals").invoke(null, null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
}
public static String  _activity_create(boolean _firsttime) throws Exception{
 //BA.debugLineNum = 27;BA.debugLine="Sub Activity_Create(FirstTime As Boolean)";
 //BA.debugLineNum = 29;BA.debugLine="ph.SetScreenOrientation(1)";
mostCurrent._ph.SetScreenOrientation(processBA,(int) (1));
 //BA.debugLineNum = 31;BA.debugLine="Activity.LoadLayout(\"Lay1\")";
mostCurrent._activity.LoadLayout("Lay1",mostCurrent.activityBA);
 //BA.debugLineNum = 32;BA.debugLine="fb.Icon = res.GetDrawable(\"ic_add_white_24dp\")";
mostCurrent._fb.setIcon(mostCurrent._res.GetDrawable("ic_add_white_24dp"));
 //BA.debugLineNum = 33;BA.debugLine="fb.HideOffset = 100%y - fb.Top";
mostCurrent._fb.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 34;BA.debugLine="fb.Hide(False)";
mostCurrent._fb.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 35;BA.debugLine="fb.Show(True)";
mostCurrent._fb.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 37;BA.debugLine="fb1.Icon = res.GetDrawable(\"about\")";
mostCurrent._fb1.setIcon(mostCurrent._res.GetDrawable("about"));
 //BA.debugLineNum = 38;BA.debugLine="fb1.HideOffset = 100%y - fb.Top";
mostCurrent._fb1.setHideOffset((int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-mostCurrent._fb.getTop()));
 //BA.debugLineNum = 39;BA.debugLine="fb1.Hide(False)";
mostCurrent._fb1.Hide(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 40;BA.debugLine="fb1.Show(True)";
mostCurrent._fb1.Show(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 42;BA.debugLine="ToastMessageShow(\"You can see APN in Notification";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("You can see APN in Notification!",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 43;BA.debugLine="noti8.Initialize";
mostCurrent._noti8.Initialize();
 //BA.debugLineNum = 44;BA.debugLine="noti8.Sound = False";
mostCurrent._noti8.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 45;BA.debugLine="noti8.Icon = \"icon\"";
mostCurrent._noti8.setIcon("icon");
 //BA.debugLineNum = 46;BA.debugLine="noti8.SetInfo(\"Prompt Password\",\"#777\",Me)";
mostCurrent._noti8.SetInfo(processBA,"Prompt Password","#777",mptcdma.getObject());
 //BA.debugLineNum = 47;BA.debugLine="noti8.Notify(1)";
mostCurrent._noti8.Notify((int) (1));
 //BA.debugLineNum = 49;BA.debugLine="noti7.Initialize";
mostCurrent._noti7.Initialize();
 //BA.debugLineNum = 50;BA.debugLine="noti7.Sound = False";
mostCurrent._noti7.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 51;BA.debugLine="noti7.Icon = \"icon\"";
mostCurrent._noti7.setIcon("icon");
 //BA.debugLineNum = 52;BA.debugLine="noti7.SetInfo(\"APN Protocol\",\"IPV4\",Me)";
mostCurrent._noti7.SetInfo(processBA,"APN Protocol","IPV4",mptcdma.getObject());
 //BA.debugLineNum = 53;BA.debugLine="noti7.Notify(2)";
mostCurrent._noti7.Notify((int) (2));
 //BA.debugLineNum = 55;BA.debugLine="noti6.Initialize";
mostCurrent._noti6.Initialize();
 //BA.debugLineNum = 56;BA.debugLine="noti6.Sound = False";
mostCurrent._noti6.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 57;BA.debugLine="noti6.Icon = \"icon\"";
mostCurrent._noti6.setIcon("icon");
 //BA.debugLineNum = 58;BA.debugLine="noti6.SetInfo(\"Authentication Type\",\"PAP or CHAP\"";
mostCurrent._noti6.SetInfo(processBA,"Authentication Type","PAP or CHAP",mptcdma.getObject());
 //BA.debugLineNum = 59;BA.debugLine="noti6.Notify(3)";
mostCurrent._noti6.Notify((int) (3));
 //BA.debugLineNum = 61;BA.debugLine="noti5.Initialize";
mostCurrent._noti5.Initialize();
 //BA.debugLineNum = 62;BA.debugLine="noti5.Sound = False";
mostCurrent._noti5.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 63;BA.debugLine="noti5.Icon = \"icon\"";
mostCurrent._noti5.setIcon("icon");
 //BA.debugLineNum = 64;BA.debugLine="noti5.SetInfo(\"MNC\",\"03\",Me)";
mostCurrent._noti5.SetInfo(processBA,"MNC","03",mptcdma.getObject());
 //BA.debugLineNum = 65;BA.debugLine="noti5.Notify(4)";
mostCurrent._noti5.Notify((int) (4));
 //BA.debugLineNum = 67;BA.debugLine="noti4.Initialize";
mostCurrent._noti4.Initialize();
 //BA.debugLineNum = 68;BA.debugLine="noti4.Sound = False";
mostCurrent._noti4.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 69;BA.debugLine="noti4.Icon = \"icon\"";
mostCurrent._noti4.setIcon("icon");
 //BA.debugLineNum = 70;BA.debugLine="noti4.SetInfo(\"MCC \",\"460\",Me)";
mostCurrent._noti4.SetInfo(processBA,"MCC ","460",mptcdma.getObject());
 //BA.debugLineNum = 71;BA.debugLine="noti4.Notify(5)";
mostCurrent._noti4.Notify((int) (5));
 //BA.debugLineNum = 73;BA.debugLine="noti3.Initialize";
mostCurrent._noti3.Initialize();
 //BA.debugLineNum = 74;BA.debugLine="noti3.Sound = False";
mostCurrent._noti3.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 75;BA.debugLine="noti3.Icon = \"icon\"";
mostCurrent._noti3.setIcon("icon");
 //BA.debugLineNum = 76;BA.debugLine="noti3.SetInfo(\"Password\",\"yourpassword\",Me)";
mostCurrent._noti3.SetInfo(processBA,"Password","yourpassword",mptcdma.getObject());
 //BA.debugLineNum = 77;BA.debugLine="noti3.Notify(6)";
mostCurrent._noti3.Notify((int) (6));
 //BA.debugLineNum = 79;BA.debugLine="noti2.Initialize";
mostCurrent._noti2.Initialize();
 //BA.debugLineNum = 80;BA.debugLine="noti2.Sound = False";
mostCurrent._noti2.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 81;BA.debugLine="noti2.Icon = \"icon\"";
mostCurrent._noti2.setIcon("icon");
 //BA.debugLineNum = 82;BA.debugLine="noti2.SetInfo(\"Username\",\"yourname@c800.mm\",Me)";
mostCurrent._noti2.SetInfo(processBA,"Username","yourname@c800.mm",mptcdma.getObject());
 //BA.debugLineNum = 83;BA.debugLine="noti2.Notify(7)";
mostCurrent._noti2.Notify((int) (7));
 //BA.debugLineNum = 85;BA.debugLine="noti1.Initialize";
mostCurrent._noti1.Initialize();
 //BA.debugLineNum = 86;BA.debugLine="noti1.Sound = False";
mostCurrent._noti1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 87;BA.debugLine="noti1.Icon = \"icon\"";
mostCurrent._noti1.setIcon("icon");
 //BA.debugLineNum = 88;BA.debugLine="noti1.SetInfo(\"APN\",\"#777\",Me)";
mostCurrent._noti1.SetInfo(processBA,"APN","#777",mptcdma.getObject());
 //BA.debugLineNum = 89;BA.debugLine="noti1.Notify(8)";
mostCurrent._noti1.Notify((int) (8));
 //BA.debugLineNum = 91;BA.debugLine="noti.Initialize";
mostCurrent._noti.Initialize();
 //BA.debugLineNum = 92;BA.debugLine="noti.Sound = False";
mostCurrent._noti.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 93;BA.debugLine="noti.Icon = \"icon\"";
mostCurrent._noti.setIcon("icon");
 //BA.debugLineNum = 94;BA.debugLine="noti.SetInfo(\"Name\",\"MPT\",Me)";
mostCurrent._noti.SetInfo(processBA,"Name","MPT",mptcdma.getObject());
 //BA.debugLineNum = 95;BA.debugLine="noti.Notify(9)";
mostCurrent._noti.Notify((int) (9));
 //BA.debugLineNum = 97;BA.debugLine="Activity.Color = Colors.White";
mostCurrent._activity.setColor(anywheresoftware.b4a.keywords.Common.Colors.White);
 //BA.debugLineNum = 99;BA.debugLine="imv.Initialize(\"\")";
mostCurrent._imv.Initialize(mostCurrent.activityBA,"");
 //BA.debugLineNum = 100;BA.debugLine="imv.Bitmap = LoadBitmap(File.DirAssets,\"mpt.png\")";
mostCurrent._imv.setBitmap((android.graphics.Bitmap)(anywheresoftware.b4a.keywords.Common.LoadBitmap(anywheresoftware.b4a.keywords.Common.File.getDirAssets(),"mpt.png").getObject()));
 //BA.debugLineNum = 101;BA.debugLine="imv.Gravity = Gravity.FILL";
mostCurrent._imv.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.FILL);
 //BA.debugLineNum = 102;BA.debugLine="Activity.AddView(imv,50%x - 50dip,10dip,100dip,10";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._imv.getObject()),(int) (anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (50),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50))),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (10)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (100)));
 //BA.debugLineNum = 104;BA.debugLine="lb.Initialize(\"lb\")";
mostCurrent._lb.Initialize(mostCurrent.activityBA,"lb");
 //BA.debugLineNum = 105;BA.debugLine="lb.Text = \"Name : MPT\"";
mostCurrent._lb.setText((Object)("Name : MPT"));
 //BA.debugLineNum = 106;BA.debugLine="lb.TextColor = Colors.Black";
mostCurrent._lb.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 107;BA.debugLine="Activity.AddView(lb,0%x,(imv.Top+imv.Height)+2%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._imv.getTop()+mostCurrent._imv.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 108;BA.debugLine="lb.Gravity = Gravity.CENTER";
mostCurrent._lb.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 110;BA.debugLine="lb1.Initialize(\"lb1\")";
mostCurrent._lb1.Initialize(mostCurrent.activityBA,"lb1");
 //BA.debugLineNum = 111;BA.debugLine="lb1.Text = \"APN : #777\"";
mostCurrent._lb1.setText((Object)("APN : #777"));
 //BA.debugLineNum = 112;BA.debugLine="lb1.TextColor = Colors.Black";
mostCurrent._lb1.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 113;BA.debugLine="Activity.AddView(lb1,0%x,(lb.Top+lb.Height),100%x";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb1.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb.getTop()+mostCurrent._lb.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 114;BA.debugLine="lb1.Gravity = Gravity.CENTER";
mostCurrent._lb1.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 116;BA.debugLine="lb2.Initialize(\"lb2\")";
mostCurrent._lb2.Initialize(mostCurrent.activityBA,"lb2");
 //BA.debugLineNum = 117;BA.debugLine="lb2.Text = \"Username : yourname@c800.mm\"";
mostCurrent._lb2.setText((Object)("Username : yourname@c800.mm"));
 //BA.debugLineNum = 118;BA.debugLine="lb2.TextColor = Colors.Black";
mostCurrent._lb2.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 119;BA.debugLine="Activity.AddView(lb2,0%x,(lb1.Top+lb1.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb2.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb1.getTop()+mostCurrent._lb1.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 120;BA.debugLine="lb2.Gravity = Gravity.CENTER";
mostCurrent._lb2.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 122;BA.debugLine="lb3.Initialize(\"lb3\")";
mostCurrent._lb3.Initialize(mostCurrent.activityBA,"lb3");
 //BA.debugLineNum = 123;BA.debugLine="lb3.Text = \"Password : yourpassword\"";
mostCurrent._lb3.setText((Object)("Password : yourpassword"));
 //BA.debugLineNum = 124;BA.debugLine="lb3.TextColor = Colors.Black";
mostCurrent._lb3.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 125;BA.debugLine="Activity.AddView(lb3,0%x,(lb2.Top+lb2.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb3.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb2.getTop()+mostCurrent._lb2.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 126;BA.debugLine="lb3.Gravity = Gravity.CENTER";
mostCurrent._lb3.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 128;BA.debugLine="lb4.Initialize(\"lb4\")";
mostCurrent._lb4.Initialize(mostCurrent.activityBA,"lb4");
 //BA.debugLineNum = 129;BA.debugLine="lb4.Text = \"MCC : 460\"";
mostCurrent._lb4.setText((Object)("MCC : 460"));
 //BA.debugLineNum = 130;BA.debugLine="lb4.TextColor = Colors.Black";
mostCurrent._lb4.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 131;BA.debugLine="Activity.AddView(lb4,0%x,(lb3.Top+lb3.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb4.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb3.getTop()+mostCurrent._lb3.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 132;BA.debugLine="lb4.Gravity = Gravity.CENTER";
mostCurrent._lb4.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 134;BA.debugLine="lb5.Initialize(\"lb5\")";
mostCurrent._lb5.Initialize(mostCurrent.activityBA,"lb5");
 //BA.debugLineNum = 135;BA.debugLine="lb5.Text = \"MNC : 03\"";
mostCurrent._lb5.setText((Object)("MNC : 03"));
 //BA.debugLineNum = 136;BA.debugLine="lb5.TextColor = Colors.Black";
mostCurrent._lb5.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 137;BA.debugLine="Activity.AddView(lb5,0%x,(lb4.Top+lb4.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb5.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb4.getTop()+mostCurrent._lb4.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 138;BA.debugLine="lb5.Gravity = Gravity.CENTER";
mostCurrent._lb5.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 140;BA.debugLine="lb6.Initialize(\"lb6\")";
mostCurrent._lb6.Initialize(mostCurrent.activityBA,"lb6");
 //BA.debugLineNum = 141;BA.debugLine="lb6.Text = \"Authentication Type : PAP or CHAP\"";
mostCurrent._lb6.setText((Object)("Authentication Type : PAP or CHAP"));
 //BA.debugLineNum = 142;BA.debugLine="lb6.TextColor = Colors.Black";
mostCurrent._lb6.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 143;BA.debugLine="Activity.AddView(lb6,0%x,(lb5.Top+lb5.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb6.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb5.getTop()+mostCurrent._lb5.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 144;BA.debugLine="lb6.Gravity = Gravity.CENTER";
mostCurrent._lb6.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 146;BA.debugLine="lb7.Initialize(\"lb7\")";
mostCurrent._lb7.Initialize(mostCurrent.activityBA,"lb7");
 //BA.debugLineNum = 147;BA.debugLine="lb7.Text = \"APN Protocol : IPV4\"";
mostCurrent._lb7.setText((Object)("APN Protocol : IPV4"));
 //BA.debugLineNum = 148;BA.debugLine="lb7.TextColor = Colors.Black";
mostCurrent._lb7.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 149;BA.debugLine="Activity.AddView(lb7,0%x,(lb6.Top+lb6.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb7.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb6.getTop()+mostCurrent._lb6.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 150;BA.debugLine="lb7.Gravity = Gravity.CENTER";
mostCurrent._lb7.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 152;BA.debugLine="lb8.Initialize(\"lb8\")";
mostCurrent._lb8.Initialize(mostCurrent.activityBA,"lb8");
 //BA.debugLineNum = 153;BA.debugLine="lb8.Text = \"Prompt Password : #777\"";
mostCurrent._lb8.setText((Object)("Prompt Password : #777"));
 //BA.debugLineNum = 154;BA.debugLine="lb8.TextColor = Colors.Black";
mostCurrent._lb8.setTextColor(anywheresoftware.b4a.keywords.Common.Colors.Black);
 //BA.debugLineNum = 155;BA.debugLine="Activity.AddView(lb8,0%x,(lb7.Top+lb7.Height),100";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._lb8.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) ((mostCurrent._lb7.getTop()+mostCurrent._lb7.getHeight())),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (7),mostCurrent.activityBA));
 //BA.debugLineNum = 156;BA.debugLine="lb8.Gravity = Gravity.CENTER";
mostCurrent._lb8.setGravity(anywheresoftware.b4a.keywords.Common.Gravity.CENTER);
 //BA.debugLineNum = 158;BA.debugLine="bbg.Initialize(Colors.Black , 15)";
mostCurrent._bbg.Initialize(anywheresoftware.b4a.keywords.Common.Colors.Black,(int) (15));
 //BA.debugLineNum = 159;BA.debugLine="b.Initialize(\"b\")";
mostCurrent._b.Initialize(mostCurrent.activityBA,"b");
 //BA.debugLineNum = 160;BA.debugLine="b.Text = \"Start\"";
mostCurrent._b.setText((Object)("Start"));
 //BA.debugLineNum = 161;BA.debugLine="b.Background = bbg";
mostCurrent._b.setBackground((android.graphics.drawable.Drawable)(mostCurrent._bbg.getObject()));
 //BA.debugLineNum = 162;BA.debugLine="Activity.AddView(b,20%x,(lb8.Top+lb8.Height)+2%y,";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._b.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (20),mostCurrent.activityBA),(int) ((mostCurrent._lb8.getTop()+mostCurrent._lb8.getHeight())+anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (2),mostCurrent.activityBA)),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (60),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 164;BA.debugLine="Banner.Initialize(\"Banner\",\" ca-app-pub-417334857";
mostCurrent._banner.Initialize(mostCurrent.activityBA,"Banner"," ca-app-pub-4173348573252986/4847019357");
 //BA.debugLineNum = 165;BA.debugLine="Banner.LoadAd";
mostCurrent._banner.LoadAd();
 //BA.debugLineNum = 166;BA.debugLine="Activity.AddView(Banner,0%x,100%y - 80dip,100%x,5";
mostCurrent._activity.AddView((android.view.View)(mostCurrent._banner.getObject()),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (0),mostCurrent.activityBA),(int) (anywheresoftware.b4a.keywords.Common.PerYToCurrent((float) (100),mostCurrent.activityBA)-anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (80))),anywheresoftware.b4a.keywords.Common.PerXToCurrent((float) (100),mostCurrent.activityBA),anywheresoftware.b4a.keywords.Common.DipToCurrent((int) (50)));
 //BA.debugLineNum = 168;BA.debugLine="Interstitial.Initialize(\"Interstitial\",\"ca-app-pu";
mostCurrent._interstitial.Initialize(mostCurrent.activityBA,"Interstitial","ca-app-pub-4173348573252986/6323752558");
 //BA.debugLineNum = 169;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 171;BA.debugLine="ad1.Initialize(\"ad1\",100)";
_ad1.Initialize(processBA,"ad1",(long) (100));
 //BA.debugLineNum = 172;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 173;BA.debugLine="ad2.Initialize(\"ad2\",15000)";
_ad2.Initialize(processBA,"ad2",(long) (15000));
 //BA.debugLineNum = 174;BA.debugLine="ad2.Enabled = True";
_ad2.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 175;BA.debugLine="End Sub";
return "";
}
public static String  _activity_pause(boolean _userclosed) throws Exception{
 //BA.debugLineNum = 336;BA.debugLine="Sub Activity_Pause (UserClosed As Boolean)";
 //BA.debugLineNum = 338;BA.debugLine="End Sub";
return "";
}
public static String  _activity_resume() throws Exception{
 //BA.debugLineNum = 332;BA.debugLine="Sub Activity_Resume";
 //BA.debugLineNum = 334;BA.debugLine="End Sub";
return "";
}
public static String  _ad1_tick() throws Exception{
 //BA.debugLineNum = 340;BA.debugLine="Sub ad1_Tick";
 //BA.debugLineNum = 341;BA.debugLine="If Interstitial.Ready Then Interstitial.Show Else";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();}
else {
mostCurrent._interstitial.LoadAd();};
 //BA.debugLineNum = 342;BA.debugLine="ad1.Enabled = False";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 343;BA.debugLine="End Sub";
return "";
}
public static String  _ad2_tick() throws Exception{
 //BA.debugLineNum = 345;BA.debugLine="Sub ad2_Tick";
 //BA.debugLineNum = 346;BA.debugLine="If Interstitial.Ready Then Interstitial.Show Else";
if (mostCurrent._interstitial.getReady()) { 
mostCurrent._interstitial.Show();}
else {
mostCurrent._interstitial.LoadAd();};
 //BA.debugLineNum = 347;BA.debugLine="End Sub";
return "";
}
public static String  _b_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _intent = null;
 //BA.debugLineNum = 240;BA.debugLine="Sub b_Click";
 //BA.debugLineNum = 241;BA.debugLine="If ph.SdkVersion > 19 Then";
if (mostCurrent._ph.getSdkVersion()>19) { 
 //BA.debugLineNum = 242;BA.debugLine="ad1.Enabled = True";
_ad1.setEnabled(anywheresoftware.b4a.keywords.Common.True);
 };
 //BA.debugLineNum = 244;BA.debugLine="ToastMessageShow(\"Click Add for set New APN Setti";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("Click Add for set New APN Setting",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 245;BA.debugLine="Dim intent As Intent";
_intent = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 246;BA.debugLine="intent.Initialize(\"android.settings.APN_SETTINGS\"";
_intent.Initialize("android.settings.APN_SETTINGS","");
 //BA.debugLineNum = 247;BA.debugLine="StartActivity(intent)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_intent.getObject()));
 //BA.debugLineNum = 249;BA.debugLine="noti8.Initialize";
mostCurrent._noti8.Initialize();
 //BA.debugLineNum = 250;BA.debugLine="noti8.Sound = False";
mostCurrent._noti8.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 251;BA.debugLine="noti8.Icon = \"icon\"";
mostCurrent._noti8.setIcon("icon");
 //BA.debugLineNum = 252;BA.debugLine="noti8.SetInfo(\"Prompt Password\",\"#777\",Me)";
mostCurrent._noti8.SetInfo(processBA,"Prompt Password","#777",mptcdma.getObject());
 //BA.debugLineNum = 253;BA.debugLine="noti8.Notify(1)";
mostCurrent._noti8.Notify((int) (1));
 //BA.debugLineNum = 255;BA.debugLine="noti7.Initialize";
mostCurrent._noti7.Initialize();
 //BA.debugLineNum = 256;BA.debugLine="noti7.Sound = False";
mostCurrent._noti7.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 257;BA.debugLine="noti7.Icon = \"icon\"";
mostCurrent._noti7.setIcon("icon");
 //BA.debugLineNum = 258;BA.debugLine="noti7.SetInfo(\"APN Protocol\",\"IPV4\",Me)";
mostCurrent._noti7.SetInfo(processBA,"APN Protocol","IPV4",mptcdma.getObject());
 //BA.debugLineNum = 259;BA.debugLine="noti7.Notify(2)";
mostCurrent._noti7.Notify((int) (2));
 //BA.debugLineNum = 261;BA.debugLine="noti6.Initialize";
mostCurrent._noti6.Initialize();
 //BA.debugLineNum = 262;BA.debugLine="noti6.Sound = False";
mostCurrent._noti6.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 263;BA.debugLine="noti6.Icon = \"icon\"";
mostCurrent._noti6.setIcon("icon");
 //BA.debugLineNum = 264;BA.debugLine="noti6.SetInfo(\"Authentication Type\",\"PAP or CHAP\"";
mostCurrent._noti6.SetInfo(processBA,"Authentication Type","PAP or CHAP",mptcdma.getObject());
 //BA.debugLineNum = 265;BA.debugLine="noti6.Notify(3)";
mostCurrent._noti6.Notify((int) (3));
 //BA.debugLineNum = 267;BA.debugLine="noti5.Initialize";
mostCurrent._noti5.Initialize();
 //BA.debugLineNum = 268;BA.debugLine="noti5.Sound = False";
mostCurrent._noti5.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 269;BA.debugLine="noti5.Icon = \"icon\"";
mostCurrent._noti5.setIcon("icon");
 //BA.debugLineNum = 270;BA.debugLine="noti5.SetInfo(\"MNC\",\"03\",Me)";
mostCurrent._noti5.SetInfo(processBA,"MNC","03",mptcdma.getObject());
 //BA.debugLineNum = 271;BA.debugLine="noti5.Notify(4)";
mostCurrent._noti5.Notify((int) (4));
 //BA.debugLineNum = 273;BA.debugLine="noti4.Initialize";
mostCurrent._noti4.Initialize();
 //BA.debugLineNum = 274;BA.debugLine="noti4.Sound = False";
mostCurrent._noti4.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 275;BA.debugLine="noti4.Icon = \"icon\"";
mostCurrent._noti4.setIcon("icon");
 //BA.debugLineNum = 276;BA.debugLine="noti4.SetInfo(\"MCC \",\"460\",Me)";
mostCurrent._noti4.SetInfo(processBA,"MCC ","460",mptcdma.getObject());
 //BA.debugLineNum = 277;BA.debugLine="noti4.Notify(5)";
mostCurrent._noti4.Notify((int) (5));
 //BA.debugLineNum = 279;BA.debugLine="noti3.Initialize";
mostCurrent._noti3.Initialize();
 //BA.debugLineNum = 280;BA.debugLine="noti3.Sound = False";
mostCurrent._noti3.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 281;BA.debugLine="noti3.Icon = \"icon\"";
mostCurrent._noti3.setIcon("icon");
 //BA.debugLineNum = 282;BA.debugLine="noti3.SetInfo(\"Password\",\"yourpassword\",Me)";
mostCurrent._noti3.SetInfo(processBA,"Password","yourpassword",mptcdma.getObject());
 //BA.debugLineNum = 283;BA.debugLine="noti3.Notify(6)";
mostCurrent._noti3.Notify((int) (6));
 //BA.debugLineNum = 285;BA.debugLine="noti2.Initialize";
mostCurrent._noti2.Initialize();
 //BA.debugLineNum = 286;BA.debugLine="noti2.Sound = False";
mostCurrent._noti2.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 287;BA.debugLine="noti2.Icon = \"icon\"";
mostCurrent._noti2.setIcon("icon");
 //BA.debugLineNum = 288;BA.debugLine="noti2.SetInfo(\"Username\",\"yourname@c800.mm\",Me)";
mostCurrent._noti2.SetInfo(processBA,"Username","yourname@c800.mm",mptcdma.getObject());
 //BA.debugLineNum = 289;BA.debugLine="noti2.Notify(7)";
mostCurrent._noti2.Notify((int) (7));
 //BA.debugLineNum = 291;BA.debugLine="noti1.Initialize";
mostCurrent._noti1.Initialize();
 //BA.debugLineNum = 292;BA.debugLine="noti1.Sound = False";
mostCurrent._noti1.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 293;BA.debugLine="noti1.Icon = \"icon\"";
mostCurrent._noti1.setIcon("icon");
 //BA.debugLineNum = 294;BA.debugLine="noti1.SetInfo(\"APN\",\"#777\",Me)";
mostCurrent._noti1.SetInfo(processBA,"APN","#777",mptcdma.getObject());
 //BA.debugLineNum = 295;BA.debugLine="noti1.Notify(8)";
mostCurrent._noti1.Notify((int) (8));
 //BA.debugLineNum = 297;BA.debugLine="noti.Initialize";
mostCurrent._noti.Initialize();
 //BA.debugLineNum = 298;BA.debugLine="noti.Sound = False";
mostCurrent._noti.setSound(anywheresoftware.b4a.keywords.Common.False);
 //BA.debugLineNum = 299;BA.debugLine="noti.Icon = \"icon\"";
mostCurrent._noti.setIcon("icon");
 //BA.debugLineNum = 300;BA.debugLine="noti.SetInfo(\"Name\",\"MPT\",Me)";
mostCurrent._noti.SetInfo(processBA,"Name","MPT",mptcdma.getObject());
 //BA.debugLineNum = 301;BA.debugLine="noti.Notify(9)";
mostCurrent._noti.Notify((int) (9));
 //BA.debugLineNum = 302;BA.debugLine="End Sub";
return "";
}
public static String  _fb_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _facebook = null;
anywheresoftware.b4a.objects.IntentWrapper _i = null;
 //BA.debugLineNum = 305;BA.debugLine="Sub fb_Click";
 //BA.debugLineNum = 306;BA.debugLine="ToastMessageShow(\"App Update နွင့္ App အသစ္ေတြ \"";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("App Update နွင့္ App အသစ္ေတြ "+anywheresoftware.b4a.keywords.Common.CRLF+"Facebook ကေနဖတ္ရူ့သိနိုင္ဖို့"+anywheresoftware.b4a.keywords.Common.CRLF+"Myanmar Android Apps ကို Like ထားလိုက္ပါ။",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 307;BA.debugLine="Try";
try { //BA.debugLineNum = 308;BA.debugLine="Dim Facebook As Intent";
_facebook = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 309;BA.debugLine="Facebook.Initialize(Facebook.ACTION_VIEW, \"fb://";
_facebook.Initialize(_facebook.ACTION_VIEW,"fb://page/627699334104477");
 //BA.debugLineNum = 310;BA.debugLine="StartActivity(Facebook)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_facebook.getObject()));
 } 
       catch (Exception e7) {
			processBA.setLastException(e7); //BA.debugLineNum = 314;BA.debugLine="Dim i As Intent";
_i = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 315;BA.debugLine="i.Initialize(i.ACTION_VIEW, \"https://m.facebook.";
_i.Initialize(_i.ACTION_VIEW,"https://m.facebook.com/MmFreeAndroidApps");
 //BA.debugLineNum = 316;BA.debugLine="StartActivity(i)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_i.getObject()));
 };
 //BA.debugLineNum = 318;BA.debugLine="End Sub";
return "";
}
public static String  _fb1_click() throws Exception{
anywheresoftware.b4a.objects.IntentWrapper _shareit = null;
 //BA.debugLineNum = 320;BA.debugLine="Sub fb1_Click";
 //BA.debugLineNum = 321;BA.debugLine="Dim ShareIt As Intent";
_shareit = new anywheresoftware.b4a.objects.IntentWrapper();
 //BA.debugLineNum = 322;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 323;BA.debugLine="copy.setText(\"All in One Myanmar APN Setting Apps";
mostCurrent._copy.setText(mostCurrent.activityBA,"All in One Myanmar APN Setting Apps"+anywheresoftware.b4a.keywords.Common.CRLF+"Download Free at : www.htetznaing.com/search?q=Myanmar+APNs"+anywheresoftware.b4a.keywords.Common.CRLF+" #Ht3tzN4ing"+anywheresoftware.b4a.keywords.Common.CRLF+" #MyanmarAndroidApps");
 //BA.debugLineNum = 324;BA.debugLine="ShareIt.Initialize (ShareIt.ACTION_SEND,\"\")";
_shareit.Initialize(_shareit.ACTION_SEND,"");
 //BA.debugLineNum = 325;BA.debugLine="ShareIt.SetType (\"text/plain\")";
_shareit.SetType("text/plain");
 //BA.debugLineNum = 326;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.TEXT\",cop";
_shareit.PutExtra("android.intent.extra.TEXT",(Object)(mostCurrent._copy.getText(mostCurrent.activityBA)));
 //BA.debugLineNum = 327;BA.debugLine="ShareIt.PutExtra (\"android.intent.extra.SUBJECT\",";
_shareit.PutExtra("android.intent.extra.SUBJECT",(Object)("Get Free!!"));
 //BA.debugLineNum = 328;BA.debugLine="ShareIt.WrapAsIntentChooser(\"Share App Via...\")";
_shareit.WrapAsIntentChooser("Share App Via...");
 //BA.debugLineNum = 329;BA.debugLine="StartActivity (ShareIt)";
anywheresoftware.b4a.keywords.Common.StartActivity(mostCurrent.activityBA,(Object)(_shareit.getObject()));
 //BA.debugLineNum = 330;BA.debugLine="End Sub";
return "";
}
public static String  _globals() throws Exception{
 //BA.debugLineNum = 12;BA.debugLine="Sub Globals";
 //BA.debugLineNum = 13;BA.debugLine="Dim lb,lb1,lb2, lb3,lb4,lb5,lb6,lb7,lb8 As Label";
mostCurrent._lb = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb1 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb2 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb3 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb4 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb5 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb6 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb7 = new anywheresoftware.b4a.objects.LabelWrapper();
mostCurrent._lb8 = new anywheresoftware.b4a.objects.LabelWrapper();
 //BA.debugLineNum = 14;BA.debugLine="Dim b As Button";
mostCurrent._b = new anywheresoftware.b4a.objects.ButtonWrapper();
 //BA.debugLineNum = 15;BA.debugLine="Dim noti,noti1,noti2,noti3,noti4,noti5,noti6,noti";
mostCurrent._noti = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti1 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti2 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti3 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti4 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti5 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti6 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti7 = new anywheresoftware.b4a.objects.NotificationWrapper();
mostCurrent._noti8 = new anywheresoftware.b4a.objects.NotificationWrapper();
 //BA.debugLineNum = 16;BA.debugLine="Dim imv As ImageView";
mostCurrent._imv = new anywheresoftware.b4a.objects.ImageViewWrapper();
 //BA.debugLineNum = 17;BA.debugLine="Dim bbg As ColorDrawable";
mostCurrent._bbg = new anywheresoftware.b4a.objects.drawable.ColorDrawable();
 //BA.debugLineNum = 18;BA.debugLine="Dim copy As BClipboard";
mostCurrent._copy = new b4a.util.BClipboard();
 //BA.debugLineNum = 19;BA.debugLine="Dim res As XmlLayoutBuilder";
mostCurrent._res = new anywheresoftware.b4a.object.XmlLayoutBuilder();
 //BA.debugLineNum = 20;BA.debugLine="Dim fb,fb1 As FloatingActionButton";
mostCurrent._fb = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
mostCurrent._fb1 = new de.amberhome.objects.floatingactionbutton.FloatingActionButtonWrapper();
 //BA.debugLineNum = 22;BA.debugLine="Dim ph As Phone";
mostCurrent._ph = new anywheresoftware.b4a.phone.Phone();
 //BA.debugLineNum = 23;BA.debugLine="Dim Banner As AdView";
mostCurrent._banner = new anywheresoftware.b4a.admobwrapper.AdViewWrapper();
 //BA.debugLineNum = 24;BA.debugLine="Dim Interstitial As InterstitialAd";
mostCurrent._interstitial = new anywheresoftware.b4a.admobwrapper.AdViewWrapper.InterstitialAdWrapper();
 //BA.debugLineNum = 25;BA.debugLine="End Sub";
return "";
}
public static String  _interstitial_adclosed() throws Exception{
 //BA.debugLineNum = 349;BA.debugLine="Sub Interstitial_AdClosed";
 //BA.debugLineNum = 350;BA.debugLine="Interstitial.LoadAd";
mostCurrent._interstitial.LoadAd();
 //BA.debugLineNum = 351;BA.debugLine="End Sub";
return "";
}
public static String  _lb_click() throws Exception{
 //BA.debugLineNum = 177;BA.debugLine="Sub lb_Click";
 //BA.debugLineNum = 178;BA.debugLine="lb.SetTextColorAnimated(1000,Colors.Green)";
mostCurrent._lb.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Green);
 //BA.debugLineNum = 179;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 180;BA.debugLine="copy.setText(\"MPT\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"MPT");
 //BA.debugLineNum = 181;BA.debugLine="ToastMessageShow(\"MPT Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("MPT Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 182;BA.debugLine="End Sub";
return "";
}
public static String  _lb1_click() throws Exception{
 //BA.debugLineNum = 184;BA.debugLine="Sub lb1_Click";
 //BA.debugLineNum = 185;BA.debugLine="lb1.SetTextColorAnimated(1000,Colors.Blue)";
mostCurrent._lb1.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Blue);
 //BA.debugLineNum = 186;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 187;BA.debugLine="copy.setText(\"#777\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"#777");
 //BA.debugLineNum = 188;BA.debugLine="ToastMessageShow(\"#777 Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("#777 Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 189;BA.debugLine="End Sub";
return "";
}
public static String  _lb2_click() throws Exception{
 //BA.debugLineNum = 191;BA.debugLine="Sub lb2_Click";
 //BA.debugLineNum = 192;BA.debugLine="lb2.SetTextColorAnimated(1000,Colors.Red)";
mostCurrent._lb2.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Red);
 //BA.debugLineNum = 193;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 194;BA.debugLine="copy.setText(\"yourname@c800.mm\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"yourname@c800.mm");
 //BA.debugLineNum = 195;BA.debugLine="ToastMessageShow(\"yourname@c800.mm Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("yourname@c800.mm Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 196;BA.debugLine="End Sub";
return "";
}
public static String  _lb3_click() throws Exception{
 //BA.debugLineNum = 198;BA.debugLine="Sub lb3_Click";
 //BA.debugLineNum = 199;BA.debugLine="lb3.SetTextColorAnimated(1000,Colors.Cyan)";
mostCurrent._lb3.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Cyan);
 //BA.debugLineNum = 200;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 201;BA.debugLine="copy.setText(\"yourpassword\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"yourpassword");
 //BA.debugLineNum = 202;BA.debugLine="ToastMessageShow(\"yourpassword Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("yourpassword Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 203;BA.debugLine="End Sub";
return "";
}
public static String  _lb4_click() throws Exception{
 //BA.debugLineNum = 205;BA.debugLine="Sub lb4_Click";
 //BA.debugLineNum = 206;BA.debugLine="lb4.SetTextColorAnimated(1000,Colors.Magenta)";
mostCurrent._lb4.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Magenta);
 //BA.debugLineNum = 207;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 208;BA.debugLine="copy.setText(\"460\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"460");
 //BA.debugLineNum = 209;BA.debugLine="ToastMessageShow(\"460 Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("460 Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 210;BA.debugLine="End Sub";
return "";
}
public static String  _lb5_click() throws Exception{
 //BA.debugLineNum = 212;BA.debugLine="Sub lb5_Click";
 //BA.debugLineNum = 213;BA.debugLine="lb5.SetTextColorAnimated(1000,Colors.DarkGray)";
mostCurrent._lb5.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.DarkGray);
 //BA.debugLineNum = 214;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 215;BA.debugLine="copy.setText(\"03\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"03");
 //BA.debugLineNum = 216;BA.debugLine="ToastMessageShow(\"03 Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("03 Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 217;BA.debugLine="End Sub";
return "";
}
public static String  _lb6_click() throws Exception{
 //BA.debugLineNum = 219;BA.debugLine="Sub lb6_Click";
 //BA.debugLineNum = 220;BA.debugLine="lb6.SetTextColorAnimated(1000,Colors.Gray)";
mostCurrent._lb6.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.Gray);
 //BA.debugLineNum = 221;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 222;BA.debugLine="copy.setText(\"PAP or CHAP\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"PAP or CHAP");
 //BA.debugLineNum = 223;BA.debugLine="ToastMessageShow(\"PAP or CHAP Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("PAP or CHAP Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 224;BA.debugLine="End Sub";
return "";
}
public static String  _lb7_click() throws Exception{
 //BA.debugLineNum = 226;BA.debugLine="Sub lb7_Click";
 //BA.debugLineNum = 227;BA.debugLine="lb7.SetTextColorAnimated(1000,Colors.LightGray)";
mostCurrent._lb7.SetTextColorAnimated((int) (1000),anywheresoftware.b4a.keywords.Common.Colors.LightGray);
 //BA.debugLineNum = 228;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 229;BA.debugLine="copy.setText(\"IPV4\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"IPV4");
 //BA.debugLineNum = 230;BA.debugLine="ToastMessageShow(\"IPV4 Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("IPV4 Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 231;BA.debugLine="End Sub";
return "";
}
public static String  _lb8_click() throws Exception{
 //BA.debugLineNum = 233;BA.debugLine="Sub lb8_Click";
 //BA.debugLineNum = 234;BA.debugLine="lb8.SetTextColorAnimated(1000,0xFF940094)";
mostCurrent._lb8.SetTextColorAnimated((int) (1000),(int) (0xff940094));
 //BA.debugLineNum = 235;BA.debugLine="copy.clrText";
mostCurrent._copy.clrText(mostCurrent.activityBA);
 //BA.debugLineNum = 236;BA.debugLine="copy.setText(\"#777\")";
mostCurrent._copy.setText(mostCurrent.activityBA,"#777");
 //BA.debugLineNum = 237;BA.debugLine="ToastMessageShow(\"#777 Copied\",True)";
anywheresoftware.b4a.keywords.Common.ToastMessageShow("#777 Copied",anywheresoftware.b4a.keywords.Common.True);
 //BA.debugLineNum = 238;BA.debugLine="End Sub";
return "";
}
public static String  _process_globals() throws Exception{
 //BA.debugLineNum = 6;BA.debugLine="Sub Process_Globals";
 //BA.debugLineNum = 9;BA.debugLine="Dim ad1,ad2 As Timer";
_ad1 = new anywheresoftware.b4a.objects.Timer();
_ad2 = new anywheresoftware.b4a.objects.Timer();
 //BA.debugLineNum = 10;BA.debugLine="End Sub";
return "";
}
}
