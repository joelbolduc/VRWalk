package com.example.mylocation.vrwalk;

public class Complex {
    double real;
    double imaginary;
    public Complex(double real, double imaginary){
        this.real=real;
        this.imaginary=imaginary;
    }
    public static Complex add(Complex c1, Complex c2){
        return new Complex(c1.real+c2.real,c1.imaginary+c2.imaginary);
    }

    public static Complex mult(Complex c1,Complex c2){
        return new Complex(c1.real*c2.real-c1.imaginary*c2.imaginary,c1.real*c2.imaginary+c1.imaginary*c2.real);
    }

    public static Complex exp(Complex c){
        return mult(new Complex(Math.exp(c.real),0),new Complex(Math.cos(c.imaginary),Math.sin(c.imaginary)));
    }

    public static double phase(Complex c){
        if(c.real>0){
            return Math.atan(c.imaginary/c.real)%(2*Math.PI);
        }
        else if(c.real<0){
            return (Math.PI+Math.atan(c.imaginary/c.real))%(2*Math.PI);
        }
        else if(c.real==0 && c.imaginary<0){
            return 3*Math.PI/2;
        }
        else{
            return Math.PI/2;
        }
    }

}