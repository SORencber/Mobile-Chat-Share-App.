<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 


require_once 'include/db_handler.php';
require 'libs/Slim/Slim.php';

\Slim\Slim::registerAutoloader();

$app = new \Slim\Slim();

if(  isset($_GET['room_id']) )
{
	
	// $eposta=$_GET['eposta'];
	 $chatRoomId=$_GET['room_id'];

	mysqli_set_charset($link,'utf8mb4'); 
	
	
	$data = array();
	
//	  $sorgu= "SELECT cr.chat_room_id, cr.name, cr.created_at as chat_room_created_at, u.name as username, c.* FROM chat_rooms cr LEFT JOIN messages c ON c.chat_room_id = cr.chat_room_id LEFT JOIN kullanicilar u ON u.kayit_no = c.user_id WHERE cr.chat_room_id = '$chatRoomId' limit 0,20");
	$sorgu="SELECT c.okundu,u.foto,u.eposta,cr.chat_room_id, cr.name, cr.created_at as chat_room_created_at, u.adi as username, c.* FROM chat_rooms cr LEFT JOIN messages c ON c.chat_room_id = cr.chat_room_id LEFT JOIN kullanicilar u ON u.kayit_no = c.user_id WHERE cr.chat_room_id ='$chatRoomId' "; 
	$sonuc=mysqli_query($link,$sorgu);
	
	
	$response["error"] = false;
    $response["messages"] = array();
   // $response['chat_room'] = array();
	$i = 0;
    // looping through result and preparing tasks array
    while ($chat_room = mysqli_fetch_assoc($sonuc)) {
        // adding chat room node
        /*if ($i == 0) {
            $tmp = array();
            $tmp["chat_room_id"] = $chat_room["chat_room_id"];
            $tmp["name"] = $chat_room["name"];
            $tmp["created_at"] = $chat_room["chat_room_created_at"];
            $response['chat_room'] = $tmp;
        }
*/
        if ($chat_room['user_id'] != NULL) {
            // message node
            $cmt = array();
            $cmt["message"] = $chat_room["message"];
            $cmt["message_id"] = $chat_room["message_id"];
             $cmt["image"] = $chat_room["image"];
			$cmt["sesUrl"] = $chat_room["ses"];

             $cmt["okundu"] = $chat_room["okundu"];

            $cmt["created_at"] = $chat_room["created_at"];
            
            // user node
            $user = array();
            $user['user_id'] = $chat_room['user_id'];
            $user['username'] = $chat_room['username'];

			            $user['foto'] = $chat_room['foto'];
			            $user['email'] = $chat_room['eposta'];

            $cmt['user'] = $user;

            array_push($response["messages"], $cmt);
        }
    }
	function echoRespnse($status_code, $response) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($response);
}
  
	 echoRespnse(200, $response);
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);

//$app->run();

}
?>