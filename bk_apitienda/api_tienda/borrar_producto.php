<?php
include 'db_connection.php';

$data = json_decode(file_get_contents("php://input"), true);
$id = $data['id'] ?? '';

if (empty($id)) {
    echo json_encode(["status" => "error", "message" => "ID no proporcionado"]);
    exit();
}

$stmt = $conn->prepare("DELETE FROM productos WHERE id=?");
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    echo json_encode(["status" => "success", "message" => "Producto eliminado correctamente"]);
} else {
    echo json_encode(["status" => "error", "message" => "Error al eliminar el producto"]);
}

$stmt->close();
$conn->close();
?>
