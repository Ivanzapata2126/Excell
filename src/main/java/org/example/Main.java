package org.example;


import java.text.DecimalFormat;
import java.util.*;


public class Main {

    // Se instancia el scanner
    static Scanner sn = new Scanner(System.in);

    // Se crea un array de double para almacenar las areas
    public static double[] listaAreas;

    public static Double[] listaAreasOrdenadas;




    // Se crea un array de int para almacenar las posiciones
    public static int[]  posiciones = new int[7];


    public static void main(String[] args) {
        System.out.println("Bienvenido querido profesor");
        boolean salir = false;
        int option; //Guardaremos la opción del usuario

        // Se mostrará el menu siempre y cuando el usuario no le de en salir que es la opcion 6
        while (salir == false) {
            System.out.print("");
            System.out.println("-------------------- Menu ------------------- ");
            System.out.println("1.Leer archivo excel");
            System.out.println("2. Salir");
            System.out.print("Ingrese una opción: ");
            //Se hace dentro de un try-Catch para recibir si la persona ingresó un valor distinto al esperado
            try {
                System.out.print("Ingrese una opción: ");
                option = sn.nextInt();
                //Switch case, para facilitar la election de la persona, recibe la elección,
                // tomada anteriormente en el menú y las notas, para pasarlas a los
                // métodos mencionados anteriormente
                switch (option) {
                    case 1:
                        Archivo.leerArchivoConsola();
                        listaAreas = Archivo.triangulos;
                        DecimalFormat df = new DecimalFormat("#.##");
                        posiciones = Archivo.posiciones;
                        listaAreasOrdenadas = new Double[listaAreas.length];
                        for (int i = 0; i < listaAreas.length; i++) {
                            listaAreasOrdenadas[i] = listaAreas[i];
                        }
                        Archivo.escribirArchivo();
                        break;
                    case 2:
                        salir = true;
                        break;
                    default:
                        System.out.println("Solo números entre 1 y 2");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número");
                sn.nextLine();
            }
        }
    }

    public static String[][] obtenerAreasMayores() {
        double mayor = 0;
        int contador = 0;

        // Contar cuántos triángulos tienen el área mayor
        for (int i = 0; i < listaAreas.length; i++) {
            if (listaAreas[i] > mayor) {
                mayor = listaAreas[i];
                contador = 1; // Reiniciar el contador si encontramos un área mayor
            } else if (listaAreas[i] == mayor) {
                contador++; // Incrementar el contador si encontramos otra área mayor
            }
        }

        // Crear un arreglo bidimensional para almacenar las áreas mayores junto con sus posiciones
        String[][] areasMayores = new String[contador][2];
        int index = 0;

        // Agregar las áreas mayores y sus posiciones al arreglo bidimensional
        for (int i = 0; i < listaAreas.length; i++) {
            if (listaAreas[i] == mayor) {
                areasMayores[index][0] = Integer.toString(posiciones[i]);
                areasMayores[index][1] = Double.toString(mayor);
                index++;
            }
        }
        return areasMayores;
    }

    public static String[][] obtenerAreasMenores() {
        double menor = Double.MAX_VALUE; // Inicializar con un valor grande
        int contador = 0;

        // Contar cuántos triángulos tienen el área menor
        for (int i = 0; i < listaAreas.length; i++) {
            if (listaAreas[i] < menor) {
                menor = listaAreas[i];
                contador = 1; // Reiniciar el contador si encontramos un área menor
            } else if (listaAreas[i] == menor) {
                contador++;
                // Incrementar el contador si encontramos otra área menor
            }
        }
        // Crear un arreglo bidimensional para almacenar las áreas menores junto con sus posiciones
        String[][] areasMenores = new String[contador][2];
        int index = 0;

        // Agregar las áreas menores y sus posiciones al arreglo bidimensional
        for (int i = 0; i < listaAreas.length; i++) {
            if (listaAreas[i] == menor) {
                areasMenores[index][0] = Integer.toString(posiciones[i]);
                areasMenores[index][1] = Double.toString(menor);
                index++;
            }
        }

        return areasMenores;
    }
    // Metodo que ordena el array de las areas de mayor a menor

}