<?php
include "baglanti.php";


if(  isset($_GET['eposta']) )
{
	
	 $username=$_GET['eposta'];

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="select  Harcama from kullanicilar where eposta='$username'";
	
	
	
 $sonuc=mysqli_query($link, $sorgu);
$veriler=mysqli_fetch_assoc($sonuc);
	
		$data[]=$veriler;
	
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);

}
?>
