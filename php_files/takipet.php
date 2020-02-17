<?php

include "baglanti.php";
mysqli_set_charset($link,'utf8'); 


if(  isset($_POST['txtkullanici']) &&  isset($_POST['txtfriend']) && isset($_POST['txtDil'])  )
{
	
		 $kullanici=$_POST['txtkullanici'];
		 $dil=$_POST['txtDil'];

	 $friend=$_POST['txtfriend'];

	$test="select * from arkadaslar where kullanici='$kullanici' and friend='$friend'";
   $testsonuc=mysqli_query($link,$test);
if ($testsonuc>0)
{
	echo "daha önce var";
	
}else {

	  $sorgu="insert into arkadaslar (kullanici,friend,dil) VALUES ('$kullanici','$friend','$dil') ";
	
	 $sonuc=mysqli_query($link, $sorgu);
	if ($sonuc > 0 )
	{
		echo "success_login";
		exit;
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}
}
}
?>
<form action="Olaylar.php" method="POST">

id:<input type="text" name="txtpid">
kullanici:<input type="text" name="txtkullanici">
oy :<input type="text" name="txtoy">

kull:<input type="text" name="txtyorum">

<input type="submit" value=" Gir ">
</form>