<?php
include "baglanti.php";


	

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="SELECT (
SUM( resimler.yukari ) - SUM( resimler.asagi )
) AS sonuc, kullanicilar.cinsiyet, kullanicilar.soyadi, kullanicilar.adi, kullanicilar.foto, kullanicilar.eposta, kullanicilar.kayit_no
FROM resimler
JOIN kullanicilar ON resimler.kullanici = kullanicilar.eposta
GROUP BY resimler.kullanici
ORDER BY sonuc DESC 
LIMIT 0 , 100
";
	
	
 $sonuc=mysqli_query($link, $sorgu);
 while($veriler=mysqli_fetch_assoc($sonuc)){
	
		$data[]=$veriler;
	 }
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);


?>
