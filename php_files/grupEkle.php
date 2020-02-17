<?php
ini_set('display_errors', 0);
ini_set('display_startup_errors', 0);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


	$OdaIsmi=$_POST['OdaIsmi'];
		$grupbaskani=$_POST['grupbaskani'];

	$toplam=$_POST['toplam'];
		mysqli_query($link,"INSERT INTO kullanicilar (eposta,foto) values('$OdaIsmi','varsayilan/grup.png')");
        
	$sonuc=mysqli_query($link,"INSERT INTO chat_rooms (name) values('$OdaIsmi')");
	       $roomid = mysqli_insert_id($link);
		   
	        for($i = 0; $i <$toplam; $i++){
         $username = $_POST['friend'][$i];
         $sql="insert into arkadaslar(kullanici,friend,room_id,onay,dil,grup,sahibi) values('$OdaIsmi','$username','$roomid','1','tr','1','0')";

          mysqli_query($link,$sql);
			}
         $sql2="insert into arkadaslar(kullanici,friend,room_id,onay,dil,grup,sahibi) values('$OdaIsmi','$grupbaskani','$roomid','1','tr','1','1')";
mysqli_query($link,$sql2);


echo "1";
