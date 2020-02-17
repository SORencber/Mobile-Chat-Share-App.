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
	$data = array();
//	$kategori=$_GET["kategori"];
	//if($kategori==0 or $kategori==1)
//	{
			mysqli_set_charset($link,'utf8mb4'); 

		$sorgu="select kullanicilar.adi,kullanicilar.kayit_no,kullanicilar.foto,kullanicilar.eposta,resimler.resim_no,resimler.asagi,resimler.resim_baslik,resimler.yukari,resimler.resim_adi,resimler.dil,resimler.tarih from resimler join kullanicilar ON resimler.kullanici=kullanicilar.eposta Where gizlilik=0 ORDER by resimler.resim_no DESC";

	//}else{
	//$sorgu="select kullanicilar.foto,kullanicilar.eposta,resim_no,asagi,resim_baslik,yukari,resim_adi,dil from resimler join kullanicilar ON resimler.kullanici=kullanicilar.eposta Where kategori='$kategori' ORDER BY RAND() limit 0,1";
	
	//}
	$sonuc=mysqli_query($link, $sorgu);
	while($veriler=mysqli_fetch_assoc($sonuc))
	{
	mysqli_set_charset($link,'utf8mb4'); 

		$data[]=$veriler;
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
?>
