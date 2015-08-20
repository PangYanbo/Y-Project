package DataModify;

import junit.framework.Assert;

import org.junit.Test;

public class ExtractFileTest {

	@Test
	public void testUncompress() {
		double actual = xxx(2,7);
		double expcet = 14;
		Assert.assertEquals(expcet, actual, 0.001);
		
		double actual2 = xxx(1,1);
		double expcet2 = 0;
		Assert.assertEquals(expcet2, actual2, 0.001);
	}
	
	public double xxx(double a , double b){
		double c = a * b;
		return c;
	}

}
