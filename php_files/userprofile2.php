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

if(  isset($_GET['to_user_id']) )
{
	
	 $to_user_id=$_GET['to_user_id'];
	 	
$sorgu2="select  eposta from kullanicilar where kayit_no='$to_user_id'";
 $sonuc2=mysqli_query($link, $sorgu2);
$veriler2=mysqli_fetch_assoc($sonuc2);
       $username=$veriler2['eposta'];
	mysqli_set_charset($link,'utf8mb4'); 
	$data = array();
	
	
$sorgu2="select count(arkadaslar.kullanici) as takipci, kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.foto,kullanicilar.cinsiyet,arkadaslar.online,kullanicilar.adi,kullanicilar.soyadi,arkadaslar.dil, 
	arkadaslar.grup,arkadaslar.sahibi,arkadaslar.kullanici from arkadaslar join kullanicilar on arkadaslar.kullanici=kullanicilar.eposta where  arkadaslar.kullanici='$username' and onay=1 order by kullanicilar.adi ";
	
	
		 $sonuc2=mysqli_query($link, $sorgu2);
		 $veriler2=mysqli_fetch_assoc($sonuc2);
	
	    $data[]=$veriler2;
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
