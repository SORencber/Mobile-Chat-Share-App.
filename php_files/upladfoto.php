<?PHP
include "baglanti.php";

	


if(isset($_POST['image']) && isset($_POST['txteposta'])){
     $image = $_POST['image'];
	$data = array();
	   $txteposta = $_POST['txteposta'];
	   	 $sql="select * from kullanicilar where eposta='$txteposta'";
		 $sonuc=mysqli_query($link,$sql);

		 $veriler=mysqli_fetch_assoc($sonuc);
			while($veriler=mysqli_fetch_assoc($sonuc))
	{

		$data[]=$veriler;
	} 
        unlink($data['foto']);

    upload($_POST['image'], $_POST['txteposta']);
    exit;
}
else{
    echo "image_not_in";
    exit;
}
function upload($image,$txteposta){
    //create unique image file name based on micro time and date
    $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
    $upload_folder = "fotolar"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    
    //Cannot use "== true"
	  $sorgu="update kullanicilar SET foto='$path' Where eposta='$txteposta'  "; 	
  $sonuc=mysqli_query($link, $sorgu);

	
    if(file_put_contents($path, base64_decode($image)) != false ){
        echo "uploaded_success";
    }
    else{
        echo "uploaded_failed";
    } 
mysql_close();	
}

?>