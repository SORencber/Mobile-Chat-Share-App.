<?php
ini_set('display_errors', 0);
ini_set('display_startup_errors', 0);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


	$OdaIsmi=$_POST['OdaIsmi'];
		$chat_room_id=$_POST['chat_room_id'];

	$toplam=$_POST['toplam'];
        
		   
	        for($i = 0; $i <$toplam; $i++){
         $username = $_POST['friend'][$i];
         $sql="delete from  arkadaslar Where kullanici='$OdaIsmi' and friend='$username'";

          mysqli_query($link,$sql);
			}


echo "1";
