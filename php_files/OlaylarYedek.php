<?php

include "baglanti.php";
	mysqli_set_charset($link,'utf8'); 


if(  isset($_POST['txtpid']) &&  isset($_POST['txtoy']) && isset($_POST['txt_to_eposta'])&& isset($_POST['txtyorum']) && isset($_POST['txtDil']) )
{
	
	 $resim_no=$_POST['txtpid'];
		 $kullanici=$_POST['txtkullanici'];

	 $oy=$_POST['txtoy'];
	 $yorum=$_POST['txtyorum'];
	 $dil=$_POST['txtDil'];
	 	 $oylananeposta=$_POST['txt_to_eposta'];


	 if($oy==1){

	 mysqli_query($link,"
    UPDATE resimler 
    SET yukari = yukari + 1 
    WHERE resim_no = '$resim_no'
");
mysqli_query($link,"
    UPDATE kullanicilar 
    SET Puan = Puan + 5
    WHERE eposta = '$oylananeposta'
"); 
		 
		 
	 }else if ($oy==2){
		 
mysqli_query($link,"
    UPDATE resimler 
    SET asagi = asagi + 1
    WHERE resim_no = '$resim_no'
");
		 
	 }else{
		 
mysqli_query($link,"
    UPDATE resimler 
    SET notr = notr + 1
    WHERE resim_no = '$resim_no'
");
	 
	 }

	mysqli_query($link,"
    UPDATE kullanicilar 
    SET Puan = Puan + 1
    WHERE eposta = '$kullanici'
"); 
	 if($yorum=="")
	 { $sorgu="insert into olaylar (resim_no,kullanici,oy) VALUES ('$resim_no','$kullanici','$oy') ";}
    else{
		mysqli_query($link,"
    UPDATE resimler 
    SET yorumsayisi = yorumsayisi + 1
    WHERE resim_no = '$resim_no'
");
	
	  $sorgu="insert into olaylar (resim_no,kullanici,oy,yorum,dil,okundumu) VALUES ('$resim_no','$kullanici','$oy','$yorum','$dil','0') ";
	  
	 }
 $sonuc=mysqli_query($link, $sorgu);
	if ($sonuc > 0 )
	{
		echo "success_login";
		exit;
	}else {
		
		echo "Wrong Username and Password";
	    exit;
	}
}
?>
