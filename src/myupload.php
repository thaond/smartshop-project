<?php
function getFileExtension($fileName)
{
   $parts=explode(".",$fileName);
   return $parts[count($parts)-1];
}

$base_dir = "C:/uploads/";

$username = $_GET['username'];
if (!is_dir($base_dir . $username)){
	mkdir($base_dir . $username, 0755);
}

$upload_image = $base_dir . $username . '/'. basename( $_FILES['uploadedfile']['name']);

$is_valid_type = false;
$arr_valid_type = array('png', 'jpe', 'jpeg', 'jpg', 'gif', 'bmp', 'ico', 'tiff', 'tif', 'svg', 'svgz');
$file_extension = getFileExtension($_FILES['uploadedfile']['name']);
foreach($arr_valid_type as $type){
	if ($file_extension == $type){
		$is_valid_type = true;
		break;
	}
}

if (!$is_valid_type){
	//The uploaded file isn't image
	echo "1:Tập tin tải lên không phải là hình ảnh";
} else if ($_FILES['uploadedfile']['size'] > 3000000){
	//The uploaded file must be less than 3MB
	echo "1:File tải lên phải nhỏ hơn 3MB";
} else if(move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $upload_image)) {
	// Get new sizes
	list($width, $height) = getimagesize($upload_image);
	$newwidth = 100; // This can be a set value or a percentage of original size ($width)
	$newheight = 75; // This can be a set value or a percentage of original size ($height)
	
	$thumbnail = $base_dir . $username . '/'. basename( $_FILES['uploadedfile']['name'] , '.'.$file_extension )."_thumbnail.jpg"; // Set the thumbnail name
	// Load the images
	$thumb = imagecreatetruecolor($newwidth, $newheight);
	$source = imagecreatefromjpeg($upload_image);
	
	// Resize the $thumb image.
	imagecopyresized($thumb, $source, 0, 0, 0, 0, $newwidth, $newheight, $width, $height);
	
	// Save the new file to the location specified by $thumbnail
	imagejpeg($thumb, $thumbnail, 100);
	echo "0:" . $upload_image;
} else{
	echo "1:Có lỗi trong quá trình tải file";
}
?>
