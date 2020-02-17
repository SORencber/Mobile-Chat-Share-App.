<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";

if(  isset($_GET['keyword']) )
{
	
	 $keyword=$_GET['keyword'];
	 $eposta=$_GET['eposta'];

     $keyword=strtolower($keyword);
	mysqli_set_charset($link,'utf8'); 
	$data = array();
	if ($keyword==""){
	$sorgu="select  kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.tarih,kullanicilar.foto,kullanicilar.cinsiyet,kullanicilar.adi,kullanicilar.soyadi
	from kullanicilar  where adi!=''   order by kullanicilar.adi  ";
	
		
	}else{
	$sorgu="select  kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.tarih,kullanicilar.foto,kullanicilar.cinsiyet,kullanicilar.adi,kullanicilar.soyadi
	from kullanicilar  where  adi!='' and kullanicilar.adi LIKE '%$keyword%' or kullanicilar.soyadi LIKE '%$keyword%'  order by kullanicilar.adi";
	}
	
	
 $sonuc=mysqli_query($link, $sorgu);
While($veriler=mysqli_fetch_assoc($sonuc)){
	
	$sql="select kullanici,friend from arkadaslar where kullanici='".$veriler['eposta']."' and friend='$eposta' ";
	$sonuc2=mysqli_query($link,$sql);
	if((mysqli_fetch_assoc($sonuc2) < 1) and ($veriler['eposta']!=$eposta))
	{
				$data[]=$veriler;

	}
}
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);
mysqli_close($link);

}
?>
