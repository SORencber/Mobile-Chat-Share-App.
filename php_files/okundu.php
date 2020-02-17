<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 
define('GOOGLE_API_KEY', 'AIzdaSyBuPCnx9dZ_dmWPddfexbkITmcWkWsyj7fOZX4c');//GCM Sunucu Key


require_once 'include/db_handler.php';
require 'libs/Slim/Slim.php';
require_once 'libs/gcm/gcm.php';
require_once 'libs/gcm/push.php';
\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

if(  isset($_POST['room_id']) && isset($_POST['kayit_no']) && isset($_POST['gcm_registration_id']) )
{

	// $eposta=$_GET['eposta'];
	 $chat_room_id=$_POST['room_id'];
	 	 $kayit_no=$_POST['kayit_no'];

	 $gcm_registration_id=$_POST['gcm_registration_id'];

	 $grup=$_POST['grup'];
     
	 	$sql="UPDATE messages SET okundu='1' Where chat_room_id='$chat_room_id' and user_id='$kayit_no'";
		mysqli_query($link,$sql);
	
	 

      
        $push = new Push();

        // get the user using userid
       
       
	   $msg = array();
   // $msg['message'] = $message;
    $msg['message_id'] = "";
    $msg['chat_room_id'] = $chat_room_id;

    $data['message'] = $msg;

    
    $push->setTitle("Google Cloud Messaging");
    $push->setIsBackground(FALSE);
    $push->setFlag(1);
    $push->setData($data);
	$push->setGrup("8");
	$push->setSahibi("0");
   
     
	 $registration_ids = array();
	  array_push($registration_ids, $gcm_registration_id);
	  sendPushNotification2( $registration_ids,  $push->getPush());
	 
	
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


