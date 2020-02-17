<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 




if(  isset($_POST['txtKullanici']) && isset($_POST['txtSilen'])  )
{
	
	$txtKullanici=$_POST['txtKullanici'];
	$txtSilen=$_POST['txtSilen'];
	
	

	$Sqkaydet="update kullanicilar SET giris='1' where eposta='$txtKullanici' ";
	$sonuc2=mysqli_query($link,$Sqkaydet);

	if((mysqli_fetch_assoc($sonuc2) < 1) and ($txtKullanici!=$txtSilen))
	{
	
	
	
	echo "0";
	
	
	}
	else{

   
	
	echo "1";
	
	}


	
}