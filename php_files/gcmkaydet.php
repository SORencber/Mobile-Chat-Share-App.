<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";


if(  isset($_POST['eposta']) && isset($_POST['gcm_registration_id']) )
{	$data = array();

	   
	    $eposta=$_POST['eposta'];

 	 $registrationID=$_POST['gcm_registration_id'];
	 
      $sorgu="update kullanicilar set gcm_registration_id='$registrationID' where eposta='$eposta'"; 	
		

	 $sonuc=mysqli_query($link, $sorgu);
	 echo $count=mysqli_affected_rows($link) ;
	if (mysqli_affected_rows($link) >=0 )
	{
	
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}
}
?>