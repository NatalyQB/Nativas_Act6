<?php
include 'db_connection.php';

$data = json_decode(file_get_contents("php://input"), true);
$id = $data['id'] ?? '';
$codigo = $data['codigo'] ?? '';
$nombre = $data['nombre'] ?? '';
$precio = $data['precio'] ?? '';


if (empty($id) || empty($codigo) || empty($nombre) || empty($precio) ) {
    echo json_encode(["status" => "error", "message" => "Datos incompletos"]);
    exit();
}

$stmt = $conn->prepare("UPDATE productos SET codigo=?, nombre=?, precio=? WHERE id=?");
$stmt->bind_param("sssi", $codigo, $nombre, $precio, $id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Producto actualizado correctamente"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error al actualizar el producto"]);
}

$stmt->close();
$conn->close();
?>
