<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";

if(  isset($_GET['eposta']) )
{
	
	// $keyword=$_GET['keyword'];
	 $eposta=$_GET['eposta'];

     //$keyword=strtolower($keyword);
	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="select  * from kullanicilar where eposta='$eposta'";
	
		
	
	
	
 $sonuc=mysqli_query($link, $sorgu);
While($veriler=mysqli_fetch_assoc($sonuc)){
	

				$data[]=$veriler;

	
}
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);
mysqli_close($link);
}
?>