<?php
include 'conexion.php';

$codigo = $_POST['codigo'];
$id_proveedor = $_POST['id_proveedor'];
$nombre = $_POST['nombre'];
$precio = $_POST['precio'];

$sentencia = $conexion->prepare("INSERT INTO producto (codigo, id_proveedor, nombre, precio) VALUES (?, ?, ?, ?)");
if (!$sentencia) {
    die("Error en la preparación de la consulta: " . $conexion->error);
}

$sentencia->bind_param('iisd', $codigo, $id_proveedor, $nombre, $precio);

if ($sentencia->execute()) {
    echo json_encode(["success" => "Producto insertado correctamente"], JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["error" => "Error al insertar producto: " . $sentencia->error], JSON_UNESCAPED_UNICODE);
}

$sentencia->close();
$conexion->close();
?>
