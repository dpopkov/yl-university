package ylab.hw2.complexnumbers;

public class ComplexNumber {

    private final double real;
    private final double imaginary;

    public ComplexNumber(double real) {
        this(real, 0.0);
    }

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public ComplexNumber add(ComplexNumber other) {
        return new ComplexNumber(this.real + other.real, this.imaginary + other.imaginary);
    }

    public ComplexNumber subtract(ComplexNumber other) {
        return new ComplexNumber(this.real - other.real, this.imaginary - other.imaginary);
    }

    public ComplexNumber multiply(ComplexNumber other) {
        double realProduct = this.real * other.real;
        double imaginarySumOfProducts = this.imaginary * other.real + this.real * other.imaginary;
        double imaginaryProduct = this.imaginary * other.imaginary;
        return new ComplexNumber(realProduct - imaginaryProduct, imaginarySumOfProducts);
    }

    public double modulus() {
        return Math.sqrt(this.real * this.real + this.imaginary * this.imaginary);
    }

    @Override
    public String toString() {
        if (imaginary != 0.0) {
            return real + (imaginary < 0 ? " - " : " + ") + Math.abs(imaginary) + "i";
        }
        return Double.toString(real);
    }
}
