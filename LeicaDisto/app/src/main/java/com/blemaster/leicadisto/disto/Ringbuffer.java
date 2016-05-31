package com.blemaster.leicadisto.disto;

public class Ringbuffer
{
	private int N = 0;
	private byte[] a;
	private int first = 0;
	private int last = 0;

	public Ringbuffer(int paramInt)
	{
		a = new byte[paramInt];
	}


	public boolean contains(byte paramByte)
	{
		if (isEmpty()) {}

        for (int i = first; i != last; i = (i + 1) % a.length) {
            if (a[i] == paramByte) {
                return true;
            }
        }
        return false;
	}
  
	public byte dequeue()
	{
		if (isEmpty()) {
			throw new RuntimeException("Ring buffer underflow");
		}
	
		byte b = a[first];
	
		N -= 1;
		first = ((first + 1) % a.length);
		return b;
	}

	public int dequeueUntil(byte[] paramArrayOfByte)
	{
		int k = 0;
		int j = 0;

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
		catch (Exception e) {
			e.printStackTrace();
		}

		return j;
	}

	public void enqueue(byte paramByte)
	{
		if (N == a.length) {
			throw new RuntimeException("Ring buffer overflow");
		}
		a[last] = paramByte;
		last = ((last + 1) % a.length);
		N += 1;
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
			catch (Exception e)
			{
				e.printStackTrace();
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
		return (N == 0);
	}

	public byte peek()
	{
		if (isEmpty()) {
			return 0;
		}
		return a[first];
	}
}


