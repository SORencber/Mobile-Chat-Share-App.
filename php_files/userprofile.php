<?php
include "baglanti.php";


if(  isset($_GET['to_user_id']) )
{
	
	 $to_user_id=$_GET['to_user_id'];
	$sorgu2="select  eposta from kullanicilar where kayit_no='$to_user_id'";
 $sonuc2=mysqli_query($link, $sorgu2);
$veriler2=mysqli_fetch_assoc($sonuc2);
       $eposta=$veriler2['eposta'];
	
	
	
	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="select  SUM(resimler.yukari) AS toplamyukari, SUM(resimler.asagi) as toplamasagi from resimler where kullanici='$eposta' GROUP BY kullanici ";
	
	
	
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
