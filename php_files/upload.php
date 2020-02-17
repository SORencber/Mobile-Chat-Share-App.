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

if(isset($_POST['image']) &&  isset($_POST['txtDil']) &&  isset($_POST['txtbaslik']) && isset($_POST['txtGizlilik']) && isset($_POST['txteposta'])){
     $image = $_POST['image'];
	 $txtbaslik = $_POST['txtbaslik'];
	  $txtGizlilik = $_POST['txtGizlilik'];
	   $txteposta = $_POST['txteposta'];
	   	   $txtDil = $_POST['txtDil'];
	   	   $txtUlke = $_POST['txtUlke'];
	   	   $txtSehir = $_POST['txtSehir'];
	   	   $txtIlce = $_POST['txtIlce'];
	   	   $txtadres = $_POST['txtadres'];
	   	   $txtlatitude = $_POST['txtlatitude'];
	   	   $txtlongitude = $_POST['txtlongitude'];

		   $sqlsorgu="UPDATE kullanicilar SET latitude='$txtlatitude', longitude='$txtlongitude' where eposta='$txteposta'";
		   mysqli_query($link,$sqlsorgu);
    upload($_POST['image'], $_POST['txtbaslik'],$_POST['txtGizlilik'],$_POST['txteposta'],$link,$txtDil,$txtUlke,$txtSehir,$txtIlce,$txtadres);
		
    exit;
}
else{
    echo "image_not_in";
    exit;
}
function upload($image,$txtbaslik,$txtGizlilik,$txteposta,$link, $txtDil,$txtUlke,$txtSehir,$txtIlce,$txtadres){
    //create unique image file name based on micro time and date
    $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
    $upload_folder = "resimler"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    
	
	
    //Cannot use "== true"
	   $sorgu="insert into resimler (resim_baslik,resim_adi,gizlilik,kullanici,dil,Ulke,Sehir,ilce,adres) VALUES 
	   ('$txtbaslik','resimler/$id.jpeg','$txtGizlilik','$txteposta','$txtDil','$txtUlke','$txtSehir','$txtIlce','$txtadres') "; 	
 $sonuc=mysqli_query($link, $sorgu);
		       $resim_no = mysqli_insert_id($link);

    if(file_put_contents($path, base64_decode($image)) != false ){
		
		
				  $fromUser= getUser($txteposta);

		
		    $msg = array();
    $msg['message'] = "Resim Ekledi";
    $msg['resimno'] = $resim_no;
     $msg['resimyol'] = $path;
	 $msq['resimsahibi']=$txteposta;

	  $data = array();
    $data['user'] = $fromUser;
    $data['message'] = $msg;
    //$data['imageUrl'] = $path;
$push = new Push();
    $push->setTitle("Yorum");
    $push->setIsBackground(FALSE);
    $push->setFlag(1);
    $push->setData($data);
    $push->setImageUrl("0");
	$push->setGrup("3");
	$push->setSahibi("0");   
	
	  
	  
	 
	
		
		
     $registration_ids = array();

	 $bildirimcilerSql="select *  from arkadaslar where onay='1' and kullanici='$txteposta' and sessiz='0' ";
		$bildirimcilerSqls=mysqli_query($link,$bildirimcilerSql);
	
	 
	 while ($veriler=mysqli_fetch_array($bildirimcilerSqls))
	 {
	     $sonuc3=mysqli_query($link,"SELECT kayit_no as user_id, adi as name, eposta, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE  eposta = '".$veriler['friend']."'");
	
       $result = mysqli_fetch_assoc($sonuc3);
          if($result['eposta']!=$txteposta){
	  $sqlinsert="insert into bildirimler (eposta,gondereneposta,adi,foto,mesaj,resim_no,resimyol,grup)
 VALUES ('".$result['eposta']."','$txteposta','".$fromUser["name"]."','".$fromUser["foto"]."','Resim Ekledi','$resim_no','$path','3')";
	
	mysqli_query($link,$sqlinsert);
	  	  array_push($registration_ids, $result['gcm_registration_id']);

	  }  
		
	 }
		  sendPushNotification2( $registration_ids,  $push->getPush());

	
		
		
        echo "uploaded_success";
    }
    else{
        echo "uploaded_failed";
    } 
mysqli_close($link);	
}
 function getUser($gonderen) {
       include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 
                
	     $sonuc3=mysqli_query($link,"SELECT foto,kayit_no as user_id, adi as name, eposta as email, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE eposta = '$gonderen'");
	
       $result = mysqli_fetch_assoc($sonuc3);
        
        
            $user = array();
            $user["user_id"] = $result['user_id'];
			$user["foto"] = $result['foto'];

            $user["name"] =$result['name'];
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
  

?>