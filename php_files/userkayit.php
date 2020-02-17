<?php
include "baglanti.php";
	mysqli_set_charset($link,'utf8mb4'); 


	if (isset($_POST['txtUsername']) && isset($_POST['txtDil']) && isset($_POST['txtPassword']) && isset($_POST['txtAdi']) && isset($_POST['txtSoyadi']) && isset($_POST['txtCinsiyet']))

	{
		     $keyword=strtolower($keyword);

	          $txtUsername=$_POST['txtUsername'];
		      $txtPassword=$_POST['txtPassword'];
		      $txtAdi=strtolower($_POST['txtAdi']);
		      $txtSoyadi=strtolower($_POST['txtSoyadi']);
		      $txtCinsiyet=$_POST['txtCinsiyet'];
		      $txtDil=$_POST['txtDil'];

	$kontrolsorgu="select eposta from kullanicilar where eposta='$txtUsername'";
	
	 $kontrolcalis=mysqli_query($link, $kontrolsorgu);

	if(mysqli_fetch_array($kontrolcalis)>0){
    
	echo "dolu";
	
	}else {
			mysqli_set_charset($link,'utf8mb4'); 

	 $sorgu="insert into kullanicilar (adi,soyadi,eposta,sifre,cinsiyet,dil) VALUES ('$txtAdi','$txtSoyadi','$txtUsername','$txtPassword','$txtCinsiyet','$txtDil') "; 	

  
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