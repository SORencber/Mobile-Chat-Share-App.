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
	
	
	$sorgu2="delete from arkadaslar where  friend='$txtKullanici'  OR  kullanici='$txtKullanici' ";
	$sorgu3="delete from resimler where  kullanici='$txtKullanici'  ";
		$sorgu4="delete from olaylar where  kullanici='$txtKullanici'  ";
		$sorgu5="delete from bildirimler where  gondereneposta='$txtKullanici' OR eposta='$txtKullanici'  ";
		$sorgu6="delete from kullanicilar where   eposta='$txtKullanici'  ";

	mysqli_query($link,$sorgu2);mysqli_query($link,$sorgu3);mysqli_query($link,$sorgu4);mysqli_query($link,$sorgu5);
	mysqli_query($link,$sorgu6);
	$Sqkaydet="insert into SilinenKullanicilar (Silen,kullanici) VALUES ('$txtSilen','$txtKullanici')";
	$sonuc2=mysqli_query($link,$Sqkaydet);

	if((mysqli_fetch_assoc($sonuc2) < 1) and ($txtKullanici!=$txtSilen))
	{
	
	
	
	echo "0";
	
	
	}
	else{

   
	
	echo "1";
	
	}


	
}