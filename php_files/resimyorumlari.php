<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
include "baglanti.php";
	mysqli_set_charset($link,'utf8mb4'); 

if(  isset($_GET['resim_no']) )
{
	
	 $resim_no=$_GET['resim_no'];
	mysqli_set_charset($link,'utf8mb4'); 
	$data = array();
	$sorgu="select olaylar.olayid,olaylar.oy, olaylar.dil,olaylar.yorum,olaylar.kullanici,kullanicilar.adi,kullanicilar.foto from olaylar 
JOIN kullanicilar ON olaylar.kullanici=kullanicilar.eposta where olaylar.resim_no='$resim_no'
 and yorum!='' order by olaylar.olayid ASC";
  $sonuc=mysqli_query($link,$sorgu);
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
