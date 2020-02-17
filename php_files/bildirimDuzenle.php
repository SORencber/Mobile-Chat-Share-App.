<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 




if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend'])  )
{$txtKullanici=$_POST['txtKullanici'];
	$txtfriend=$_POST['txtfriend'];
		$sessiz=$_POST['sessiz'];

 	$sorgu2="UPDATE arkadaslar SET sessiz=$sessiz where kullanici='$txtKullanici' and friend='$txtfriend' and onay=1";
	$sonuc2=mysqli_query($link,$sorgu2);


	
}