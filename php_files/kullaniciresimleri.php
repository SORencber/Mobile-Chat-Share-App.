<?php
include "baglanti.php";

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$kullanici=$_GET["eposta"];
	$sorgu="select  yorumsayisi,resim_no, asagi,resim_baslik,yukari,resim_adi from resimler where kullanici='$kullanici' order by resim_no DESC";
 $sonuc=mysqli_query($link, $sorgu);
	while($veriler=mysqli_fetch_assoc($sonuc))
	{
		
		$data[]=$veriler;
	}
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);

?>