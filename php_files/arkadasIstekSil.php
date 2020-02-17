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



if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend']) && isset($_POST['txtb_no'])  )
{
	
	
	
	
	$txtKullanici=$_POST['txtKullanici'];
	$txtfriend=$_POST['txtfriend'];
		$b_no=$_POST['txtb_no'];

	
    
	
	
	        
 $sql="delete from arkadaslar where kullanici='$txtKullanici' and friend='$txtfriend' OR friend='$txtKullanici' and kullanici='$txtfriend'";
 
mysqli_query($link,$sql);
$sql2="delete from bildirimler where b_no='$b_no'";
 mysqli_query($link,$sql2);
 




echo "1";




	
	


	 
}