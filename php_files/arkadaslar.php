<?php
include "baglanti.php";


if(  isset($_GET['eposta']) )
{
	
	 $username=$_GET['eposta'];
	 $room_id=$_GET['room_id'];

	mysqli_set_charset($link,'utf8'); 
	$data = array();
	
	$sorgu="select  arkadaslar.room_id as no,arkadaslar.tarih,kullanicilar.kayit_no,kullanicilar.eposta,kullanicilar.foto,kullanicilar.cinsiyet,arkadaslar.online,kullanicilar.adi,kullanicilar.soyadi,arkadaslar.dil, 
	arkadaslar.grup,arkadaslar.sahibi,arkadaslar.kullanici from arkadaslar join kullanicilar on arkadaslar.kullanici=kullanicilar.eposta where  arkadaslar.friend='$username' and onay=1 order by arkadaslar.son_yazi_tarihi DESC ";
	
		
	
	
 $sonuc=mysqli_query($link, $sorgu);
While($veriler=mysqli_fetch_assoc($sonuc)){
	
		$data[]=$veriler;
}
	$degistir="";
	$ara='\\';
	echo $cleandata = str_replace($ara,$degistir, json_encode($data, JSON_UNESCAPED_UNICODE));
	//json_encode($data,JSON_UNESCAPED_UNICODE);
	//echo json_encode(json_encode($data, JSON_UNESCAPED_UNICODE),JSON_UNESCAPED_SLASHES);

}
?>
