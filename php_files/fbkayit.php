<?php
include "baglanti.php";
	mysqli_set_charset($link,'utf8mb4'); 


	if (isset($_POST['txtUsername']) && isset($_POST['txtDil']) && isset($_POST['txtfb']) && isset($_POST['txtAdi']) && isset($_POST['txtSoyadi']) && isset($_POST['txtCinsiyet']))

	{	mysqli_set_charset($link,'utf8mb4'); 

		     $keyword=strtolower($keyword);

	          $txtUsername=$_POST['txtUsername'];
		      $txtfb=$_POST['txtfb'];
		      $txtAdi=strtolower($_POST['txtAdi']);
		      $txtSoyadi=strtolower($_POST['txtSoyadi']);
		      $txtCinsiyet=$_POST['txtCinsiyet'];
		      $txtDil=$_POST['txtDil'];

	$kontrolsorgu="select eposta from kullanicilar where eposta='$txtUsername' and fb='1'";
	
	 $kontrolcalis=mysqli_query($link, $kontrolsorgu);

	if(mysqli_fetch_array($kontrolcalis)>0){
    
	echo "dolu";
	
	}else {
			mysqli_set_charset($link,'utf8mb4'); 

	 $sorgu="insert into kullanicilar (adi,soyadi,eposta,fb,cinsiyet,dil) VALUES ('$txtAdi','$txtSoyadi','$txtUsername','$txtfb','$txtCinsiyet','$txtDil') "; 	
 $sonuc=mysqli_query($link, $sorgu);
	if ($sonuc > 0 )
	{
		echo "oldu";
		exit;
	}else {
		
		echo "olmadi";
	    exit;
	}
			}
	}
?>