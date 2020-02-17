<?PHP
include "baglanti.php";
mysqli_set_charset($link,'utf8mb4'); 


if(isset($_POST['image']) && isset($_POST['txteposta'])){
     $image = $_POST['image'];
	
	   $txteposta = $_POST['txteposta'];
	    $txtUlke = $_POST['txtUlke'];
	   	   $txtSehir = $_POST['txtSehir'];
	   	   $txtIlce = $_POST['txtIlce'];
	   	   $txtadres = $_POST['txtadres'];
    upload($_POST['image'], $_POST['txteposta'],$link,$txtUlke,$txtSehir,$txtIlce,$txtadres);
    exit;
}
else{
    echo "image_not_in";
    exit;
}
function upload($image,$txteposta,$link,$txtUlke,$txtSehir,$txtIlce,$txtadres){
    //create unique image file name based on micro time and date
    $now = DateTime::createFromFormat('U.u', microtime(true));
    $id = $now->format('YmdHisu');
    
    $upload_folder = "fotolar"; //DO NOT put url (http://example.com/upload)
    $path = "$upload_folder/$id.jpeg";
    
    //Cannot use "== true"
	  $sorgu="update kullanicilar SET foto='$path',Ulke='$txtUlke',Sehir='$txtSehir',ilce='$txtIlce',adres='$txtadres' Where eposta='$txteposta'  "; 	
 $sonuc=mysqli_query($link, $sorgu);
	
    if(file_put_contents($path, base64_decode($image)) != false ){
        echo "uploaded_success";
    }
    else{
        echo "uploaded_failed";
    } 
mysqli_close($link);	
}

?>