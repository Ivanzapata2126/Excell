package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Archivo  {
    public static boolean error = false;

    public static List<String> inconsistencias = new ArrayList<>();
    public static double[] triangulos;
    public static int[] posiciones = new int[7];

    public static void leerArchivoConsola(){
        Scanner sn = new Scanner(System.in);
        boolean salir;
        do {
            try {
                salir = false;
                System.out.println("Ingrese la ruta exacta donde se encuentra el archivo EXCEL. Eje: C:/Users/USUARIO/Desktop: ");
                String rutaArchivo = sn.nextLine();
                abrirArchivo(rutaArchivo);
                while (error) {
                    try {
                        System.out.println("No se ha encontrado el archivo. Por favor revise el nombre y la ruta ingresada");
                        System.out.println("¿Desea hacer el proceso de nuevo? pulse s o S para confirmar. Cualquier otra tecla para salir: ");
                        String op = sn.nextLine();
                        if (op.equals("s") || op.equals("S")) {
                            leerArchivoConsola();
                        } else {
                            System.out.println("Hasta pronto!!");
                            error = false;
                            return;
                        }
                    } catch (InputMismatchException ex) {
                        System.out.println("Hasta pronto!!");
                        error = false;
                        return;
                    }
                }
            } catch (InputMismatchException ex) {
                System.out.println("Formato ingresado no valido");
                salir = true;
            }
        }while(salir);
    }
    public static void abrirArchivo(String rutaArchivo){
        try (FileInputStream file = new FileInputStream(rutaArchivo)) {
            XSSFWorkbook worbook = new XSSFWorkbook(file);
            convertirAMatriz(worbook);
        } catch (Exception e) {
            error = true;
            e.printStackTrace();
        }
    }


    public static void escribirArchivo() {
        String nombreArchivo = "resultados.xlsx";
        String nombreHoja = "Resultados";
        DecimalFormat df = new DecimalFormat("#.##");
        String directorioActual = System.getProperty("user.dir");
        String rutaArchivo = directorioActual + "\\" + nombreArchivo;
        String[] header = new String[]{"ID", "ÁREA", "Triángulo de mayor área", "Triángulo de menor área", "Listado Ordenado","Inconsistencias",""};

        String[][] datos = new String[triangulos.length + 1][header.length];
        String[][] areasMayores = Main.obtenerAreasMayores();
        String[][] areasMenores = Main.obtenerAreasMenores();
        Arrays.sort(Main.listaAreasOrdenadas,Collections.reverseOrder());
        for(int i = 0;i<triangulos.length;i++){
            datos[i][0] = String.valueOf(posiciones[i]);
            datos[i][1] = String.valueOf(df.format(triangulos[i]));
        }
        for(int j = 0; j<areasMayores.length;j++){
            datos[j][2] = "El triangulo de id " + areasMayores[j][0] + " con: " + df.format(Double.parseDouble(areasMayores[j][1]));
        }
        for(int h = 0; h<areasMenores.length;h++){
            datos[h][3] = "El triangulo de id " + areasMenores[h][0] + " con: " + df.format(Double.parseDouble(areasMenores[h][1]));
        }

        datos[0][4] = "AREAS";

        for(int s = 0; s< Main.listaAreasOrdenadas.length;s++){
           datos[s+1][4] = String.valueOf(Main.listaAreasOrdenadas[s]);
        }

        for(int s = 0; s<inconsistencias.size();s++){
            datos[s][5] = inconsistencias.get(s);
        }

        try (XSSFWorkbook libro = new XSSFWorkbook()) {
            XSSFSheet hoja = libro.createSheet(nombreHoja);
            CellStyle style = libro.createCellStyle();
            Font font = libro.createFont();
            font.setBold(true);
            style.setFont(font);

            // Generar los datos para el documento
            for (int i = 0; i < triangulos.length + 2; i++) {
                XSSFRow row = hoja.createRow(i); // Se crea las filas
                for (int j = 0; j < header.length; j++) {
                    XSSFCell cell = row.createCell(j); // Se crea las celdas para la cabecera, junto con la posición
                    // Se crea las celdas para el contenido, junto con la posición
                    if (i == 0) { // Para la cabecera
                        cell.setCellStyle(style); // Se añade el style crea anteriormente
                        cell.setCellValue(header[j]); // Se añade el contenido
                    } else { // Para el contenido
                        cell.setCellValue(datos[i-1][j]); // Se añade el contenido
                    }
                }
            }

            try (FileOutputStream salidaArchivo = new FileOutputStream(rutaArchivo)) {
                System.out.println("Archivo Creado en la ruta: " + directorioActual);
                libro.write(salidaArchivo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void convertirAMatriz(XSSFWorkbook workbook){
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        int contador = 0;
        rowIterator.next();// Saltar la primera fila de cabeceras
        triangulos = new double[7];

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            Cell idCell = row.getCell(0);
            Cell ladoACell = row.getCell(1);
            Cell ladoBCell = row.getCell(2);
            Cell ladoCCell = row.getCell(3);


            double ladoA = 0.0;
            double ladoB = 0.0;
            double ladoC = 0.0;

            ladoA = validarLado(ladoACell);
            ladoB = validarLado(ladoBCell);
            ladoC = validarLado(ladoCCell);

            validarTriangulo(ladoA,ladoB,ladoC, (int) idCell.getNumericCellValue());

            Triangulo triangulo = new Triangulo(ladoA,ladoB,ladoC);
            triangulos[contador] = triangulo.obtenerArea();
            posiciones[contador] = (int) idCell.getNumericCellValue();
            contador++;
        }
    }

    public static double validarLado(Cell ladoCell){
        double lado = 0;
        if (ladoCell != null && ladoCell.getCellType() == CellType.NUMERIC) {
            lado = ladoCell.getNumericCellValue();
            if(lado < 0){
                inconsistencias.add("El valor de la celda" + ladoCell.getAddress() + "es negativo, se cambió por su valor absoluto");
                lado = Math.abs(lado);
            }
        }else {
            inconsistencias.add("El valor de la celda" + ladoCell.getAddress() + "no es un número o está vacía");
        }
        return lado;
    }

    public static boolean validarTriangulo(double lado_a,double lado_b,double lado_c,int id){
        if (lado_a + lado_b > lado_c && lado_a + lado_c > lado_b && lado_b + lado_c > lado_a) {
            return true;
        } else {
            inconsistencias.add("El triangulo #"+id+ " no cumple con la fórmula de Herón");
            return false;
        }
    }
}
