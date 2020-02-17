<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include "baglanti.php";
	mysqli_set_charset($link,'utf8'); 

if(  isset($_POST['olayid']) )
{
	
	$olayid=$_POST['olayid'];
  
   

    $sorgu="delete from olaylar where olayid='$olayid' "; 	
    //mysqli_query ($link,"delete from olaylar where resim_no='$resim_no' ");
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