<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include "baglanti.php";
	mysqli_set_charset($link,'utf8'); 

if(  isset($_POST['txtresim_no']) &&  isset($_POST['txtthumbnail']))
{
	
	$resim_no=$_POST['txtresim_no'];
    $thumbnail=$_POST['txtthumbnail'];
  
     unlink($_POST['txtthumbnail']);
   

    $sorgu="delete from resimler where resim_no='$resim_no' "; 	
    mysqli_query ($link,"delete from olaylar where resim_no='$resim_no' ");
	    mysqli_query ($link,"delete from bildirimler  where resim_no='$resim_no' ");

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