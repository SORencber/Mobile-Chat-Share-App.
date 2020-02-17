<?php
ini_set('display_errors', 0);
ini_set('display_startup_errors', 0);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


	$user_id=$_POST['user_id'];
		$chat_room_id=$_POST['chat_room_id'];
	$grup=$_POST['grup'];

	$toplam=$_POST['toplam'];
        
		   if ($grup=="0")
		   {	        for($i = 0; $i <$toplam; $i++){
         $message_id = $_POST['mesaj'][$i];
         $sql="delete from  messages Where message_id='$message_id' ";

          mysqli_query($link,$sql);
			}
		   }else {
			   
			    for($i = 0; $i <$toplam; $i++){
         $message_id = $_POST['mesaj'][$i];
         $sql="delete from  messages Where message_id='$message_id' and $user_id='$user_id' ";

          mysqli_query($link,$sql);
			}   
			   
		   }
		   

echo "1";
?>