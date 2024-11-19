<?php
include 'db_connection.php';

$sql = "SELECT * FROM productos";
$result = $conn->query($sql);

$productos = [];
if ($result->num_rows > 0) {
    while ($row = $result->fetch_assoc()) {
        $productos[] = $row;
    }
}

echo json_encode(["status" => "success", "data" => $productos]);

$conn->close();
?>
