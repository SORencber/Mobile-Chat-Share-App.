<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 




if(  isset($_POST['txtKullanici']) && isset($_POST['txtfriend'])  )
{
	
	$txtKullanici=$_POST['txtKullanici'];
	$txtfriend=$_POST['txtfriend'];
	$sorgu2="Select * from arkadaslar where (kullanici='$txtKullanici' and friend='$txtfriend' and onay='1') OR (onay='1' and kullanici='$txtfriend' and friend='$txtKullanici')";
	$sonuc2=mysqli_query($link,$sorgu2);
	
	if((mysqli_fetch_assoc($sonuc2) < 1) and ($txtKullanici!=$txtfriend))
	{
	
	$sorgu3="Select * from kullanicilar where eposta='$txtfriend'";
	$sonuc3=mysqli_query($link,$sorgu3);
	$veriler=mysqli_fetch_assoc($sonuc3);
	
	echo "0";
	
	
	}
	else{

    $sorgu2="Select * from arkadaslar where kullanici='$txtKullanici' and friend='$txtfriend' and sessiz='0' ";
	$sonuc2=mysqli_query($link,$sorgu2);
	
	if((mysqli_fetch_assoc($sonuc2) < 1))
	{
	 echo "1";
	
	} else{ 

	
	echo "2";
	
	}


	}
}