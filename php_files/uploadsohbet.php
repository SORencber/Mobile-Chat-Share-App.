<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


if(isset($_POST['image']) &&  isset($_POST['user_id']) &&  isset($_POST['chatRoomId']) ){
     $image = $_POST['image'];
	 $user_id = $_POST['user_id'];
	  $chatRoomId = $_POST['chatRoomId'];
	   

    upload($_POST['image'], $_POST['user_id'],$_POST['chatRoomId'],$link);
    exit;
}
else{
    echo "image_not_in";
    exit;
}
function upload($image,$user_id,$chatRoomId,$link){
    //create unique image file name based on micro time and date
    $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
    $upload_folder = "sohbetfoto"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    
    //Cannot use "== true"
	    $sorgu="insert into messages (image,user_id,chat_room_id) VALUES ('sohbetfoto/$id.jpeg','$user_id','$chatRoomId') "; 	
 $sonuc=mysqli_query($link, $sorgu);
	
    if(file_put_contents($path, base64_decode($image)) != false ){
        echo "uploaded_success";
    }
    else{
        echo "uploaded_failed";
    } 
mysqli_close($link);	
}

?>