<?php
include "baglanti.php";

if(  isset($_POST['txtUsername']))
{
	
	$username=$_POST['txtUsername'];
$sorgu="Select * from kullanicilar where eposta='$username' "; 	
 $sonuc=mysqli_query($link, $sorgu);
$verilericek=mysqli_fetch_array($sonuc);
$sifre=$verilericek["eposta"];
//mail("selam",,"Şifreniz: $sifre",,)
	$sonuc =mysql_query($sorgu);
	if (mysql_num_rows($sonuc) > 0 )
	{
		mail("KanKa Şifreniz",,"Şifreniz: $sifre",,)

		echo "success_login";
		exit;
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}
}
?>