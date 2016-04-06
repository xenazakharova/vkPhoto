package ua.xenazakharova.test.vkphoto;

public class Utils {
	public static String plural(int number, String form1, String form2, String form3) {
		String result;
		
		int lastDigit = Math.abs(number) % 10;
		switch(lastDigit){
			case 0 :
				result = " ";
			break;
			case 1 :
				if(number==11)
					result = number + " " + form3;
				else
					result = number + " " + form1;
			break;
				
			case 2 :
				if(number==12)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			case 3 :
				if(number==13)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			case 4 :
				if(number==14)
					result = number + " " + form3;
				else
					result = number + " " + form2;
			break;
				
			default:
				result = number + " " + form3;
			break;
			
		}
	    return result;
	}

}
