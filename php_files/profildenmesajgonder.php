<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 




if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend'])  )
{$txtKullanici=$_POST['txtKullanici'];
	$txtfriend=$_POST['txtfriend'];
	$sorgu2="Select * from arkadaslar where (kullanici='$txtKullanici' and friend='$txtfriend') OR (kullanici='$txtfriend' and friend='$txtKullanici')";
	$sonuc2=mysqli_query($link,$sorgu2);
	
	if((mysqli_fetch_assoc($sonuc2) < 1) and ($txtKullanici!=$txtfriend))
	{
	
	
	$sonuc=mysqli_query($link,"INSERT INTO chat_rooms (name) values('Kullanici Odası')");
	       $roomid = mysqli_insert_id($link);
	        
 $sql="insert into arkadaslar(kullanici,friend,room_id,onay,dil) values('$txtKullanici','$txtfriend','$roomid','1','tr')";
 $sql2="insert into arkadaslar(kullanici,friend,room_id,onay,dil) values('$txtfriend','$txtKullanici','$roomid','1','tr')";
mysqli_query($link,$sql); mysqli_query($link,$sql2);

echo "roomid";
	}else{ echo "0";}
}