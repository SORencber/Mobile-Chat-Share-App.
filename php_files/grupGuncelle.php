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
		 $sql="select kullanici,friend from arkadaslar where kullanici='$OdaIsmi' and friend='$username' and room_id='$chat_room_id' ";
	$sonuc2=mysqli_query($link,$sql);
	if((mysqli_fetch_assoc($sonuc2) < 1))
	{
         $sql="insert into arkadaslar(kullanici,friend,room_id,onay,dil,grup,sahibi) values('$OdaIsmi','$username','$chat_room_id','1','tr','1','0')";
    }
          mysqli_query($link,$sql);
			}


echo "1";
