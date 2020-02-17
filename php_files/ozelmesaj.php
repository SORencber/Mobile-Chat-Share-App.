<html>
<head>
 <meta http-equiv="Content-Type" content="text/html; charset=utf-8"> <!--Türkçe karakter sorunu yaşamamak için-->

 <title>GCM Send</title>
</head>
<body>
<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 
define('GOOGLE_API_KEY', 'AIzaSyBuPCnx9dZ_mWPexbkITmcWkWsyj7OZX4c');//GCM Sunucu Key


if(  isset($_POST['txtMessage']) &&  isset($_POST['txtkime']) && isset($_POST['txteposta']) && isset($_POST['txtDil'])  )
{
	
	$kime=$_POST['txtkime'];
    $kimden=$_POST['txteposta'];
    $dil=$_POST['txtDil'];
	$message=$_POST['txtMessage'];
   // $gcmRegIds=$_POST['txtRegistration_ID'];
     $registration_ids = array();//registration idlerimizi tutacak array ı oluşturuyoruz
   
   $sql = "SELECT * FROM kullanicilar where eposta='$kime'";//Tüm kullanıcı gcm registration idlerini alıcak sql sorgumuz
   $result = mysqli_query($link, $sql);//sorguyu çalıştırıyoruz
   while($row = mysqli_fetch_assoc($result)){
    array_push($registration_ids, $row['gcm_registration_id']);//databaseden dönen registration idleri $registration_ids arrayine atıyoruz
   }

  sendPushNotification($registration_ids, $message);
	  $sorgu="insert into ozelmesajlar (kimden,kime,icerik,dil) VALUES ('$kimden','$kime','$message','$dil') ";
	
	 $sonuc=mysqli_query($link, $sorgu);
	
	if ($sonuc > 0 )
	{
		echo "";
		exit;
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}

}
	function sendPushNotification($registration_ids,$mesaj) {
          $url = 'https://gcm-http.googleapis.com/gcm/send';
   
    $mesaj = array("notification_message" => $_POST['txtMessage']); //gönderdiğimiz mesaj POST 'tan alıyoruz.Androidde okurken notification_message değerini kullanacağız
         $fields = array(
             'registration_ids' => $registration_ids,
             'data' => $mesaj,
         );
		
		//Alttaki Authorization: key= kısmına Google Apis kısmında oluşturduğumuz key'i yazacağız
         $headers = array(
             'Authorization: key=".$GOOGLE_API_KEY."', 
             'Content-Type: application/json'
         );
         // Open connection
         $ch = curl_init();
   
         // Set the url, number of POST vars, POST data
         curl_setopt($ch, CURLOPT_URL, $url);
   
         curl_setopt($ch, CURLOPT_POST, true);
         curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
         curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
   
         // Disabling SSL Certificate support temporarly
         curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
   
         curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
   
         // Execute post
         $result = curl_exec($ch);
         if ($result === FALSE) {
             die('Curl failed: ' . curl_error($ch));
         }
   
         // Close connection
         curl_close($ch);
         echo $result;
  

}
?>
<form action="ozelmesaj.php" method="POST">

kullanici:<input type="text" name="txtMessage" value="selam">
kullanici:<input type="text" name="txtkime" value="s@s.com">

kullanici:<input type="text" name="txteposta" value="d@d.com">

kull:<input type="text" name="txtDil" value="tr">

<input type="submit" value=" Gir ">
</form>