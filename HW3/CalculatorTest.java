/*=====================================================================================
|HW #3: Calculator Test
|
|Author: KIM MIN JU
|Student ID: 2018-11550
|Language: JAVA
|
|Class: M1522.000900 - 001 Data Structure
|Instructor: Prof. Byung-Ro Moon (moon@snu.ac.kr)
|Due Date:2022.04.29(FRI) 23:59 KST
|
|Description:
|	Implement a calculator using stack.
|	Supports integer calcutation in the range of long data type.
|   Supporting operators: +, -, *, /, %, ^, (,)
|   Finish program when "q" is entered.
|	Input expression can be:
|		'0', '1', ..., '9', '+', '-', '*', '/', '%', '^', ' ', '\t', '(',')'
========================================================================================*/

import java.io.*;
import java.lang.Math;
import java.lang.String;
import java.util.Stack;

public class CalculatorTest{

	private static int priority(char ch){ //set priority
		switch(ch){
			case '(':
			case ')':
				return 1;

			case '^':
				return 2;
			
			case '~':
				return 3;
			
			case '*':
			case '/':
			case '%':
				return 4;
			
			case '+':
			case '-':
				return 5;
			
			default:
				return -1;
		}
	}

	public static String processInput(String s) throws Exception{
		//process Input(infix) to postfix expression
		String postfix = "";
		Stack<Character> operator = new Stack<>();
		boolean numberAdded = false;
		boolean shouldHaveOperator = false;
		boolean shouldHaveNumber = false;

		//1. Check the input string from left to right one character at a time.
		for(char ch : s.toCharArray()){
			//2. If the input is operand, append to postfix Linked list.
			if(Character.isDigit(ch)){ // if ch is number
				numberAdded = true;
				postfix += ch;
				if(shouldHaveOperator)	throw new Exception();
				shouldHaveNumber = false;
				continue;
			}
			else if(ch == ' ' || ch == '\t'){ //if ch is whitespace or tab
				if(numberAdded)	shouldHaveOperator = true;
				continue;
			}
			// if ch is operator
			shouldHaveOperator = false;
			if(!postfix.endsWith(" ") && (postfix != "")) postfix += " ";

			if(ch == '-' && !numberAdded)	ch = '~';

			if(shouldHaveNumber && (ch != '~') && (ch != '('))	throw new Exception();
			if(numberAdded && ch == '(') throw new Exception();
			if(!numberAdded && ch == ')')	throw new Exception();
	
			//3. If "(" : push to stack
			if(ch == '('){	
				operator.push(ch);
				shouldHaveNumber = true;
			}
			//4. If ")": all operators down to the most recently scanned "(" must be popped and appended to the postfix Linked List; the pair of () must be discarded
			else if(ch == ')'){
				while(!operator.empty() && (operator.peek() != '('))
					postfix += (operator.pop() + " ");
				if(operator.peek() == '(')	operator.pop();
			}
			//5.If the next char is an operator: check priority
			else{
				shouldHaveNumber = true;
				if(ch != '^' && ch != '~'){ // left associated
					while(!operator.empty() && (priority(ch) >= priority(operator.peek())) && (operator.peek() != '('))
						postfix += (operator.pop() + " ");
				}
				else{ // right associated
					while(!operator.empty() && (priority(ch) > priority(operator.peek())) && (operator.peek() != '('))
						postfix += (operator.pop() + " ");
				}
				
				operator.push(ch);
			}

			if(numberAdded && (ch == '(' || ch == ')'))	numberAdded = true;
			else numberAdded = false;
		}
		if(!postfix.endsWith(" ")) postfix += " ";

		//6. If input is all checked, all the remaining operators in stack should be popped and appended to the postfix Linked List.
		while(!operator.empty()){
			char op = operator.pop();
			if(op == '(')	throw new Exception();
			postfix += (op + " ");
		}
		
		if(postfix.endsWith(" "))	postfix = postfix.substring(0, postfix.length()-1);

		return postfix;
	}

	public static long calculate(String postfix) throws Exception{
		//return calculation result
		String[] splited = postfix.split("\\s+");
		Stack<Long> result = new Stack<>();
		long divisor = 0;
		long ans = 0;

		for(String s : splited){
			switch(s){
				case "+":
					result.push(result.pop() + result.pop());
					break;

				case "-":
					result.push(-result.pop() + result.pop());
					break;

				case "~":
					result.push(-result.pop());
					break;

				case "*":
					result.push(result.pop() * result.pop());
					break;

				case "/":
					divisor = result.pop();
					if(divisor == 0) throw new Exception(); //if x/0, error
					result.push(result.pop() / divisor);
					break;

				case "%":
					divisor = result.pop();
					if(divisor == 0) throw new Exception(); //if x%0, error
					result.push(result.pop() % divisor);
					break;

				case "^":
					long index = result.pop();
					long base = result.pop();
					if((base == 0) && (index < 0)) throw new Exception(); //if 0^y when y < 0, error
					result.push((long)Math.pow(base,index));
					break;
				
				default: // if string is number
					result.push(Long.parseLong(s));
					break;
			}
		}
		ans = result.pop();
		
		if(!result.empty())	throw new Exception();

		return ans;
	}

	public static void main(String args[]){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true){
			try{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e){
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception{
		String postfix = "";

		postfix = processInput(input); //change input to postfix expression
		long result = calculate(postfix); //calculate the result

		//print postfix expression
		//String postfixPrint = String.join(" ", postfix);
		System.out.println(postfix);

		//print calculation result
		System.out.println(result);
	}
}
