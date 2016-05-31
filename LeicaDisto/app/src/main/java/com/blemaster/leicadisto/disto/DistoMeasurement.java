package com.blemaster.leicadisto.disto;

import android.text.format.Time;
import de.ffuf.leica.sketchlibrary.classes.JQuery;

public class DistoMeasurement
{
  public String m_Angle = "";
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
  
  public static String getFractioned(double paramDouble, int paramInt)
  {
    double d = paramDouble * 0.0032808399D;
    long l6 = d;
    d = Math.abs(l6 - d) * 12.0D;
    long l5 = d;
    d = Math.abs(l5 - d);
    long l4 = (32.0D * d + 0.5D);
    JQuery.w("ulFractions: " + l4 + " restInches: " + d);
    for (long l3 = 32L; (l4 % 2L == 0L) && (l4 != 0L); l3 /= 2L) {
      l4 = (l4 / 2L + 0.5D);
    }
    if (paramInt == 8) {
      for (;;)
      {
        l1 = l3;
        l2 = l4;
        if (l3 <= 4L) {
          break;
        }
        l4 = (l4 / 2L + 0.5D);
        l3 /= 2L;
      }
    }
    if (paramInt == 7) {
      for (;;)
      {
        l1 = l3;
        l2 = l4;
        if (l3 <= 8L) {
          break;
        }
        l4 = (l4 / 2L + 0.5D);
        l3 /= 2L;
      }
    }
    if (paramInt == 6) {
      for (;;)
      {
        l1 = l3;
        l2 = l4;
        if (l3 <= 16L) {
          break;
        }
        l4 = (l4 / 2L + 0.5D);
        l3 /= 2L;
      }
    }
    long l1 = l3;
    long l2 = l4;
    if (paramInt == 5) {
      for (;;)
      {
        l1 = l3;
        l2 = l4;
        if (l3 <= 32L) {
          break;
        }
        l4 = (l4 / 2L + 0.5D);
        l3 /= 2L;
      }
    }
    while ((l2 % 2L == 0L) && (l2 != 0L))
    {
      l2 /= 2L;
      l1 /= 2L;
    }
    l4 = l6;
    l3 = l5;
    if (paramDouble < 0.0D)
    {
      l4 = -l6;
      l3 = -l5;
    }
    long l7 = l3;
    l6 = l1;
    l5 = l2;
    if (l1 == 1L)
    {
      l7 = l3;
      l6 = l1;
      l5 = l2;
      if (l2 == 1L)
      {
        l5 = 0L;
        l6 = 0L;
        l7 = l3 + 1L;
      }
    }
    String str2;
    if ((paramInt == 8) || (paramInt == 7) || (paramInt == 6) || (paramInt == 5))
    {
      if (paramDouble < 0.0D) {}
      for (str1 = String.format("-%d' %d\"", new Object[] { Long.valueOf(Math.abs(l4)), Long.valueOf(Math.abs(l7)) });; str1 = String.format("%d' %d\"", new Object[] { Long.valueOf(Math.abs(l4)), Long.valueOf(Math.abs(l7)) }))
      {
        str2 = str1;
        if (l5 != 0L) {
          str2 = str1 + String.format(" %d/%d", new Object[] { Long.valueOf(l5), Long.valueOf(l6) });
        }
        return str2;
      }
    }
    l1 = Math.abs(l4) * 12L + Math.abs(l7);
    if (paramDouble < 0.0D) {}
    for (String str1 = String.format("-%d", new Object[] { Long.valueOf(l1) });; str1 = String.format("%d", new Object[] { Long.valueOf(l1) }))
    {
      str2 = str1;
      if (l5 != 0L) {
        str2 = str1 + String.format(" %d/%d", new Object[] { Long.valueOf(l5), Long.valueOf(l6) });
      }
      return str2 + " in";
    }
  }
  
  public String getAngle()
  {
    double d2 = Float.valueOf(this.m_Angle).floatValue() * 180.0F / 3.141592653589793D;
    double d3 = Float.valueOf(this.m_Angle).floatValue();
    String str;
    switch (this.m_AngleUnitBLE)
    {
    default: 
      return this.m_Angle + this.m_AngleUnit;
    case 6: 
      str = String.format("%.2f°", new Object[] { Double.valueOf(Math.round(d2 / 0.05D) * 0.05D) });
    }
    for (;;)
    {
      return str;
      double d1;
      if (d3 < -1.5707963267948966D) {
        d1 = 180.0D + d2 + 180.0D;
      }
      for (;;)
      {
        d1 = Math.round(d1 / 0.05D) * 0.05D;
        JQuery.i("", "angle calc: " + d1);
        str = String.format("%.2f°", new Object[] { Double.valueOf(d1) });
        break;
        d1 = d2;
        if (d3 < 0.0D) {
          d1 = d2 + 360.0D;
        }
      }
      if ((d3 > 3.141592653589793D) && (d3 < 3.141592653589793D)) {
        d1 = -90.0D + (d2 - 90.0D);
      }
      for (;;)
      {
        str = String.format("%.2f°", new Object[] { Double.valueOf(Math.round(d1 / 0.05D) * 0.05D) });
        break;
        d1 = d2;
        if (d3 < -1.5707963267948966D)
        {
          d1 = d2;
          if (d3 > -3.141592653589793D) {
            d1 = d2 + 180.0D;
          }
        }
      }
      str = String.format("%.2f°", new Object[] { Double.valueOf(Math.round(d2 / 0.05D) * 0.05D) });
      continue;
      str = String.format("%.2f%%", new Object[] { Double.valueOf(Math.tan(d3) * 100.0D) });
      continue;
      str = String.format("%.1fmm/m", new Object[] { Double.valueOf(1000.0D * d3) });
      continue;
      str = String.format("%.2fin/ft", new Object[] { Double.valueOf(d3 * 12.0D) });
    }
  }
  
  public String getDistance()
  {
    Object localObject1;
    if ((this.m_Distance != null) && (this.m_Distance.isEmpty()))
    {
      localObject1 = "...";
      label21:
      return (String)localObject1;
    }
    int m = 0;
    int n = 0;
    int i1 = this.m_DistanceUnitBLE;
    for (;;)
    {
      double d2;
      int i;
      double d1;
      int k;
      int j;
      try
      {
        d2 = Double.valueOf(this.m_Distance.replace(",", ".").replaceAll("[^0-9?!\\.]", "")).doubleValue();
        JQuery.d("DISTANCE: " + this.m_Distance + " UNIT: " + this.m_DistanceUnitBLE);
        if ((i1 < 100) || (i1 >= 1000)) {
          break label447;
        }
        m = 1;
        i1 -= 100;
        if ((i1 == 9) || (i1 == 13) || (i1 == 12) || (i1 == 11) || (i1 == 10) || (i1 == 4) || (i1 == 8) || (i1 == 7) || (i1 == 6) || (i1 == 5))
        {
          i = 4;
          d1 = d2 * 3.28083989501312D;
          k = n;
          j = m;
          switch (i)
          {
          default: 
            localObject1 = String.format("%.3fm", new Object[] { Double.valueOf(d1) });
            Object localObject2 = localObject1;
            if (j != 0) {
              localObject2 = (String)localObject1 + "²";
            }
            localObject1 = localObject2;
            if (k == 0) {
              break label21;
            }
            return (String)localObject2 + "³";
          }
        }
      }
      catch (NumberFormatException localNumberFormatException)
      {
        JQuery.e(localNumberFormatException);
        return this.m_Distance;
      }
      if (i1 == 3)
      {
        i = 0;
        d1 = d2;
        j = m;
        k = n;
      }
      else
      {
        d1 = d2;
        j = m;
        k = n;
        i = i1;
        if (i1 == 14)
        {
          d1 = 3.28083989501312D * d2 / 3.0D;
          j = m;
          k = n;
          i = i1;
          continue;
          label447:
          d1 = d2;
          j = m;
          k = n;
          i = i1;
          if (i1 >= 1000)
          {
            n = 1;
            i1 -= 1000;
            if ((i1 == 9) || (i1 == 13) || (i1 == 12) || (i1 == 11) || (i1 == 10) || (i1 == 4) || (i1 == 8) || (i1 == 7) || (i1 == 6) || (i1 == 5))
            {
              i = 4;
              d1 = 3.28083989501312D * d2 * 3.28083989501312D;
              j = m;
              k = n;
            }
            else if (i1 == 3)
            {
              i = 0;
              d1 = d2;
              j = m;
              k = n;
            }
            else
            {
              d1 = d2;
              j = m;
              k = n;
              i = i1;
              if (i1 == 14)
              {
                d1 = 3.28083989501312D * d2 / 3.0D * 3.28083989501312D / 3.0D;
                j = m;
                k = n;
                i = i1;
                continue;
                String str = String.format("%.3fm", new Object[] { Double.valueOf(1.0E-5D + d1) });
                continue;
                str = String.format("%.2fm", new Object[] { Double.valueOf(d1) });
                continue;
                str = String.format("%.1fmm", new Object[] { Double.valueOf(1000.0D * d1) });
                continue;
                str = String.format("%.4fm", new Object[] { Double.valueOf(d1) });
                continue;
                str = String.format("%.2fft", new Object[] { Double.valueOf(3.28083989501312D * d1) });
                continue;
                str = getFractioned(1000.0D * d1, i);
                continue;
                str = String.format("%.2f in", new Object[] { Double.valueOf(39.370079040527344D * d1) });
                continue;
                str = getFractioned(1000.0D * d1, i);
                continue;
                str = String.format("%.3fyd", new Object[] { Double.valueOf(3.28083989501312D * d1 / 3.0D) });
              }
            }
          }
        }
      }
    }
  }
  
  public String getType()
  {
    JQuery.e("M_DISTANCEUNIT: " + this.m_DistanceUnit + " BLE: " + this.m_DistanceUnitBLE);
    if ((this.m_DistanceUnit.endsWith("²")) || ((this.m_DistanceUnitBLE >= 100) && (this.m_DistanceUnitBLE < 1000))) {
      return "Area";
    }
    if ((this.m_DistanceUnit.endsWith("³")) || (this.m_DistanceUnitBLE >= 1000)) {
      return "Volume";
    }
    return "Inclination";
  }
  
  public boolean isSpecial()
  {
    if ((this.m_DistanceUnit.endsWith("²")) || (this.m_DistanceUnit.endsWith("³")) || (this.m_DistanceUnit.endsWith("°")) || (this.m_DistanceUnitBLE >= 100)) {}
    while ((this.m_Distance.equals("")) && (!this.m_Angle.equals(""))) {
      return true;
    }
    return false;
  }
}


