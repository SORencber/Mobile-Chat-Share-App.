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
	
if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend'])  )
{	$data = array();

	$txtKullanici=$_POST['txtKullanici'];
	$txtfriend=$_POST['txtfriend'];
	$sorgu2="Select * from arkadaslar where (kullanici='$txtKullanici' and friend='$txtfriend') OR (kullanici='$txtfriend' and friend='$txtKullanici')";
	$sonuc2=mysqli_query($link,$sorgu2);
	
	/*if((mysqli_fetch_assoc($sonuc2) < 1) and ($txtKullanici!=$txtfriend))
	{
	
	
	$sonuc=mysqli_query($link,"INSERT INTO chat_rooms (name) values('Kullanici Odası')");
	       $roomid = mysqli_insert_id($link);
	        
 $sql="insert into arkadaslar(kullanici,friend,room_id,onay,dil) values('$txtKullanici','$txtfriend','$roomid','1','tr')";
 $sql2="insert into arkadaslar(kullanici,friend,room_id,onay,dil) values('$txtfriend','$txtKullanici','$roomid','1','tr')";
mysqli_query($link,$sql); mysqli_query($link,$sql2);

	}
	*/
	$sorgu="select  arkadaslar.room_id as no,arkadaslar.tarih,kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.foto,kullanicilar.cinsiyet,arkadaslar.online,kullanicilar.adi,kullanicilar.soyadi,arkadaslar.dil, 
	arkadaslar.grup,arkadaslar.sahibi,arkadaslar.kullanici from arkadaslar join kullanicilar on arkadaslar.kullanici=kullanicilar.eposta where  arkadaslar.kullanici='$txtKullanici' and arkadaslar.friend='$txtfriend' and onay='1'  ";
	
		 
		 
		 
	$sonuc=mysqli_query($link, $sorgu);
	while($veriler=mysqli_fetch_assoc($sonuc))
	{
	mysqli_set_charset($link,'utf8mb4'); 
   $data["error"]=false;
		$data["odabilgileri"]=$veriler;
	}
		mysqli_set_charset($link,'utf8mb4'); 

	$degistir="";
	$ara='\\';
	//echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);
function echoRespnse($status_code, $data) {
    $app = \Slim\Slim::getInstance();
    // Http response code
    $app->status($status_code);

    // setting response content type to json
    $app->contentType('application/json');

    echo json_encode($data);
}
 echoRespnse(200, $data);
	
}
	 
	
	
?>
