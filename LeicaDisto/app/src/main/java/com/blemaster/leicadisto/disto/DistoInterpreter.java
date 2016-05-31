package com.blemaster.leicadisto.disto;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

public class DistoInterpreter
{
  private static final String[][] sDistoCommandTable = { { "a", "a", "a", "a" }, { "b", "b", "b", "b" }, { "g", "g", "g", "g" }, { null, null, "gi", "gi" }, { null, null, "iv", "iv" }, { "N00N", "N00N", "N00N", "N00N" }, { "N02N", "N02N", "N02N", "N02N" }, { "o", "o", "o", "o" }, { "p", "p", "p", "p" }, { "cfm", "cfm", "cfm", "cfm" } };
  public final int FeetInchSpaceInchFract = 1;
  public final int FeetInchTabInchFract = 3;
  public final int FeetTabInchNoFract = 2;
  int mDistoDeviceNbr = 0;
  int mDistoType = 2;
  public int m_ErrorCode = 0;
  public int m_ImperialMode = 1;
  public int m_MetricConvert = 0;
  public String m_ResultString;
  public String m_ResultUnitString;
  public int m_SendConfirmation = 0;
  public boolean m_bResultMetric = false;
  public boolean m_bResultUseEnter = false;
  public boolean m_bResultUseTab = false;
  public boolean m_bResultUseUnit = true;
  public int m_eResultType = 0;
  public int m_nMetricResultDezimals = 0;
  
  private String FormatImperial(int paramInt1, int paramInt2, int paramInt3)
  {
    String str = String.format("%d %d/%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    switch (this.m_ImperialMode)
    {
    default: 
      return str;
    case 1: 
      return String.format("%d %d/%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
    case 2: 
      return String.format("%d\t", new Object[] { Integer.valueOf(paramInt1) });
    }
    return String.format("%d\t%d/%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3) });
  }
  
  private String FormatImperial(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    String str = new String();
    switch (this.m_ImperialMode)
    {
    default: 
      return str;
    case 1: 
      return String.format("%d.%02d %d/%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4) });
    case 2: 
      return String.format("%d\t%02d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    }
    return String.format("%d.%02d\t%d/%d", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2), Integer.valueOf(paramInt3), Integer.valueOf(paramInt4) });
  }
  
  private int getAngleUnit(int paramInt)
  {
    switch (paramInt)
    {
    default: 
      return 0;
    case 0: 
      return 13;
    case 1: 
      return 14;
    case 2: 
      return 15;
    case 3: 
      return 16;
    case 4: 
      return 17;
    }
    return 18;
  }
  
  private int getUnit(int paramInt)
  {
    int i = 0;
    switch (paramInt)
    {
    default: 
      paramInt = i;
    case 0: 
    case 1: 
    case 2: 
    case 3: 
    case 4: 
      do
      {
        return paramInt;
        return 3;
        return 4;
        return 9;
        return 10;
        paramInt = 3;
      } while ((1 != this.mDistoType) && (2 != this.mDistoType) && (3 != this.mDistoType));
      return 2;
    case 5: 
      return 11;
    case 6: 
      return 5;
    case 7: 
      return 12;
    case 8: 
      return 7;
    }
    return 8;
  }
  
  public int AnalyzeAndInterpretGsiItem(String paramString)
  {
    Log.d("DISTOtransfer", "AnalyzeAndInterpretGsiItem " + paramString);
    Object localObject = new GsiItemAccess(paramString);
    this.m_SendConfirmation = 0;
    this.m_eResultType = 0;
    this.m_ErrorCode = 0;
    if (((GsiItemAccess)localObject).isAck()) {
      return 0;
    }
    if (((GsiItemAccess)localObject).isErrorCode())
    {
      this.m_ErrorCode = ((GsiItemAccess)localObject).getErrorCode();
      this.m_eResultType = 3;
      return 1;
    }
    if (((GsiItemAccess)localObject).isValidWI())
    {
      int j = ((GsiItemAccess)localObject).getWI();
      switch (j)
      {
      }
      for (;;)
      {
        return 1;
        this.m_ResultString = ((GsiItemAccess)localObject).getDez1();
        this.m_ResultUnitString = "";
        this.m_SendConfirmation += 1;
        continue;
        this.mDistoDeviceNbr = Integer.getInteger(((GsiItemAccess)localObject).getDez1()).intValue();
        this.m_SendConfirmation += 1;
        continue;
        paramString = ((GsiItemAccess)localObject).getDez1().substring(0, 5);
        localObject = ((GsiItemAccess)localObject).getDez2();
        int i = (int)Double.parseDouble(paramString);
        j = (int)Double.parseDouble((String)localObject);
        if (i == 73)
        {
          this.mDistoType = 0;
        }
        else if (i == 74)
        {
          this.mDistoType = 1;
        }
        else if (i == 75)
        {
          this.mDistoType = 2;
        }
        else if (i == 76)
        {
          this.mDistoType = 3;
          continue;
          this.m_ResultString = ((GsiItemAccess)localObject).getDez1();
          this.m_SendConfirmation += 1;
          continue;
          int k = getUnit(((GsiItemAccess)localObject).getRawUnit());
          this.m_eResultType = 1;
          this.m_SendConfirmation += 1;
          paramString = ((GsiItemAccess)localObject).getDez1();
          i = 1;
          if (j == 314) {
            i = 2;
          }
          if (j == 315) {
            i = 3;
          }
          double d = Double.parseDouble(paramString);
          j = (int)d;
          int m;
          int n;
          switch (k)
          {
          case 1: 
          case 13: 
          case 14: 
          case 15: 
          case 16: 
          case 17: 
          case 18: 
          default: 
            break;
          case 0: 
            this.m_ResultString = String.format("%.03f", new Object[] { Double.valueOf(d / 1000.0D) });
            this.m_ResultUnitString = "";
            this.m_nMetricResultDezimals = 3;
            break;
          case 3: 
            d /= 1000.0D;
            this.m_ResultUnitString = "m";
            this.m_bResultMetric = true;
            switch (this.m_MetricConvert)
            {
            default: 
              this.m_ResultString = String.format("%.03f", new Object[] { Double.valueOf(d) });
              this.m_nMetricResultDezimals = 3;
            }
            for (;;)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                this.m_ResultUnitString += "²";
                break;
                this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d * 1000.0D) });
                this.m_ResultUnitString = "mm";
                this.m_nMetricResultDezimals = 1;
                continue;
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d * 100.0D) });
                this.m_ResultUnitString = "cm";
                this.m_nMetricResultDezimals = 2;
              }
            }
            this.m_ResultUnitString += "³";
            break;
          case 2: 
            this.m_ResultUnitString = "mm";
            this.m_bResultMetric = true;
            switch (this.m_MetricConvert)
            {
            default: 
              this.m_ResultString = String.format("%.0f", new Object[] { Double.valueOf(d) });
              this.m_nMetricResultDezimals = 1;
            }
            for (;;)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                this.m_ResultUnitString += "²";
                break;
                this.m_ResultString = String.format("%.04f", new Object[] { Double.valueOf(d / 1000.0D) });
                this.m_nMetricResultDezimals = 4;
                this.m_ResultUnitString = "m";
                continue;
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d / 10.0D) });
                this.m_nMetricResultDezimals = 2;
                this.m_ResultUnitString = "cm";
              }
            }
            this.m_ResultUnitString += "³";
            break;
          case 11: 
            d /= 100.0D;
            this.m_ResultUnitString = "cm";
            this.m_bResultMetric = true;
            switch (this.m_MetricConvert)
            {
            default: 
              this.m_ResultString = String.format("%.2f", new Object[] { Double.valueOf(d) });
              this.m_nMetricResultDezimals = 2;
            }
            for (;;)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                this.m_ResultUnitString += "²";
                break;
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d * 100.0D) });
                this.m_nMetricResultDezimals = 2;
                this.m_ResultUnitString = "cm";
                continue;
                this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d * 1000.0D) });
                this.m_nMetricResultDezimals = 1;
                this.m_ResultUnitString = "mm";
              }
            }
            this.m_ResultUnitString += "³";
            break;
          case 5: 
            d /= 10000.0D;
            this.m_ResultUnitString = "m";
            this.m_bResultMetric = true;
            switch (this.m_MetricConvert)
            {
            default: 
              this.m_ResultString = String.format("%.04f", new Object[] { Double.valueOf(d) });
              this.m_nMetricResultDezimals = 4;
            }
            for (;;)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                this.m_ResultUnitString += "²";
                break;
                this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d * 1000.0D) });
                this.m_nMetricResultDezimals = 1;
                this.m_ResultUnitString = "mm";
                continue;
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d * 100.0D) });
                this.m_nMetricResultDezimals = 2;
                this.m_ResultUnitString = "cm";
              }
            }
            this.m_ResultUnitString += "³";
            break;
          case 4: 
            this.m_ResultUnitString = "ft";
            this.m_bResultMetric = true;
            if ((2 == this.mDistoType) || (3 == this.mDistoType)) {
              this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d / 100.0D) });
            }
            for (;;)
            {
              switch (i)
              {
              default: 
                break;
              case 2: 
                this.m_ResultUnitString += "²";
                break;
                if (i == 3) {
                  this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d / 10.0D) });
                } else {
                  this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d / 100.0D) });
                }
                break;
              }
            }
            this.m_ResultUnitString += "³";
            break;
          case 6: 
            k = j % 100;
            m = (j % 10000 - k) / 100;
            n = (j - m * 100 - k) / 10000;
            j = 16;
            if (k % 8 == 0)
            {
              i = k / 8;
              j = 2;
            }
            for (;;)
            {
              this.m_ResultString = FormatImperial(n, m, i, j);
              this.m_ResultUnitString = "";
              break;
              if (k % 4 == 0)
              {
                i = k / 4;
                j = 4;
              }
              else
              {
                i = k;
                if (k % 2 == 0)
                {
                  i = k / 2;
                  j = 8;
                }
              }
            }
          case 7: 
            k = j % 100;
            m = (j % 10000 - k) / 100;
            n = (j - m * 100 - k) / 10000;
            j = 16;
            if (k % 8 == 0)
            {
              i = k / 8;
              j = 2;
            }
            for (;;)
            {
              this.m_ResultString = FormatImperial(n, m, i, j);
              this.m_ResultUnitString = "";
              break;
              if (k % 4 == 0)
              {
                i = k / 4;
                j = 4;
              }
              else
              {
                i = k;
                if (k % 2 == 0)
                {
                  i = k / 2;
                  j = 8;
                }
              }
            }
          case 8: 
            k = j % 100;
            m = (j % 10000 - k) / 100;
            n = (j - m * 100 - k) / 10000;
            j = 32;
            if (k % 16 == 0)
            {
              i = k / 16;
              j = 2;
            }
            for (;;)
            {
              this.m_ResultString = FormatImperial(n, m, i, j);
              this.m_ResultUnitString = "";
              break;
              if (k % 8 == 0)
              {
                i = k / 8;
                j = 4;
              }
              else if (k % 4 == 0)
              {
                i = k / 4;
                j = 8;
              }
              else
              {
                i = k;
                if (k % 2 == 0)
                {
                  i = k / 2;
                  j = 16;
                }
              }
            }
          case 9: 
            this.m_bResultMetric = true;
            this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d / 10.0D) });
            this.m_ResultUnitString = "in";
            switch (i)
            {
            default: 
              break;
            case 2: 
              this.m_ResultUnitString += "²";
              break;
            case 3: 
              this.m_ResultUnitString += "³";
            }
            break;
          case 19: 
            k = j % 100;
            m = (j - k) / 100;
            j = 16;
            if (k % 8 == 0)
            {
              i = k / 8;
              j = 2;
            }
            for (;;)
            {
              this.m_ResultString = FormatImperial(m, i, j);
              this.m_ResultUnitString = "";
              break;
              if (k % 4 == 0)
              {
                i = k / 4;
                j = 4;
              }
              else
              {
                i = k;
                if (k % 2 == 0)
                {
                  i = k / 2;
                  j = 8;
                }
              }
            }
          case 10: 
            k = j % 100;
            m = (j - k) / 100;
            j = 32;
            if (k % 16 == 0)
            {
              i = k / 16;
              j = 2;
            }
            for (;;)
            {
              this.m_ResultString = FormatImperial(m, i, j);
              this.m_ResultUnitString = "";
              break;
              if (k % 8 == 0)
              {
                i = k / 8;
                j = 4;
              }
              else if (k % 4 == 0)
              {
                i = k / 4;
                j = 8;
              }
              else
              {
                i = k;
                if (k % 2 == 0)
                {
                  i = k / 2;
                  j = 16;
                }
              }
            }
          case 12: 
            this.m_bResultMetric = true;
            this.m_ResultString = String.format("%.03f", new Object[] { Double.valueOf(d / 1000.0D) });
            this.m_ResultUnitString = "yd";
            switch (i)
            {
            default: 
              break;
            case 2: 
              this.m_ResultUnitString += "°";
              break;
            case 3: 
              this.m_ResultUnitString += "°";
              continue;
              this.m_eResultType = 2;
              this.m_SendConfirmation += 1;
              i = getAngleUnit(((GsiItemAccess)localObject).getRawUnit());
              d = Double.parseDouble(((GsiItemAccess)localObject).getDez1());
              switch (i)
              {
              default: 
                break;
              case 13: 
              case 14: 
              case 15: 
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d / 100.0D) });
                this.m_ResultUnitString = "°";
                this.m_nMetricResultDezimals = 2;
                break;
              case 16: 
                this.m_ResultString = String.format("%.02f", new Object[] { Double.valueOf(d / 100.0D) });
                this.m_ResultUnitString = "%";
                this.m_nMetricResultDezimals = 2;
                break;
              case 17: 
                this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d / 10.0D) });
                this.m_ResultUnitString = "mm/m";
                this.m_nMetricResultDezimals = 1;
                break;
              case 18: 
                this.m_ResultString = String.format("%.01f", new Object[] { Double.valueOf(d / 10.0D) });
                this.m_ResultUnitString = "in/ft";
                this.m_nMetricResultDezimals = 1;
                continue;
                this.m_SendConfirmation += 1;
                this.m_ResultString = ((GsiItemAccess)localObject).getDez1();
                continue;
                this.m_SendConfirmation += 1;
                this.m_ResultString = ((GsiItemAccess)localObject).getDez1();
                continue;
                this.m_SendConfirmation += 1;
                this.m_ResultString = ((GsiItemAccess)localObject).getDez1();
                this.m_ResultString.trim();
                continue;
                this.m_SendConfirmation += 1;
                this.m_eResultType = 4;
                this.m_ResultString = ((GsiItemAccess)localObject).getDez2();
                Log.d("DistoKeyCode", this.m_ResultString);
              }
              break;
            }
            break;
          }
        }
      }
    }
    return 0;
  }
  
  public void UpdateSettings(SharedPreferences paramSharedPreferences, Resources paramResources)
  {
    this.m_bResultUseTab = paramSharedPreferences.getBoolean("distoTab", this.m_bResultUseTab);
    this.m_bResultUseEnter = paramSharedPreferences.getBoolean("distoEnter", this.m_bResultUseEnter);
    this.m_bResultUseUnit = paramSharedPreferences.getBoolean("distoUnit", this.m_bResultUseUnit);
    switch ((int)Double.parseDouble(paramSharedPreferences.getString("distoMetric", "0")))
    {
    }
    for (;;)
    {
      switch ((int)Double.parseDouble(paramSharedPreferences.getString("distoImperial", "0")))
      {
      default: 
        return;
        this.m_MetricConvert = 0;
        continue;
        this.m_MetricConvert = 2;
        continue;
        this.m_MetricConvert = 11;
        continue;
        this.m_MetricConvert = 3;
      }
    }
    this.m_ImperialMode = 1;
    return;
    this.m_ImperialMode = 2;
    return;
    this.m_ImperialMode = 3;
  }
  
  public String getDistoCmd(int paramInt)
  {
    return sDistoCommandTable[paramInt][this.mDistoType];
  }
  
  public boolean hasDistoCmd(int paramInt)
  {
    return sDistoCommandTable[paramInt][this.mDistoType] != null;
  }
  
  private class GsiItemAccess
  {
    String m_GsiItem;
    
    public GsiItemAccess(String paramString)
    {
      this.m_GsiItem = paramString;
    }
    
    public String getDez1()
    {
      String str = "";
      if (this.m_GsiItem.length() >= 8) {
        str = this.m_GsiItem.substring(6).replace("\n", "").replace("\r", "").replace(" ", "");
      }
      return str;
    }
    
    public String getDez2()
    {
      String str = "";
      if (this.m_GsiItem.length() >= 8) {
        str = this.m_GsiItem.substring(11).replace("\n", "").replace("\r", "").replace(" ", "");
      }
      return str;
    }
    
    public int getErrorCode()
    {
      try
      {
        double d = Double.parseDouble(this.m_GsiItem.substring(2));
        return (int)d;
      }
      catch (NumberFormatException localNumberFormatException) {}
      return 0;
    }
    
    public int getRawUnit()
    {
      int i = 0;
      if (this.m_GsiItem.length() >= 8) {}
      try
      {
        i = Integer.parseInt(this.m_GsiItem.substring(5, 6));
        return i;
      }
      catch (NumberFormatException localNumberFormatException) {}
      return 0;
    }
    
    public int getWI()
    {
      int i = 0;
      if (this.m_GsiItem.length() >= 8) {}
      try
      {
        i = Integer.parseInt(this.m_GsiItem.substring(0, 4).replace(".", ""));
        return i;
      }
      catch (NumberFormatException localNumberFormatException) {}
      return 0;
    }
    
    public boolean isAck()
    {
      return this.m_GsiItem.startsWith("?");
    }
    
    public boolean isErrorCode()
    {
      return this.m_GsiItem.startsWith("@");
    }
    
    public boolean isValidWI()
    {
      return this.m_GsiItem.length() >= 8;
    }
  }
}
