<?php
include 'conexion.php';

$login=$_POST['usuario'];
$password=$_POST['password'];

// $login = "asmith";
// $password = "mypassword";

$sentencia = $conexion->prepare("SELECT * FROM user WHERE login=? AND password=?");
if (!$sentencia) {
    die("Error en la preparación de la consulta: " . $conexion->error);
}

$sentencia->bind_param('ss', $login, $password);
$sentencia->execute();

$resultado = $sentencia->get_result();
if ($fila = $resultado->fetch_assoc()) {
    echo json_encode($fila, JSON_UNESCAPED_UNICODE);
} else {
    echo json_encode(["error" => "Usuario o contraseña incorrectos"], JSON_UNESCAPED_UNICODE);
}

$sentencia->close();
$conexion->close();
?>
