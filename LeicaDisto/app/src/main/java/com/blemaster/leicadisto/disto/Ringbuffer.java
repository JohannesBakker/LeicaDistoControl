package com.blemaster.leicadisto.disto;

public class Ringbuffer
{
  private int N = 0;
  private byte[] a;
  private int first = 0;
  private int last = 0;
  
  public Ringbuffer(int paramInt)
  {
    this.a = new byte[paramInt];
  }
  
  public boolean contains(byte paramByte)
  {
    if (isEmpty()) {}
    for (;;)
    {
      for (int i = first; i != last; i = (i + 1) % this.a.length) {
        if (this.a[i] == paramByte) {
          return true;
        }
      }

      return false;
    }
  }
  
  public byte dequeue()
  {
    if (isEmpty()) {
      throw new RuntimeException("Ring buffer underflow");
    }
    byte b = this.a[this.first];
    this.N -= 1;
    this.first = ((this.first + 1) % this.a.length);
    return b;
  }
  
  public int dequeueUntil(byte[] paramArrayOfByte)
  {
    int k = 0;
    j = 0;
    try
    {
      int n = peek();
      int i = 0;
      for (;;)
      {
        int m = k;
        j = i;
        if (i < paramArrayOfByte.length)
        {
          if (paramArrayOfByte[i] == n) {
            m = 1;
          }
        }
        else
        {
          if (m == 0)
          {
            j = i;
            dequeue();
          }
          j = i;
          k = m;
          if (m == 0) {
            break;
          }
          return i;
        }
        i += 1;
      }
      return j;
    }
    catch (RuntimeException paramArrayOfByte) {}
  }
  
  public void enqueue(byte paramByte)
  {
    if (this.N == this.a.length) {
      throw new RuntimeException("Ring buffer overflow");
    }
    this.a[this.last] = paramByte;
    this.last = ((this.last + 1) % this.a.length);
    this.N += 1;
  }
  
  public int getLine(byte[] paramArrayOfByte, byte paramByte)
  {
    int j = paramArrayOfByte.length;
    int i = 0;
    for (;;)
    {
      if (i < j) {}
      try
      {
        paramArrayOfByte[i] = dequeue();
        byte b = paramArrayOfByte[i];
        if (b == paramByte) {
          return i;
        }
      }
      catch (RuntimeException paramArrayOfByte)
      {
        return i;
      }
      i += 1;
    }
  }
  
  public void insert(byte[] paramArrayOfByte, int paramInt)
  {
    int i = 0;
    while (i < paramInt)
    {
      enqueue(paramArrayOfByte[i]);
      i += 1;
    }
  }
  
  public boolean isEmpty()
  {
    return this.N == 0;
  }
  
  public byte peek()
  {
    if (isEmpty()) {
      return 0;
    }
    return this.a[this.first];
  }
}


/* Location:              D:\Elance\000_01_PrepareTask\DecompileAPK\Tools\jd-gui-windows-1.4.0\leca_classes-dex2jar.jar!\de\ffuf\leica\sketchlibrary\disto\Ringbuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */