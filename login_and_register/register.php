<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
$body = json_decode(file_get_contents("php://input"));

if ($body!=null) {
    //isset($_POST['username']) && isset($_POST['email']) && isset($_POST['password'])

    // receiving the post params
    $name = $body->username;   //$_POST['username'];
    $email = $body->email;  //$_POST['email'];
    $password = $body->password;  //$_POST['password'];
 
    //check if user is already existed with the same email
    if ($db->isUserExisting($email)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with ".$email;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($name, $email, $password);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["user"]["id"] = $user["id"];
            $response["user"]["username"] = $user["username"];
            $response["user"]["email"] = $user["email"];
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>