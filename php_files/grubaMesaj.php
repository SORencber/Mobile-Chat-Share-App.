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

if(  isset($_POST['room_id']) && isset($_POST['user_id']) && isset($_POST['to_user_id'])  )
{
	mysqli_set_charset($link,'utf8mb4'); 

	// $eposta=$_GET['eposta'];
	  $chat_room_id=$_POST['room_id'];
	 $user_id=$_POST['user_id'];
	 $message=$_POST['message'];
	 $imageUrl=$_POST['image'];
 $sahibi=$_POST['sahibi'];
	 $grup=$_POST['grup'];
	   $nedir=$_POST['nedir']; // 0 sadece mesaj, 1 sadece resim, 2 sadece ses dosyasi
	
	 $to_user_id=$_POST['to_user_id'];

	  $path = ""; 

		if($nedir=="0")
		{
		}else if ($nedir=="1") {
			 $message="Resim Gönderildi";
		 $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
     $upload_folder = "sohbetfoto"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg"; 
    if(file_put_contents($path, base64_decode($imageUrl)) != false ){
        $path = "$upload_folder/$id.jpeg"; 
    }
	
		 }
    else if ($nedir=="2"){
	 $message="Sesli Mesaj Gönderildi";	

	$file_path = "sohbetses/";
     
     $file_path = $file_path . basename( $_FILES['ses']['name']);
    if(move_uploaded_file($_FILES['ses']['tmp_name'], $file_path) ){
		$path = $file_path; 

    } else{
       echo "fail";
    }

    } 
	 

        $response =  addMessage($user_id, $chat_room_id, $message,$path);

    

     
        $push = new Push();

        // get the user using userid
        $fromUser= getUser($user_id);
     
        $toUser = getUser($to_user_id);
       
	   $msg = array();
    $msg['message'] = $message;
    $msg['message_id'] = "";
    $msg['chat_room_id'] = $chat_room_id;
	 $msg['imageUrl'] = $path;
	 $msg['foto'] = $fromUser['foto'];

    $msg['created_at'] = date('Y-m-d G:i:s');

    $data = array();
    $data['user'] = $fromUser;
    $data['message'] = $msg;
    $data['imageUrl'] = $path;

    $push->setTitle("Google Cloud Messaging");
    $push->setIsBackground(FALSE);
    $push->setFlag(1);
    $push->setData($data);
    $push->setImageUrl($path);
	$push->setGrup($grup);
	$push->setSahibi("0");
    // sending push message to single user
    //$gcm->send($toUser['gcm_registration_id'], $push->getPush());
    // send($toUser['gcm_registration_id'], $data);
    $response['user'] = $fromUser;
    $response['error'] = false;
    //sendToTopic('topic_' . $chat_room_id, $push->getPush());
	 echoRespnse(200, $response);
     
	 $registration_ids = array();
	 
	 $grupsorgu=mysqli_query($link,"select * from arkadaslar where room_id='$chat_room_id'");
	 
	 while ($veriler=mysqli_fetch_array($grupsorgu))
	 {
	     $sonuc3=mysqli_query($link,"SELECT kayit_no as user_id, adi as name, eposta as email, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE eposta = '".$veriler['friend']."'");
	
       $result = mysqli_fetch_assoc($sonuc3);
        
		if ($_POST['user_id']!=$result['user_id']){
	  array_push($registration_ids, $result['gcm_registration_id']);
		}
	 }
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
  function send($to, $message) {
        $fields = array(
            'to' => $to,
            'data' => $message,
        );
        return sendPushNotification($fields);
    }

    // Sending message to a topic by topic id
     function sendToTopic($to, $message) {
        $fields = array(
            'to' => '/topics/' . $to,
            'data' => $message,
        );
        return sendPushNotification($fields);
    }

    // sending push message to multiple users by gcm registration ids
     function sendMultiple($registration_ids, $message) {
        $fields = array(
            'registration_ids' => $registration_ids,
            'data' => $message,
        );

        return sendPushNotification($fields);
    }

    // function makes curl request to gcm servers
     function sendPushNotification($fields) {
        // include config

        // Set POST variables
          $url = 'https://android.googleapis.com/gcm/send';

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

        return $result;
    }

	  function getUser($user_id) {
       include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 

	     $sonuc3=mysqli_query($link,"SELECT foto,kayit_no as user_id, adi as name, eposta as email, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE kayit_no = '$user_id'");
	
       $result = mysqli_fetch_assoc($sonuc3);
        
        
            $user = array();
						$user["foto"] = $result['foto'];

            $user["user_id"] = $result['user_id'];
            $user["name"] =$result['name'];
            $user["email"] = $result['email'];
            $user["gcm_registration_id"] = $result['gcm_registration_id'];
            $user["created_at"] = $result['created_at'];
            
            return $user;
       
    }

	  function addMessage($user_id, $chat_room_id, $message,$path) {
        $response = array();
        include "baglanti.php";
         mysqli_set_charset($link,'utf8mb4'); 
		 
		 
	
   
		
         $sonuc=mysqli_query($link,"INSERT INTO messages ( user_id,chat_room_id ,message,image) values('$user_id', '$chat_room_id', '$message','$path')");
	       $id = mysqli_insert_id($link);
	    
       // $result = mysqli_fetch_assoc($sonuc);
        	$updatearkadaslarsql="update arkadaslar SET son_yazi_tarihi=NOW() WHERE room_id='$chat_room_id'";
							 $sonucupdate=mysqli_query($link,$updatearkadaslarsql);

		$response['error'] = false;

            // get the message
             
            //$MesajiGetirSorgu="SELECT message_id, user_id, chat_room_id, message,image, created_at FROM messages WHERE message_id = '$id'";
   $MesajiGetirSorgu="SELECT c.okundu,u.foto,cr.chat_room_id, cr.name, cr.created_at as chat_room_created_at, u.adi as username, c.* FROM chat_rooms cr LEFT JOIN messages c ON c.chat_room_id = cr.chat_room_id LEFT JOIN kullanicilar u ON u.kayit_no = c.user_id WHERE  c.message_id = '$id'"; 
$sonuc2=mysqli_query($link,$MesajiGetirSorgu);
            $veriler = mysqli_fetch_assoc($sonuc2);
                
				
				$tmp = array();
                $tmp['message_id'] = $veriler['message_id'];
                $tmp['chat_room_id'] = $veriler['chat_room_id'];
                $tmp['message'] =  $veriler['message'];
				$tmp['imageUrl'] =  $veriler['image'];
				$tmp['foto'] =  $veriler['foto'];
				$tmp['okundu'] =  $veriler['okundu'];

                $tmp['created_at'] =  $veriler['created_at'];
                $response['message'] = $tmp;
            
      

        return $response;
    }
	function upload($image,$user_id,$chat_room_id,$link){
    //create unique image file name based on micro time and date
    $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
    $upload_folder = "sohbetfoto"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    
    //Cannot use "== true"
	    $sorgu="insert into messages (image,user_id,chat_room_id) VALUES ('sohbetfoto/$id.jpeg','$user_id','$chat_room_id') "; 	
 $sonuc=mysqli_query($link, $sorgu);
	
    if(file_put_contents($path, base64_decode($image)) != false ){
       return $path;
    }
    else{
       // echo "uploaded_failed";
    } 
mysqli_close($link);	
}
	function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}
  
	
//$app->run();

?>


