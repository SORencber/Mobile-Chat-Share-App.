<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


if(  isset($_POST['txtUsername']) && isset($_POST['txtPassword']) )
{	$data = array();

	   
	      $username=$_POST['txtUsername'];

 	$password=$_POST['txtPassword'];
	
	//EĞer facebook kullanici girisi yapmissa 
	if ($password=="yavuzselim65.123.!!!!,???")
	{
		     $sorgu="Select bit,giris,kayit_no,adi,eposta from kullanicilar where eposta='$username'  "; 	

	}else{
     $sorgu="Select bit,giris,kayit_no,adi,eposta from kullanicilar where eposta='$username' and sifre='$password'  "; 	
	}
	$sonuc=mysqli_query($link, $sorgu);
	if (mysqli_num_rows($sonuc) > 0 )
	{
	//mysqli_query($link, "update kullanicilar set gcm_registration_id='$registrationID' where eposta='$username'");

			while($veriler=mysqli_fetch_assoc($sonuc))
	{
		
          if ($veriler['giris']==1)
		  {
		$data[]=$veriler;
		  } else{ 
		  echo "Engelli";
		  }
	}
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
		exit;
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}
	
}
?>
