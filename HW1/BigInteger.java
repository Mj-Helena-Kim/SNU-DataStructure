/*==========================================================================================
|HW #1: BigInteger
|
|Author: KIM MIN JU
|Language: JAVA
|
|Class: M1522.000900 - 001 Data Structure
|Instructor: Prof. Byung-Ro Moon (moon@snu.ac.kr)
|Due Date:2022.04.01(FRI) 23:59 KST
|
|Description:
|	Calculation of big integers.
|	Input number has 100 digits for maximum;
|   Output number has 101 digits(+ or -) or 200 digits(*) for maximum.
|   Finish program when "quit" is entered.
|	Input format:
|		(space)(sign)(space)(number)(space)(+/-/*)(space)(sign)(space)(number)(space)
=============================================================================================*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("(\\d+)([\\+\\-\\*]+)(\\d+)");

    private int[] arr;
    private char sign;
  
    public BigInteger(){
        this.arr = new int[0];
        this.sign = ' ';
    }
  
    public BigInteger(String s){
        char[] tmparr = s.toCharArray();
        int l = tmparr.length;

        if(tmparr[0] == '-'){
            this.sign = '-';
            this.arr = new int[l - 1];
            for (int i = 0; i < l - 1; i++)
                this.arr[i] = tmparr[l-i-1] - '0';
        }
        else if (tmparr[0] == '+'){
            this.sign = '+';
            this.arr = new int[l - 1];
            for (int i = 0; i < l - 1; i++)
                this.arr[i] = tmparr[l-i-1] - '0';
        }
        else{
            this.sign = '+';
            this.arr = new int[l];
            for(int i = 0; i < l; i++)
                this.arr[i] = tmparr[l-i-1] - '0';
        }
    }
  
    public BigInteger add(BigInteger big){
        int l = this.arr.length;
        int carry = 0;
        BigInteger sum = new BigInteger();

        //define sign of the sum
        if(this.sign == '-') // if neg. + neg., sign should be neg.
            sum.sign = '-';
        
        sum.arr = new int[l + 1];

        for(int i = 0; i < l; i++){
            sum.arr[i] = carry + this.arr[i] + big.arr[i];
            if(sum.arr[i] >= 10){
                carry = 1;
                sum.arr[i] -=10;
            }
            else
                carry = 0;
        }
        sum.arr[l] = carry;
        
        return sum;
    }
  
    public BigInteger subtract(BigInteger big){
        int l = this.arr.length;
        int borrow = 0;
        BigInteger difference = new BigInteger();
        
        difference.sign = this.sign;

        difference.arr = new int[l+1];
        for(int i = 0; i < l; i++){
            difference.arr[i] = this.arr[i] - big.arr[i] - borrow;
            if(difference.arr[i] < 0){
                difference.arr[i] += 10;
                borrow = 1;
            }
            else
                borrow = 0;
        }

        return difference;
    }
  
    public BigInteger multiply(BigInteger big){
        int l = this.arr.length;
        BigInteger product = new BigInteger();

        //define sign of the product
        product.sign = (this.sign == big.sign) ? '+' : '-' ;
        
        product.arr = new int[2*l];

        for(int i = 0; i < l; i++){
            for(int j = 0; j < l; j++){
                product.arr[i + j] = product.arr[i + j] + this.arr[i] * big.arr[j];
                if(product.arr[i+j] >= 10){
                    product.arr[i+j+1] += product.arr[i+j] / 10;
                    product.arr[i+j] %= 10;
                }
            }
        }
        return product;
    }

    public BigInteger calculate(BigInteger big, char operator){
        int alength = this.arr.length;
        int blength = big.arr.length;
        int flag = 0;
        BigInteger largerInt = new BigInteger();
        BigInteger smallerInt = new BigInteger();
        BigInteger subtractionOfSame = new BigInteger();

        //arrange digit
        if(alength > blength){
            smallerInt.arr = new int[alength];
            smallerInt.sign = big.sign;
            for(int i = 0; i < blength; i++)
                smallerInt.arr[i] = big.arr[i];
            for(int i = blength; i < alength; i++)
                smallerInt.arr[i] = 0;
            largerInt = this;
        }
        else if (alength < blength){
            smallerInt.arr = new int[blength];
            smallerInt.sign = this.sign;
            for(int i = 0; i < alength; i++)
                smallerInt.arr[i] = this.arr[i];
            for(int i = alength; i < blength; i++)
                smallerInt.arr[i] = 0;
            largerInt = big;
        }
        else{ //same length
            for(int i = alength - 1; i >= 0; i--){
                if(this.arr[i] == big.arr[i]){
                    continue;
                }
                else if(this.arr[i] > big.arr[i]){
                    largerInt = this;
                    smallerInt = big;
                    flag = 1;
                    break;
                }
                else{
                    largerInt = big;
                    smallerInt = this;
                    flag = 1;
                    break;
                }
            }
            if(flag == 0){ // this == big
                largerInt = this;
                smallerInt = big;
                flag = 2;
            }
        }

        if(operator == '*')
            return largerInt.multiply(smallerInt);
        else if(this.sign == big.sign)
            return largerInt.add(smallerInt);
        else
            if(flag == 2)
                return subtractionOfSame;
            return largerInt.subtract(smallerInt);
    }
  
    @Override
    public String toString(){
        int l = this.arr.length;
        String s = "";

        //int array to string
        for(int i = 0; i < l; i++)
            s += this.arr[l - i -1];

        //remove zero if necessary
        s = s.replaceFirst ("^0*", "");
        if (s.isEmpty()) s = "0";

        //add sign if necessary
        if(this.sign == '-' && s != "0")
            s = "-" + s;

        return s;
    }

  
    static BigInteger evaluate(String input) throws IllegalArgumentException{

        String a, b, operator;

        input = input.replaceAll("\\p{Z}", "");
        Matcher m = EXPRESSION_PATTERN.matcher(input);
        
        if(!m.find())  System.err.println(MSG_INVALID_INPUT);
        
        a = m.group(1);
        operator = m.group(2);
        b = m.group(3);

        //add a sign
        if(input.startsWith("-"))
            a = '-' + a;
        
        //add b sign
        if(operator.length() != 1){
            if(!operator.startsWith("-")){
                if(operator.endsWith("-")){
                    b = '-' + b;
                }
            }
            else{
                if(operator.endsWith("+"))
                    b = '-' + b;
                operator = "+";
            }
        }
        else if (operator.equals("-")){
            b = '-' + b;
            operator = "+";
        }

        char o = operator.charAt(0);

        BigInteger A = new BigInteger(a);
        BigInteger B = new BigInteger(b);

        return A.calculate(B,o);
    }
  
    public static void main(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
