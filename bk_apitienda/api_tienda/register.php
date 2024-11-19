<?php
include 'db_connection.php';

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $nombre = htmlspecialchars(trim($_POST['nombre']));
    $apellido = htmlspecialchars(trim($_POST['apellido']));
    $correo = htmlspecialchars(trim($_POST['correo']));
    $clave = password_hash(trim($_POST['clave']), PASSWORD_BCRYPT);
    $identificacion = htmlspecialchars(trim($_POST['identificacion']));

    $stmt = $conn->prepare("INSERT INTO usuarios (nombre, apellido, correo, clave,identificacion) VALUES (?,?, ?, ?, ?)");
    $stmt->bind_param("sssss", $nombre, $apellido, $correo, $clave,$identificacion);

    if ($stmt->execute()) {
        echo json_encode(["status" => "success"]);
    } else {
        echo json_encode(["status" => "error", "message" => $stmt->error]);
    }

    $stmt->close();
}

$conn->close();
?>
