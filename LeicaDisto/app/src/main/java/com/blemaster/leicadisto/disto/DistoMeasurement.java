package com.blemaster.leicadisto.disto;

import android.text.format.Time;

public class DistoMeasurement
{
	public String m_Angle = "";		// Radian 
	public String m_AngleUnit = "";
	public int m_AngleUnitBLE = -1;
	public String m_Distance = "";
	public float m_DistanceOriginal;
	public String m_DistanceUnit = "";
	public int m_DistanceUnitBLE = -1;
	public Time m_Time;
	public boolean m_bEnter = false;
	public boolean m_bMetric = false;
	public boolean m_bTab = false;
	public int m_nMetricDezimals = 0;

	public static double pi = 3.141592653589793D;
	public static double coeff_feet = 3.28083989501312D;	// m -> feet
	public static double coeff_in = 39.370079040527344D;		// m -> inch
		
	
	public static String getFractioned(double paramDouble, int paramInt)
	{
		double d = paramDouble * 0.0032808399D;	// cm-> feet
		long intPart1 = (long)d;
		long intPart2;
		long intPart3;	// l4
		long intPart4;	// l3
		long intPart5;	// l1
		long intPart6;	// l2
		long intPart7;	// l7
		
		d = Math.abs(intPart1 - d) * 12.0D;
		intPart2 = (long)d;
		
		d = Math.abs(intPart2 - d);
		intPart3 = (long)((double)(32.0D * d + 0.5D));
		
//		JQuery.w("ulFractions: " + intPart3 + " restInches: " + d);
		
		for (intPart4 = 32L; (intPart3 % 2L == 0L) && (intPart3 != 0L); intPart4 /= 2L) {
			intPart3 = (long)((double)((intPart3 / 2L) + 0.5D));
		}

		intPart5 = intPart4;
		intPart6 = intPart3;
		
		if (paramInt == 8) {
			for (;;)
			{
				intPart5 = intPart4;
				intPart6 = intPart3;
				if (intPart4 <= 4L) {
					break;
				}
				intPart3 = (long)((double)(intPart3 / 2L + 0.5D));
				intPart4 /= 2L;
			}
		}
		else if (paramInt == 7) {
			for (;;)
			{
				intPart5 = intPart4;
				intPart6 = intPart3;
				if (intPart4 <= 8L) {
					break;
				}
				intPart3 = (long)((double)(intPart3 / 2L + 0.5D));
				intPart4 /= 2L;
			}
		}
		else if (paramInt == 6) {
			for (;;)
			{
				intPart5 = intPart4;
				intPart6 = intPart3;
				if (intPart4 <= 16L) {
					break;
				}
				intPart3 = (long)((double)(intPart3 / 2L + 0.5D));
				intPart4 /= 2L;
			}
		}
		else if (paramInt == 5) {
			for (;;)
			{
				intPart5 = intPart4;
				intPart6 = intPart3;
				if (intPart4 <= 32L) {
					break;
				}
				intPart3 = (long)((double)(intPart3 / 2L + 0.5D));
				intPart4 /= 2L;
			}
		}
		
		while ((intPart6 % 2L == 0L) && (intPart6 != 0L))
		{
			intPart6 /= 2L;
			intPart5 /= 2L;
		}
		
		intPart3 = intPart1;
		intPart4 = intPart2;
		
		if (paramDouble < 0.0D)
		{
			intPart3 = -intPart1;
			intPart4 = -intPart2;
		}
		
		intPart7 = intPart4;
		intPart1 = intPart5;
		intPart2 = intPart6;
		
		if (intPart5 == 1L)
		{
			if (intPart6 == 1L)
			{
				intPart2 = 0L;
				intPart1 = 0L;
				intPart7 = intPart4 + 1L;
			}
			else 
			{
				intPart7 = intPart4;
				intPart1 = intPart5;
				intPart2 = intPart6;
			}
		}

		String str2 = "";
		if ((paramInt == 8) || (paramInt == 7) || (paramInt == 6) || (paramInt == 5))
		{
			if (paramDouble < 0.0D) {
			}
			else 
			{
				
				for (String str1 = String.format("-%d' %d\"",
						new Object[] { Long.valueOf(Math.abs(intPart3)), Long.valueOf(Math.abs(intPart7)) });
						;
					 str1 = String.format("%d' %d\"", new Object[] { Long.valueOf(Math.abs(intPart3)), Long.valueOf(Math.abs(intPart7)) }))
				{
					str2 = str1;
					if (intPart2 != 0L) {
						str2 = str1 + String.format(" %d/%d",new Object[] { Long.valueOf(intPart2), Long.valueOf(intPart1) });
					}
					return str2;
				}
			}
			
		}
		
		intPart5 = Math.abs(intPart3) * 12L + Math.abs(intPart7);
		if (paramDouble < 0.0D) {
			
		}
		else 
		{
			for (String str1 = String.format("-%d", new Object[] { Long.valueOf(intPart5) });				
                ;
     	        str1 = String.format("%d", new Object[] { Long.valueOf(intPart5) }))
			{
				str2 = str1;
				if (intPart2 != 0L) {
					str2 = str1 + String.format(" %d/%d", new Object[] { Long.valueOf(intPart2), Long.valueOf(intPart1) });
				}
				return str2 + " in";
			}
		}
		return str2;
		
	}
	
	public String getAngle()
	{
		double ang_degree = Float.valueOf(m_Angle).floatValue() * 180.0F / 3.141592653589793D;	// d2
		double ang_radian = Float.valueOf(m_Angle).floatValue();	// d3

		String str_angle;
		double result_degree;	// d1
		
		if (ang_radian < -(pi/2)) {
			result_degree = 180.0D + (ang_degree + 180.0D);
		}
		else
		{
			if (ang_radian < 0.0D)
				result_degree = ang_degree + 360.0D;
			else
				result_degree = ang_degree;

			if ((ang_radian > pi) && (ang_radian < (2 *pi))) {
				result_degree = -90.0D + (ang_degree - 90.0D);
			}
			else if (ang_radian < -(pi/2))
			{
				result_degree = ang_degree;
				if (ang_radian > -pi) {
					result_degree = ang_degree + 180.0D;
				}
			}

		}

		switch (m_AngleUnitBLE)
		{
		default: 
			return m_Angle + m_AngleUnit;
			
		case 6:	// Raw Degree
			str_angle = String.format("%.2f°", new Object[] { Double.valueOf(Math.round(ang_degree / 0.05D) * 0.05D) });
			break;

		case 5:
			result_degree = Math.round(result_degree / 0.05D) * 0.05D;
//			JQuery.i("", "angle calc: " + d1);
			str_angle = String.format("%.2f°", new Object[] { Double.valueOf(result_degree) });
			break;

		case 4:
			str_angle = String.format("%.2f%%", new Object[] { Double.valueOf(Math.tan(ang_radian) * 100.0D) });
			break;

		case 3:
			str_angle = String.format("%.1fmm/m", new Object[] { Double.valueOf(1000.0D * ang_radian) });
			break;

		case 2:
			str_angle = String.format("%.2fin/ft", new Object[] { Double.valueOf(ang_radian * 12.0D) });
			break;
		}

		return str_angle;
	}
	
	public String getDistance()
	{
		String str_distance;	// localObject1
		
		if ((m_Distance != null) && (m_Distance.isEmpty()))
		{
			str_distance = "...";
			return str_distance;
		}
		
		boolean bIsVolume = false;	// n
		boolean bIsArea = false;	// m
		int distanceUnitBle = m_DistanceUnitBLE;	// i1
		double distance;	// d2
		double ret_distance = 0.0;	// d1, return distance value with selected type
		int distanceUnitType = 0;	// i
		
		try
		{
			distance = Double.valueOf(this.m_Distance.replace(",", ".").replaceAll("[^0-9?!\\.]", "")).doubleValue();
//				JQuery.d("DISTANCE: " + this.m_Distance + " UNIT: " + this.m_DistanceUnitBLE);

			if ((distanceUnitBle < 100) || (distanceUnitBle >= 1000))
			{
				ret_distance = distance;
				distanceUnitType = distanceUnitBle;

				if (distanceUnitBle >= 1000)
				{
					bIsVolume = true;						
					distanceUnitBle -= 1000;
					
					if ((distanceUnitBle == 9) 
						|| (distanceUnitBle == 13) 
						|| (distanceUnitBle == 12) 
						|| (distanceUnitBle == 11) 
						|| (distanceUnitBle == 10) 
						|| (distanceUnitBle == 4) 
						|| (distanceUnitBle == 8) 
						|| (distanceUnitBle == 7) 
						|| (distanceUnitBle == 6) 
						|| (distanceUnitBle == 5))
					{
						distanceUnitType = 4;

						// k (of k-ε)
						// m2/s2  ×  10.7639104167097  =  ft2/s2
						ret_distance = coeff_feet * distance * coeff_feet;
					}
					else if (distanceUnitBle == 3)
					{
						distanceUnitType = 0;
						ret_distance = distance;
					}
					else
					{
						ret_distance = distance;
						distanceUnitType = distanceUnitBle;
						if (distanceUnitBle == 14)
						{
							// m2/s2  ×  1.09361329833771  =  yd2/s2
							ret_distance = coeff_feet * distance / 3.0D * coeff_feet / 3.0D;
							distanceUnitType = distanceUnitBle;
						}
					}
				}
				else
				{
					if (distanceUnitBle == 3)
					{
						distanceUnitType = 0;
						ret_distance = distance;
					}
					else
					{
						ret_distance = distance;
						distanceUnitType = distanceUnitBle;
						if (distanceUnitBle == 14)
						{
							// meter --> yard
							ret_distance = coeff_feet * distance / 3.0D;
							distanceUnitType = distanceUnitBle;
						}
					}
				}
			}
			else 
			{
				bIsArea = true;
				distanceUnitBle -= 100;
				if ((distanceUnitBle == 9) 
					|| (distanceUnitBle == 13) 
					|| (distanceUnitBle == 12) 
					|| (distanceUnitBle == 11) 
					|| (distanceUnitBle == 10) 
					|| (distanceUnitBle == 4) 
					|| (distanceUnitBle == 8) 
					|| (distanceUnitBle == 7) 
					|| (distanceUnitBle == 6) 
					|| (distanceUnitBle == 5))
				{
					distanceUnitType = 4;
					ret_distance = distance * coeff_feet;
					
				}
			}

			switch (distanceUnitType)
			{
				default:
				case 1:	// "m" with fraction-3
					str_distance = String.format("%.3fm", new Object[] { Double.valueOf(1.0E-5D + ret_distance) });
					break;

				case 2: // "m" with fraction-2
					str_distance = String.format("%.2fm", new Object[] { Double.valueOf(ret_distance) });
					break;

				case 3:	// "mm" with fraction-1
					str_distance = String.format("%.1fmm", new Object[] { Double.valueOf(1000.0D * ret_distance) });
					break;

				case 4:	// "m" with fraction-4
					str_distance = String.format("%.4fm", new Object[] { Double.valueOf(ret_distance) });
					break;

				case 5:	// "ft" with fraction-2
					str_distance = String.format("%.2fft", new Object[] { Double.valueOf(coeff_feet * ret_distance) });
					break;

				case 6:	// with fraction-paranm
					str_distance = getFractioned(1000.0D * ret_distance, 4);// i --> 4
					break;

				case 7:	// "in" with fraction-2
					str_distance = String.format("%.2f in", new Object[] { Double.valueOf(coeff_in * ret_distance) });
					break;

				case 8:	// with fraction-paranm
					str_distance = getFractioned(1000.0D * ret_distance, 4);// i --> 4
					break;

				case 9:	// "yd" with fraction-3
					str_distance = String.format("%.3fyd", new Object[] { Double.valueOf(coeff_feet * ret_distance / 3.0D) });
					break;
					
			}

			String area_volume = str_distance;
								
			if (bIsArea) {
				area_volume = (String)str_distance + "²";
			}

			str_distance = area_volume;
			
			if (bIsVolume == false) {
				return str_distance;
			}
			else {
				return (String)area_volume + "³";
			}
			
		}
		catch (NumberFormatException localNumberFormatException)
		{
//				JQuery.e(localNumberFormatException);
			return m_Distance;
		}
	}
	
	public String getType()
	{
//		JQuery.e("M_DISTANCEUNIT: " + this.m_DistanceUnit + " BLE: " + this.m_DistanceUnitBLE);
		if ((m_DistanceUnit.endsWith("²"))
				|| ((m_DistanceUnitBLE >= 100) && (m_DistanceUnitBLE < 1000))) {
			return "Area";
		}
		if ((m_DistanceUnit.endsWith("³")) || (m_DistanceUnitBLE >= 1000)) {
			return "Volume";
		}
		return "Inclination";
	}
	
	public boolean isSpecial()
	{
		if ((m_DistanceUnit.endsWith("²"))
				|| (m_DistanceUnit.endsWith("³"))
				|| (m_DistanceUnit.endsWith("°"))
				|| (m_DistanceUnitBLE >= 100)) {

		}

		while ((m_Distance.equals("")) && (!m_Angle.equals(""))) {
			return true;
		}
		return false;
	}
}


