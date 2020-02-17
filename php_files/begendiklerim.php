<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";


if(  isset($_GET['eposta']))
{
	
	 $username=$_GET['eposta'];
	mysqli_set_charset($link,'utf8'); 
	$data = array();
	$sorgu="

select count(olaylar.resim_no) as yorumsayisi,resimler.resim_no,resimler.resim_adi,resimler.resim_baslik,resimler.yukari,resimler.asagi from olaylar 
JOIN resimler ON olaylar.resim_no=resimler.resim_no where olaylar.kullanici='$username' and oy='1' GROUP BY resim_no
order by olaylar.olayid DESC limit 0,20 ";
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

}
?>
