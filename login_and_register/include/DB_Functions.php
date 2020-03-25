<?php
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $password) {
        $uuid = substr(bin2hex(random_bytes(15)),0, 13);
        $encrypted_password = password_hash($password, PASSWORD_DEFAULT);
        $stmt = $this->conn->prepare("INSERT INTO users(id, username, email, password) VALUES(?, ?, ?, ?)");
        $stmt->bind_param("ssss", $uuid, $name, $email, $encrypted_password);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $stmt->bind_result($col1, $col2, $col3, $col4);
            while ($stmt->fetch()) {
                $user["id"] = $col1;
                $user["username"] = $col2;
                $user["email"] = $col3;
            }
            $stmt->close();
            return $user;
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $stmt->bind_result($col1, $col2, $col3, $col4);
            while ($stmt->fetch()) {
                $user["id"] = $col1;
                $user["name"] = $col2;
                $user["email"] = $col3;
                $user["password"] = $col4;
            }
            $stmt->close();
 
            // verifying user password
            $encrypted_password = $user['password'];
            // check for password equality
            if (password_verify($password,$encrypted_password)) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
    /**
     * Check user is existing or not
     */
    public function isUserExisting($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existing 
            $stmt->close();
            return true;
        } else {
            // user not existing
            $stmt->close();
            return false;
        }
    }
}
?>