<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Juego de la Frase Interminable</title>
<style>
body {
font-family: Arial, sans-serif;
background-color: #f4f4f4;
padding: 20px;
}
h1 {
text-align: center;
color: #333;
}
</style>
</head>
<body>
<h1>Juego de la Frase Interminable</h1>

<?php

class Jugador {
    public $nombre;
    public $teclaAceptacion;
    public $teclaRechazo;
    public $puntuacion;
    public $tiempo;

    function __construct($nombre, $teclaAceptacion, $teclaRechazo) {
        $this->nombre = $nombre;
        $this->teclaAceptacion = $teclaAceptacion;
        $this->teclaRechazo = $teclaRechazo;
        $this->puntuacion = 0;
        $this->tiempo = 0;
    }

    function __toString() {
        return "Nombre: " . $this->nombre . ", Tecla de Aceptación: " . $this->teclaAceptacion . ", Tecla de Rechazo: " . $this->teclaRechazo;
    }

    function sumarTiempo($tiempo) {
        $this->tiempo += $tiempo;
    }

    function puntuar($puntos) {
        $this->puntuacion += $puntos;
    }
}

class Juego {
    private $jugadores;
    private $jugadoresSobrantes;
    private $frase;

    function __construct() {
        $this->jugadores = [];
        $this->jugadoresSobrantes = [];
        $this->frase = [];
    }

    function agregarJugador($jugador) {
        $this->jugadores[] = $jugador;
        return true;
    }

    function elegirOrden() {
        shuffle($this->Jugadores);
        $this->jugadoresSobrantes = $this->jugadores;
    }

    function imprimirJugadores() {
        $mensaje = "";
        foreach($this->jugadores as $index => $jugador) {
            $mensaje .= ($index + 1) . ". " . $jugador . "\n";
        }
        echo "<script>alert('$mensaje');</script>";
    }

    function iniciarJuego() {
        echo "<script>alert('El juego ha comenzado!');</script>";
        $this->elegirOrden();
        $mensaje = "Orden de los jugadores:\n";
        foreach($this->jugadores as $index => $jugador) {
            $mensaje .= ($index + 1) . ". " . $jugador . "\n";
        }
        echo "<script>alert('$mensaje');</script>";

        while(count($this->jugadoresSobrantes) > 1) {
            foreach($this->jugadoresSobrantes as $index => $jugador) {
                echo "<script>alert('Es el turno de {$jugador->nombre}');</script>";
                echo "<script>alert('Frase actual: " . implode(" ", $this->frase) . "');</script>";

                $startTime = microtime(true);
                $input = $this->prompt("Ingrese la frase:");
                $endTime = microtime(true);

                $timeTaken = $endTime - $startTime;
                $jugador->sumarTiempo($timeTaken);

                $palabras = explode(" ", $input);
                $error = $this->comprobarError($palabras, $index);

                foreach($this->jugadoresSobrantes as $j => $calificador) {
                    if ($j != $index) {
                        $decision = "";
                        do {
                            $decision = $this->prompt("{$calificador->nombre} está calificando la frase de {$jugador->nombre}\n"
                                    . "Tecla de aceptación: {$calificador->teclaAceptacion}, Tecla de rechazo: {$calificador->teclaRechazo}\n"
                                    . "¿La frase es correcta? ({$calificador->teclaAceptacion}/{$calificador->teclaRechazo}): ");
                            if ($decision != $calificador->teclaAceptacion && $decision != $calificador->teclaRechazo) {
                                echo "<script>alert('Entrada inválida. Por favor, ingrese solo la tecla de aceptación o rechazo.');</script>";
                            }
                        } while ($decision != $calificador->teclaAceptacion && $decision != $calificador->teclaRechazo);
                        $calificacion = $decision == $calificador->teclaAceptacion;
                        $this->calificar($j, $error, $calificacion);
                    }
                }

                if ($error) {
                    echo "<script>alert('La frase fue rechazada. {$jugador->nombre} ha sido eliminado.');</script>";
                    array_splice($this->jugadoresSobrantes, $index, 1);
                    $index--;
                } else {
                    echo "<script>alert('La frase fue aceptada.');</script>";
                }

                $sb = "Puntajes y tiempos actuales:\n";
                usort($this->jugadoresSobrantes, function($j1, $j2) {
                    return $j2->puntuacion - $j1->puntuacion;
                });
                foreach($this->jugadoresSobrantes as $j) {
                    $sb .= "{$j->nombre} - Puntaje: {$j->puntuacion}, Tiempo: {$j->tiempo}\n";
                }
                echo "<script>alert('$sb');</script>";
            }

            $this->eliminarJugadorConMenorPuntaje();
        }

        echo "<script>alert('El juego ha terminado!');</script>";
        $sb = "Resultados finales:\n";
        usort($eliminados, function($j1, $j2) {
            return $j2->puntuacion - $j1->puntuacion;
        });
        foreach($this->jugadoresSobrantes as $j) {
            $sb .= "{$j->nombre} - Puntaje: {$j->puntuacion}, Tiempo: {$j->tiempo}\n";
        }
        echo "<script>alert('$sb');</script>";

        if (count($this->jugadoresSobrantes) > 0) {
            $ganador = $this->jugadoresSobrantes[[1]](https://www.codeconvert.ai/javascript-to-php-converter?utm_source=textcortex&utm_medium=zenochat);
            echo "<script>alert('El ganador es: {$ganador->nombre} con un puntaje de {$ganador->puntuacion}');</script>";
        }
    }

    function eliminarJugadorConMenorPuntaje() {
        if (count($this->jugadoresSobrantes) == 0) return;

        $menorPuntaje = min(array_map(function($j) {
            return $j->puntuacion;
        }, $this->jugadoresSobrantes));
        $id = array_search($menorPuntaje, array_map(function($j) {
            return $j->puntuacion;
        }, $this->jugadoresSobrantes));

        if ($id !== false) {
            echo "<script>alert('Se eliminó al jugador: {$this->jugadoresSobrantes[$id]->nombre} por tener el menor puntaje...');</script>";
            array_splice($this->jugadoresSobrantes, $id, 1);
        }
    }

    function calificar($i, $error, $calificacion) {
        if ($error) {
            $this->jugadoresSobrantes[$i]->puntuar($calificacion ? -0.5 : 0.5);
        } else {
            $this->jugadoresSobrantes[$i]->puntuar($calificacion ? 0.5 : -0.5);
        }
    }

    function comprobarError($frase, $jugador) {
        if (count($frase) - count($this->frase) > 1 || count($frase) < count($this->frase) || count($frase) == count($this->frase)) {
            return true;
        } else {
            if (count($this->frase) == 0) {
                $ultimaPalabra = end($frase);
                $this->frase[] = $ultimaPalabra;
                $this->jugadoresSobrantes[$jugador]->puntuar(1);
                return false;
            } else {
                $equivocaciones = 0;
                foreach($this->frase as $i => $palabra) {
                    if (strcasecmp($frase[$i], $palabra) !== 0) {
                        $equivocaciones++;
                    }
                }
                if ($equivocaciones > 0) {
                    return true;
                } else {
                    $this->frase[] = end($frase);
                    $this->jugadoresSobrantes[$jugador]->puntuar(1);
                    return false;
                }
            }
        }
    }

    private function prompt($message) {
        return readline("{$message}: ");
    }
}

class MenuJuego {
    private static $juego;

    static function iniciar($juego) {
        self::$juego = $juego;
        self::menuPrincipal();
    }

    static function registrarJugador() {
        $nombre = self::prompt("Nombre del jugador:");
        while (empty(trim($nombre))) {
            echo "<script>alert('El nombre no puede estar vacío. Inténtelo de nuevo.');</script>";
            $nombre = self::prompt("Nombre del jugador:");
        }

        $teclaA = self::prompt("Tecla de aceptación (ej. A):");
        while (empty(trim($teclaA))) {
            echo "<script>alert('La tecla de aceptación no puede estar vacía. Inténtelo de nuevo.');</script>";
            $teclaA = self::prompt("Tecla de aceptación:");
        }

        $teclaR = self::prompt("Tecla de rechazo (ej. R):");
        while (empty(trim($teclaR))) {
            echo "<script>alert('La tecla de rechazo no puede estar vacía. Inténtelo de nuevo.');</script>";
            $teclaR = self::prompt("Tecla de rechazo:");
        }

        if (self::$juego->agregarJugador(new Jugador($nombre, $teclaA, $teclaR))) {
            echo "<script>alert('Jugador registrado con éxito!!');</script>";
        } else {
            echo "<script>alert('No se pudo registrar el jugador');</script>";
        }
    }

    static function editarJugador($op) {
        if ($op < count(self::$juego->jugadores) && $op >= 0) {
            $nombre = self::prompt("Nombre del jugador:");
            while (empty(trim($nombre))) {
                echo "<script>alert('El nombre no puede estar vacío. Inténtelo de nuevo.');</script>";
                $nombre = self::prompt("Nombre del jugador:");
            }

            $teclaA = self::prompt("Tecla de aceptación:");
            while (empty(trim($teclaA))) {
                echo "<script>alert('La tecla de aceptación no puede estar vacía. Inténtelo de nuevo.');</script>";
                $teclaA = self::prompt("Tecla de aceptación:");
            }

            $teclaR = self::prompt("Tecla de rechazo:");
            while (empty(trim($teclaR))) {
                echo "<script>alert('La tecla de rechazo no puede estar vacía. Inténtelo de nuevo.');</script>";
                $teclaR = self::prompt("Tecla de rechazo:");
            }

            self::$juego->jugadores[$op]->nombre = $nombre;
            self::$juego->jugadores[$op]->teclaAceptacion = $teclaA;
            self::$juego->jugadores[$op]->teclaRechazo = $teclaR;
            echo "<script>alert('Jugador actualizado con éxito!!');</script>";
            self::$juego->imprimirJugadores();
        } else {
            echo "<script>alert('No se puede editar jugador fuera de rango');</script>";
        }
    }

    static function eliminarJugador($op) {
        if ($op < count(self::$juego->jugadores) && $op >= 0) {
            array_splice(self::$juego->jugadores, $op, 1);
            echo "<script>alert('Jugador eliminado con éxito!!');</script>";
        } else {
            echo "<script>alert('No se puede eliminar jugador fuera de rango');</script>";
        }
    }

    static function menuPrincipal() {
        $opcion = 0;
        do {
            $input = self::prompt(
                "**************************************************\n" .
                "JUEGO DE LA FRASE INTERMINABLE\n" .
                "**************************************************\n" .
                "1.- Jugadores\n" .
                "2.- Iniciar nuevo juego\n" .
                "3.- Salir\n" .
                "Por favor, seleccione una opción (1-3):"
            );
            $opcion = intval($input);
            switch ($opcion) {
                case 1:
                    self::menuRegistro();
                    break;
                case 2:
                    self::verificarJugadores();
                    break;
                case 3:
                    echo "<script>alert('Hasta pronto!!');</script>";
                    break;
                default:
                    echo "<script>alert('Opción no válida. Por favor, seleccione una opción (1-3).');</script>";
                    break;
            }
        } while ($opcion !== 3);
    }

    static function verificarJugadores() {
        if (count(self::$juego->jugadores) > 1) {
            self::$juego->iniciarJuego();
        } else {
            echo "<script>alert('No hay suficiente cantidad de jugadores para comenzar el juego');</script>";
        }
    }

    static function menuRegistro() {
        $opcion = 0;
        do {
            $input = self::prompt(
                "*******************************************\n" .
                "MENU REGISTRO JUGADORES\n" .
                "*******************************************\n" .
                "1.- Registrar jugador\n" .
                "2.- Jugadores registrados\n" .
                "3.- Editar jugador\n" .
                "4.- Eliminar jugador\n" .
                "5.- Salir\n" .
                "Por favor, seleccione una opción (1-5):"
            );
            $opcion = intval($input);
            switch ($opcion) {
                case 1:
                    self::registrarJugador();
                    break;
                case 2:
                    if (count(self::$juego->jugadores) == 0) {
                        echo "<script>alert('No hay jugadores registrados.');</script>";
                    } else {
                        self::$juego->imprimirJugadores();
                    }
                    break;
                case 3:
                    if (count(self::$juego->jugadores) == 0) {
                        echo "<script>alert('No hay jugadores registrados.');</script>";
                    } else {
                        self::$juego->imprimirJugadores();
                        $input = self::prompt("Seleccione jugador a editar (1-" . count(self::$juego->jugadores) . "):");
                        $op = intval($input);
                        self::editarJugador($op - 1);
                    }
                    break;
                case 4:
                    if (count(self::$juego->jugadores) == 0) {
                        echo "<script>alert('No hay jugadores registrados.');</script>";
                    } else {
                        self::$juego->imprimirJugadores();
                        $input = self::prompt("Seleccione jugador a eliminar (1-" . count(self::$juego->jugadores) . "):");
                        $op = intval($input);
                        self::eliminarJugador($op - 1);
                    }
                    break;
                case 5:
                    return;
                default:
                    echo "<script>alert('Opción no válida. Por favor, seleccione una opción (1-5).');</script>";
                    break;
            }
        } while ($opcion !== 5);
    }

    private static function prompt($message) {
        return readline("{$message}: ");
    }
}

$juego = new Juego();
MenuJuego::iniciar($juego);

?>
</body>
</html>

