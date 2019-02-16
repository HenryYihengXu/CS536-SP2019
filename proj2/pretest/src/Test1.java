public class Test1 {
	public static void main(String[] args) {
		System.out.println(Integer.MAX_VALUE);
		String s1 = "asdf";
		String s2 = "2147483647";
		String s3 = "2147483648";
        //System.out.println(new Integer(s1).intValue());
        System.out.println(new Integer(s2).intValue());
        System.out.println(new Integer(s3).intValue());
        System.out.println("\");
	}
}

