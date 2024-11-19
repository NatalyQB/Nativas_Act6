<?php
include 'db_connection.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $codigo = htmlspecialchars(trim($_POST['codigo']));
    $nombre = htmlspecialchars(trim($_POST['nombre']));
    $valor = htmlspecialchars(trim($_POST['valor']));


    $stmt = $conn->prepare("INSERT INTO productos (codigo, nombre, valor) VALUES (?,?, ?)");
    $stmt->bind_param("sss", $codigo, $nombre, $valor);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success"]);
    } else {
        echo json_encode(["status" => "error", "message" => $stmt->error]);
    }

    $stmt->close();
}

$conn->close();
?>
