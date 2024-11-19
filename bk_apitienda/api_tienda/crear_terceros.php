<?php
include 'db_connection.php';

$data = json_decode(file_get_contents("php://input"), true);
$nombre = $data['nombre'] ?? '';
$apellido = $data['apellido'] ?? '';
$identificacion = $data['identificacion'] ?? '';
$telefono = $data['telefono'] ?? '';
$correo = $data['correo'] ?? '';

if (empty($nombre) || empty($apellido) || empty($identificacion) || empty($telefono) || empty($correo)) {
    echo json_encode(["status" => "error", "message" => "Datos incompletos"]);
    exit();
}

$stmt = $conn->prepare("INSERT INTO terceros (nombre, apellido, identificacion, telefono, correo) VALUES (?, ?, ?, ?, ?)");
$stmt->bind_param("sssss", $nombre, $apellido, $identificacion, $telefono, $correo);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Tercero creado correctamente"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error al crear el tercero"]);
}

$stmt->close();
$conn->close();
?>
