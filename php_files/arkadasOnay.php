<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 
define('GOOGLE_API_KEY', 'AIzaSyBuPCnx9dZ_mWPexbkITmcWkWsyj7OZX4c');//GCM Sunucu Key


require_once 'include/db_handler.php';
require 'libs/Slim/Slim.php';
require_once 'libs/gcm/gcm.php';
require_once 'libs/gcm/push.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();



if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend']) && isset($_POST['txtb_no'])  )
{
	
	
	
	
	$txtKullanici=$_POST['txtKullanici'];
	 $txtfriend=$_POST['txtfriend'];
		$b_no=$_POST['txtb_no'];

	$sorgu2="select  kullanicilar.gcm_registration_id,kullanicilar.eposta  from kullanicilar  where eposta='$txtKullanici'";
  $sonuc2=mysqli_query($link,$sorgu2);
 $veriler2=mysqli_fetch_assoc($sonuc2);
      $toUser['gcm_registration_id']=$veriler2['gcm_registration_id'];

	  $fromUser= getUser($txtfriend);
	    $msg = array();
			    $msg = array();
    $msg['message'] = "Arkadaşlık isteğiniz kabul edildi.";
    $msg['resimno'] = "";
     $msg['resimyol'] = "";
	  $data = array();
    $data['user'] = $fromUser;
    $data['message'] = $msg;
    //$data['imageUrl'] = $path;
$push = new Push();
    $push->setTitle("Arkadas Ekle");
    $push->setIsBackground(FALSE);
    $push->setFlag(1);
    $push->setData($data);
    $push->setImageUrl("0");
	$push->setGrup("5"); //arkadas Onayla
	$push->setSahibi("0");
	
    

	
	        $sqlinsert="insert into bildirimler (eposta,gondereneposta,adi,foto,mesaj,resim_no,resimyol,grup)
 VALUES ('$txtKullanici','$txtfriend','".$fromUser["name"]."','".$fromUser["foto"]."','Arkadaşlık isteğiniz kabul edildi','','','5')";
	
	mysqli_query($link,$sqlinsert);
  $sql="UPDATE arkadaslar SET  onay='1' where kullanici='$txtKullanici'  and friend='$txtfriend' ";
   $sql3="UPDATE arkadaslar SET  onay='1' where  friend='$txtKullanici' and kullanici='$txtfriend'";
   
 $sqlpuanartir="UPDATE kullanicilar SET  Puan=Puan+'".$fromUser["Harcama"]."' where  eposta='$txtKullanici'";
 $sqlpuazalt="UPDATE kullanicilar SET  Puan=Puan-'".$fromUser["Harcama"]."' where  eposta='$txtfriend' ";

mysqli_query($link,$sql);mysqli_query($link,$sql3);
mysqli_query($link,$sqlpuanartir);mysqli_query($link,$sqlpuazalt);

$sql2="delete from bildirimler where b_no='$b_no'";
 mysqli_query($link,$sql2);
 $registration_ids = array();
	 array_push($registration_ids, $toUser['gcm_registration_id']);
	 sendPushNotification2( $registration_ids,  $push->getPush());
	
	




echo "1";




}
	  function getUser($gonderen) {
  include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 
   $sonuc3=mysqli_query($link,"SELECT Harcama,foto,kayit_no as user_id, adi as name, eposta as email, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE eposta = '$gonderen'");
	
       $result = mysqli_fetch_assoc($sonuc3);
        
        
            $user = array();
            $user["user_id"] = $result['user_id'];
			$user["foto"] = $result['foto'];

            $user["name"] =$result['name'];
			            $user["Harcama"] =$result['Harcama'];

            $user["email"] = $result['email'];
            $user["gcm_registration_id"] = $result['gcm_registration_id'];
            $user["created_at"] = $result['created_at'];
            
            return $user;
       
    }
	
function sendPushNotification2($registration_ids,$mesaj) {
         
//mysqli_set_charset($link,'utf8mb4'); 

		  $url = 'https://android.googleapis.com/gcm/send';
   
        // $mesaj = array("notification_message" => $mesaj); //gönderdiğimiz mesaj POST 'tan alıyoruz.Androidde okurken notification_message değerini kullanacağız
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
          $result;
  

}