<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";

if(  isset($_GET['eposta']) )
{
	
	 $eposta=$_GET['eposta'];

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sallaguncelle="UPDATE kullanicilar SET salla='1' where eposta='$eposta'";
	 mysqli_query($link, $sallaguncelle);

	$sorgu="select  kullanicilar.latitude,kullanicilar.longitude,kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.tarih,kullanicilar.foto,kullanicilar.cinsiyet,kullanicilar.adi,kullanicilar.soyadi
	from kullanicilar  where  kullanicilar.adi!=''  ORDER BY RAND() limit 0,1";
	
	
	
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