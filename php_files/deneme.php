<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";






 $grupsorgu=mysqli_query($link,"select * from arkadaslar where room_id='250'");
	 
	 while ($veriler=mysqli_fetch_array($grupsorgu))
	 {
	    echo $veriler['friend'];
		$sonuc3=mysqli_query($link,"SELECT kayit_no as user_id, adi as name, eposta as email, gcm_registration_id,tarih as created_at FROM kullanicilar   WHERE eposta = '".$veriler['friend']."'");
	
       $result = mysqli_fetch_assoc($sonuc3);
        
	
	 }
?>


