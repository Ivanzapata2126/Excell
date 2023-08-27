package org.example;

public class Triangulo {

    // Atributos
    public double lado_a;

    public double lado_b;

    public double lado_c;

    // Constructor
    Triangulo(double lado_a,double lado_b, double lado_c){
        this.lado_a = lado_a;
        this.lado_b = lado_b;
        this.lado_c = lado_c;
    }

    // Metodo que valida que los lados ingresados sean de un triangulo
    public boolean validarTriangulo(){
        if (lado_a + lado_b > lado_c && lado_a + lado_c > lado_b && lado_b + lado_c > lado_a) {
            return true;
        } else {
            return false;
        }
    }

    // Metodo que obtiene el area
    public double obtenerArea(){
        double s = (this.lado_a+this.lado_b+this.lado_c) / 2;
        return Math.sqrt(s * (s-this.lado_a) * (s-lado_b) * (s-lado_c));
    }
}
