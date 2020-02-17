<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include "baglanti.php";
	mysqli_set_charset($link,'utf8'); 

if(  isset($_POST['txteposta']) &&  isset($_POST['txtsqleposta'])&&  isset($_POST['txtroomid']))
{
	
	$kullanici=$_POST['txteposta'];
    $friend=$_POST['txtsqleposta'];
      $roomid=$_POST['txtroomid'];

     //unlink($_POST['txtthumbnail']);
   

     $sorgu="delete from arkadaslar where friend='$friend' and kullanici='$kullanici' OR friend='$kullanici' and kullanici='$friend' "; 	
    mysqli_query ($link,"delete from messages where chat_room_id='$roomid' ");
	mysqli_query ($link,"delete from chat_rooms where chat_room_id='$roomid' ");

 $sonuc=mysqli_query($link, $sorgu);
 
 if ($sonuc > 0 )
	{
		echo "success_login";
		exit;
	}else {
		
		echo "Olmadı";
	    exit;
	}
	mysqli_close($link);
}
?>