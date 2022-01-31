public class Calkowanie
{
    public static void main(String[] args)
    {
        double suma = 0;

        M_prostokatow thread1 = new M_prostokatow();
        M_Simpsona thread2 = new M_Simpsona();
        M_trapezow thread3 = new M_trapezow();

        thread1.start();
        thread2.start();
        thread3.start();

        try
        {
            thread1.join();
            thread2.join();
            thread3.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        suma += thread1.integral;
        suma += thread2.integral;
        suma += thread3.integral;

        System.out.println("Suma calek = " + suma);
    }
}

class M_prostokatow extends Thread
{
    public double integral = 1;
    public double a = 2;
    public double b = 3;
    public double h = 0;
    public int n = 3;

    private static double value(double x)
    {
        return Math.pow(x, 2)+3*x+9;
    }

    @Override
    public void run()
    {
        h = (b - a) / n;

        for (int i=1; i<=n; i++)
        {
            integral += value(a + i * h);
        }

        integral *= h;
    }
}


class M_Simpsona extends Thread
{
    public double integral = 0;
    public double a = 5;
    public double b = 7;
    public double h, sum_ti = 0, xi;
    public int n = 4;

    private static double value(double x)
    {
        return Math.pow(x, 2)+3*x+9;
    }

    @Override
    public void run()
    {
        h = (b - a) / n;

        for (int i=1; i<n; i++)
        {
            xi = a + i*h;
            sum_ti += value(xi - h / 2);
            integral += value(xi);
        }

        sum_ti += value(b - h / 2);
        integral = (h/6) * (value(a) + value(b) + 2*integral + 4*sum_ti);
    }
}


class M_trapezow extends Thread
{
    public double integral = 0;
    public double a = 1;
    public double b = 3;
    public int n = 4;

    private static double value(double x)
    {
        return Math.pow(x, 2)+3*x+9;
    }

    @Override
    public void run()
    {
        double h;

        h = (b-a)/n;

        for (int i=1; i<n; i++){
            integral += value(a + i * h);
        }


        integral += (value(a) + value(b))/2;
        integral *= h;
    }
}