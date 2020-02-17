<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include "baglanti.php";
	mysqli_set_charset($link,'utf8'); 

if(  isset($_POST['txteposta']) &&  isset($_POST['txtresim_no']) &&  isset($_POST['txtsessiz']))
{
	
	$resim_no=$_POST['txtresim_no'];
    $eposta=$_POST['txteposta'];
      $sessiz=$_POST['txtsessiz'];

   

    $sorgu="UPDATE olaylar SET sessiz='$sessiz' where kullanici='$eposta' and resim_no='$resim_no' "; 	
    
 $sonuc=mysqli_query($link, $sorgu);
 if ($sonuc > 0 )
	{
		echo "success_login";
		exit;
	}else {
		
		echo "Olmadı";
	    exit;
	}
	mysqli_close($link);
}
?>