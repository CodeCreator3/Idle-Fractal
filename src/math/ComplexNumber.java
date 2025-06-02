package src.math;

public class ComplexNumber {
    public double a, b;
    public ComplexNumber(double a, double b) {
        this.a = a;
        this.b = b;
    }
    public ComplexNumber add(ComplexNumber v) {
        return new ComplexNumber(this.a + v.a, this.b + v.b);
    }
    public ComplexNumber subtract(ComplexNumber v) {
        return new ComplexNumber(this.a - v.a, this.b - v.b);
    }
    public double dot(ComplexNumber v) {
        return this.a * v.a + this.b * v.b;
    }
    public double magnitude() {
        return Math.sqrt(this.a * this.a + this.b * this.b);
    }
    public ComplexNumber normalize() {
        double mag = this.magnitude();
        if (mag == 0) return new ComplexNumber(0, 0);
        return new ComplexNumber(this.a / mag, this.b / mag);
    }
    public double angleWith(ComplexNumber v) {
        double dotProduct = this.dot(v);
        double magnitudes = this.magnitude() * v.magnitude();
        if (magnitudes == 0) return 0; // Avoid division by zero
        return Math.acos(dotProduct / magnitudes);
    }
    public double distanceTo(ComplexNumber v) {
        return Math.sqrt(Math.pow(this.a - v.a, 2) + Math.pow(this.b - v.b, 2));
    }
    public ComplexNumber multiply(double x){
        return new ComplexNumber(this.a * x, this.b * x);
    }
    public ComplexNumber multiply(ComplexNumber v) {
        double real =  this.a * v.a - this.b * v.b;
        double imaginary = this.b * v.a + this.a * v.b;
        return new ComplexNumber(real, imaginary);
    }
    public ComplexNumber sqrt(){
        double magnitude = this.magnitude();
        if (magnitude == 0) return new ComplexNumber(0, 0);
        if(b == 0){
            if (a < 0) {
                a = -a; // Ensure a is non-negative for square root
            }
            return new ComplexNumber(Math.sqrt(a), 0);
        }
        return new ComplexNumber(Math.sqrt((magnitude + a) / 2.0), Math.sqrt((magnitude - a) / 2.0) * (this.b < 0 ? -1 : 1));
    }
}
