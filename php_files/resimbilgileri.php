<?php
include "baglanti.php";

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	$resim_no=$_GET["resimno"];

	 $sorgu="select   kullanicilar.foto,kullanicilar.adi,kullanicilar.soyadi,kullanicilar.eposta,
	 kullanicilar.kayit_no,resimler.yorumsayisi,resimler.resim_no, resimler.asagi,resimler.resim_baslik,resimler.yukari,resimler.resim_adi,resimler.tarih from resimler JOIN kullanicilar ON kullanicilar.eposta=resimler.kullanici where resim_no='$resim_no'";
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