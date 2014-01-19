/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ir.mod.tavana.toranj.consensus.lib;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Template;
import java.util.regex.Pattern;

/**
 *
 * @author Admin
 */
public class Matrix <template>{
    private template [][] matrix;
    
    public Matrix (int m , int n)
    {
        matrix = (template[][]) new Object[m][n];
    }

    /* constructor that creates a matrix based on an
    input in form of a11,a12,...,a1n;a21,a22,...,an2;...ann; */
    public Matrix(String str)
    {
        try{
            str = str.trim();
            String[] rows = str.split(Config.MATRIX_ROWS_SEPERATOR);
            String[] numbersInAColumn = rows[0].split(Config.MATRIX_ENTRIES_SEPERATOR);
            matrix = (template[][]) new Object[rows.length][numbersInAColumn.length];
            for(int i = 0 ; i < rows.length ; i++){
                numbersInAColumn = rows[i].split(Config.MATRIX_ENTRIES_SEPERATOR);
                for(int j = 0 ; j < numbersInAColumn.length ; j++)
                {
                    matrix[i][j] = (template)numbersInAColumn[j];
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * @return the element
     */
    public template getElement(int m , int n) {
        return matrix[m][n];
    }

    /**
     * @param matrix the matrix to set
     */
    public void setElement(template element, int m , int n) {
        this.matrix[m][n] = element;
    }

    public String toString(){
        String str = "";
        for(int i = 0 ; i < matrix.length ; i++){
            for(int j = 0 ; j < this.matrix[i].length ; j++){
                str += matrix[i][j]+Config.MATRIX_ENTRIES_SEPERATOR;
            }
            str += Config.MATRIX_ROWS_SEPERATOR;
        }
        return str;
    }

    public int getLength(){
        if (this.matrix == null) return 0;
        return this.matrix[0].length;
    }

    public int getHeight(){
        if (this.matrix == null) return 0;
        return this.matrix.length;
    }

    public Matrix sum(Matrix m1 , Matrix m2){
        if(m1.getLength()!=m2.getLength() || m1.getHeight()!=m2.getHeight()) return null;
        int length = m1.getLength();
        int height = m1.getHeight();
        Matrix result = new Matrix<Float>(height, length);
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < length ; j++){
                float sum = Float.parseFloat(m1.getElement(i, j)+"")+Float.parseFloat(m2.getElement(i, j)+"");
                result.setElement(sum, i, j);
            }
        }
        return result;
    }

    public Matrix multiply(Matrix m1 , Matrix m2){
        if(m1.getLength()!=m2.getHeight() || m1.getHeight()==0 || m2.getHeight()==0) return null;
        
        int length = m2.getLength();
        int height = m1.getHeight();
        int commonSize = m1.getLength();
        Matrix result = new Matrix<Float>(height, length);
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < length ; j++){
                float sum = 0;
                for(int k = 0 ; k < commonSize ; k++){
                    sum += (Float)m1.getElement(i, k)*(Float)m2.getElement(k, j);
                }
                result.setElement(sum, i, j);
            }
        }
        return result;
    }

    public Matrix divide(Matrix m1 , float denominator){
        if(denominator == 0) return null;
        int length = m1.getLength();
        int height = m1.getHeight();
        Matrix result = new Matrix<Float>(height, length);
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < length ; j++){
                float tmp = (Float)m1.getElement(i, j)/denominator;
                result.setElement(tmp, i, j);
            }
        }
        return result;
    }

    // Multiplies matrix m1 by the specified coefficient
    public Matrix multiply(Matrix m1 , float coefficient){
        int length = m1.getLength();
        int height = m1.getHeight();
        Matrix result = new Matrix<Float>(height, length);
        for(int i = 0 ; i < height ; i++){
            for(int j = 0 ; j < length ; j++){
                float tmp = (Float)m1.getElement(i, j)*coefficient;
                result.setElement(tmp, i, j);
            }
        }
        return result;
    }

//    public static void main(String[] args){
//        String str = "1,2,5,3.5;4,3,7,19; ";
//        Matrix<Float> m = new Matrix(str);
//        System.out.println(m.getLength());
//        System.out.print(m.toString());
//    }
}
