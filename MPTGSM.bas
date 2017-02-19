﻿Type=Activity
Version=6.5
ModulesStructureVersion=1
B4A=true
@EndOfDesignText@
#Region  Activity Attributes 
	#FullScreen: False
	#IncludeTitle: True
#End Region

Sub Process_Globals
	'These global variables will be declared once when the application starts.
	'These variables can be accessed from all modules.
	Dim ad1,ad2 As Timer
End Sub

Sub Globals
	Dim lb,lb1,lb2, lb3,lb4 As Label
	Dim b As Button
	Dim noti,noti1,noti2,noti3,noti4 As Notification
	Dim imv As ImageView
	Dim bbg As ColorDrawable
	Dim copy As BClipboard
	Dim res As XmlLayoutBuilder
	Dim fb,fb1 As FloatingActionButton
	Dim ph As Phone
	Dim Banner As AdView
	Dim Interstitial As InterstitialAd
End Sub

Sub Activity_Create(FirstTime As Boolean)
	
	ph.SetScreenOrientation(1)
	
	Activity.LoadLayout("Lay1")
	fb.Icon = res.GetDrawable("ic_add_white_24dp")
	fb.HideOffset = 100%y - fb.Top
	fb.Hide(False)
	fb.Show(True)
	
	fb1.Icon = res.GetDrawable("about")
	fb1.HideOffset = 100%y - fb.Top
	fb1.Hide(False)
	fb1.Show(True)
	
	ToastMessageShow("You can see APN in Notification!",True)
	
	noti4.Initialize
	noti4.Sound = False
	noti4.Icon = "icon"
	noti4.SetInfo("APN Protocol","IPV4",Me)
	noti4.Notify(1)
	
	noti3.Initialize
	noti3.Sound = False
	noti3.Icon = "icon"
	noti3.SetInfo("MNC","01",Me)
	noti3.Notify(2)
	
	noti2.Initialize
	noti2.Sound = False
	noti2.Icon = "icon"
	noti2.SetInfo("MCC","414",Me)
	noti2.Notify(3)
	
	noti1.Initialize
	noti1.Sound = False
	noti1.Icon = "icon"
	noti1.SetInfo("APN","mptnet",Me)
	noti1.Notify(4)
	
	noti.Initialize
	noti.Sound = False
	noti.Icon = "icon"
	noti.SetInfo("Name","MPT",Me)
	noti.Notify(5)
	
	Activity.Color = Colors.White
	
	imv.Initialize("")
	imv.Bitmap = LoadBitmap(File.DirAssets,"mpt.png")
	imv.Gravity = Gravity.FILL
	Activity.AddView(imv,50%x - 50dip,10dip,100dip,100dip)
	
	lb.Initialize("lb")
	lb.Text = "Name : MPT"
	lb.TextColor = Colors.Black
	Activity.AddView(lb,0%x,(imv.Top+imv.Height)+2%y,100%x,10%y)
	lb.Gravity = Gravity.CENTER
	
	lb1.Initialize("lb1")
	lb1.Text = "APN : mptnet"
	lb1.TextColor = Colors.Black
	Activity.AddView(lb1,0%x,(lb.Top+lb.Height),100%x,10%y)
	lb1.Gravity = Gravity.CENTER
	
	lb2.Initialize("lb2")
	lb2.Text = "MCC : 414"
	lb2.TextColor = Colors.Black
	Activity.AddView(lb2,0%x,(lb1.Top+lb1.Height),100%x,10%y)
	lb2.Gravity = Gravity.CENTER
	
	lb3.Initialize("lb3")
	lb3.Text = "MNC : 01"
	lb3.TextColor = Colors.Black
	Activity.AddView(lb3,0%x,(lb2.Top+lb2.Height),100%x,10%y)
	lb3.Gravity = Gravity.CENTER
	
	lb4.Initialize("lb4")
	lb4.Text = "APN Protocol : IPV4"
	lb4.TextColor = Colors.Black
	Activity.AddView(lb4,0%x,(lb3.Top+lb3.Height),100%x,10%y)
	lb4.Gravity = Gravity.CENTER
	
	bbg.Initialize(Colors.Black , 15)
	b.Initialize("b")
	b.Text = "Start"
	b.Background = bbg
	Activity.AddView(b,20%x,(lb4.Top+lb4.Height)+2%y,60%x,50dip)
	
	Banner.Initialize("Banner"," ca-app-pub-4173348573252986/4847019357")
	Banner.LoadAd
	Activity.AddView(Banner,0%x,100%y - 80dip,100%x,50dip)
		
	Interstitial.Initialize("Interstitial","ca-app-pub-4173348573252986/6323752558")
	Interstitial.LoadAd
		
	ad1.Initialize("ad1",100)
	ad1.Enabled = False
	ad2.Initialize("ad2",15000)
	ad2.Enabled = True
End Sub

Sub lb_Click
	lb.SetTextColorAnimated(1000,Colors.Green)
	copy.clrText
	copy.setText("MPT")
	ToastMessageShow("MPT Copied",True)
End Sub

Sub lb1_Click
	lb1.SetTextColorAnimated(1000,Colors.Blue)
	copy.clrText
	copy.setText("mpnet")
	ToastMessageShow("mecnet Copied",True)
End Sub

Sub lb2_Click
	lb2.SetTextColorAnimated(1000,Colors.Red)
	copy.clrText
	copy.setText("414")
	ToastMessageShow("414 Copied",True)
End Sub

Sub lb3_Click
	lb3.SetTextColorAnimated(1000,Colors.Cyan)
	copy.clrText
	copy.setText("01")
	ToastMessageShow("01 Copied",True)
End Sub

Sub lb4_Click
	lb4.SetTextColorAnimated(1000,Colors.Magenta)
	copy.clrText
	copy.setText("IPV4")
	ToastMessageShow("IPV4 Copied",True)
End Sub

Sub b_Click
	If ph.SdkVersion > 19 Then
		ad1.Enabled = True
	End If
	ToastMessageShow("Click Add for set New APN Setting",True)
	Dim intent As Intent
	intent.Initialize("android.settings.APN_SETTINGS","")
	StartActivity(intent)
	noti4.Initialize
	noti4.Sound = False
	noti4.Icon = "icon"
	noti4.SetInfo("APN Protocol","IPV4",Me)
	noti4.Notify(1)
	
	noti3.Initialize
	noti3.Sound = False
	noti3.Icon = "icon"
	noti3.SetInfo("MNC","01",Me)
	noti3.Notify(2)
	
	noti2.Initialize
	noti2.Sound = False
	noti2.Icon = "icon"
	noti2.SetInfo("MCC","414",Me)
	noti2.Notify(3)
	
	noti1.Initialize
	noti1.Sound = False
	noti1.Icon = "icon"
	noti1.SetInfo("APN","mptnet",Me)
	noti1.Notify(4)
	
	noti.Initialize
	noti.Sound = False
	noti.Icon = "icon"
	noti.SetInfo("Name","MPT",Me)
	noti.Notify(5)
	
	
End Sub


Sub fb_Click
	ToastMessageShow("App Update နွင့္ App အသစ္ေတြ " & CRLF & "Facebook ကေနဖတ္ရူ့သိနိုင္ဖို့" &CRLF& "Myanmar Android Apps ကို Like ထားလိုက္ပါ။",True)
	Try
		Dim Facebook As Intent
		Facebook.Initialize(Facebook.ACTION_VIEW, "fb://page/627699334104477")
		StartActivity(Facebook)
 
	Catch

		Dim i As Intent
		i.Initialize(i.ACTION_VIEW, "https://m.facebook.com/MmFreeAndroidApps")
		StartActivity(i)
	End Try
End Sub

Sub fb1_Click
	Dim ShareIt As Intent
	copy.clrText
	copy.setText("All in One Myanmar APN Setting Apps" &CRLF & "Download Free at : www.htetznaing.com/search?q=Myanmar+APNs" &CRLF & " #Ht3tzN4ing" &CRLF & " #MyanmarAndroidApps")
	ShareIt.Initialize (ShareIt.ACTION_SEND,"")
	ShareIt.SetType ("text/plain")
	ShareIt.PutExtra ("android.intent.extra.TEXT",copy.getText)
	ShareIt.PutExtra ("android.intent.extra.SUBJECT","Get Free!!")
	ShareIt.WrapAsIntentChooser("Share App Via...")
	StartActivity (ShareIt)
End Sub

Sub Activity_Resume

End Sub

Sub Activity_Pause (UserClosed As Boolean)

End Sub

Sub ad1_Tick
	If Interstitial.Ready Then Interstitial.Show Else Interstitial.LoadAd
	ad1.Enabled = False
End Sub

Sub ad2_Tick
	If Interstitial.Ready Then Interstitial.Show Else Interstitial.LoadAd
End Sub

Sub Interstitial_AdClosed
	Interstitial.LoadAd
End Sub