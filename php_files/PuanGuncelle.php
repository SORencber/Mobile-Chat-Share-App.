<?php

include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


if(  isset($_POST['txt_to_eposta']) &&  isset($_POST['txtPuan'])  )
{
	
	 $eposta=$_POST['txt_to_eposta'];
		 $puan=$_POST['txtPuan'];

	
		 
	 
mysqli_query($link,"UPDATE kullanicilar 
    SET Harcama ='$puan'    WHERE eposta = '$eposta'
");
echo "success_login";

}	 
?>