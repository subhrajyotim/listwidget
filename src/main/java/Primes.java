public class Primes
{
 public static void main(String[] args)
 {
  for (int i = 1; i < 1000; i++)
  {
   boolean isPrime = true;
   for (int j = 2; j < 34; j++)
   {
    if (i % j == 0)
    {
     if (isPrime = true)
     {
      System.out.println(i);
     }
     else isPrime = false;
    }
   }
  }
 }

}