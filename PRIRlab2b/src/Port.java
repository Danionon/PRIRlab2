import java.util.Random;

public class Port
{
    static int liczba_dostepnych = 10;
    static int liczba_statkow = 100;
    static int liczba_zajetych;

    static int w_doku = 1;
    static int ROZPOCZNIJ = 2;
    static int TANKOWANIE = 3;
    static int do_doku = 4;
    static int NIEPOWODZENIE = 5;

    Port(int doki, int statki)
    {
        doki = liczba_dostepnych;
        statki = liczba_statkow;
        liczba_zajetych = 0;
    }

    synchronized int start(int numer)
    {
        System.out.println("Statek nr "+numer+" może dokować");
        liczba_zajetych--;
        return ROZPOCZNIJ;
    }

    synchronized int tankowanie()
    {
        try
        {
            Thread.currentThread().sleep(1000);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        if(liczba_zajetych < liczba_dostepnych)
        {
            liczba_zajetych++;
            System.out.println("Zapraszamy do doku nr "+liczba_zajetych);
            return w_doku;
        }
        else
        {
            return do_doku;
        }
    }

    synchronized void wydokowanie()
    {
        liczba_dostepnych--;
        System.out.println("Statek opuścił dok");
    }

    public static void main(String[] args)
    {
        Port s = new Port(liczba_dostepnych, liczba_statkow);
        for(int i = 0; i< liczba_statkow; i++)
        {
            new Sttek(s,i,400).start();
        }
    }
}



 class Sttek extends Thread
{
    static int PALIWO = 1000;
    static int ZDENERWOWANIE = 200;

    static int W_DOKU = 1;
    static int ROZPOCZNIJ = 2;
    static int TANKOWANIE = 3;
    static int ZADOKOWANY = 4;
    static int NIEPOWODZENIE = 5;

    int index;
    int zdenerwowanie;
    int stanTankowania;

    Port s;
    Random random;

    public Sttek(Port s, int index, int patiency_level)
    {
        this.s = s;
        random = new Random();
        this.index = index;
        this.zdenerwowanie = patiency_level;
        this.stanTankowania = TANKOWANIE;
    }

    @Override
    public void run()
    {
        while(true)
        {
            if(stanTankowania == W_DOKU)
            {
                if(random.nextInt(5)==1)
                {
                    stanTankowania = ROZPOCZNIJ;
                    zdenerwowanie = PALIWO;
                    System.out.println("Statek nr "+index+" chcę zakończyć tankowanie");
                    stanTankowania = s.start(index);
                }
                else
                {
                    System.out.println("Statek nr "+index+" czeka w kolejce");
                }
            }
            else if(stanTankowania == ROZPOCZNIJ)
            {
                System.out.println("Statek nr "+index+" rozpoczą tankowanie");
                stanTankowania = TANKOWANIE;
            }
            else if(stanTankowania == TANKOWANIE)
            {
                zdenerwowanie -=random.nextInt(500);

                if(zdenerwowanie <= ZDENERWOWANIE){
                    stanTankowania = ZADOKOWANY;
                }
                else try
                {
                    sleep(random.nextInt(1000));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else if(stanTankowania == ZADOKOWANY)
            {
                System.out.println("Kapitan statku nr "+index+" chcę zakończyć tankowanie poziom cierpliwosci: "+ zdenerwowanie);
                stanTankowania =s.tankowanie();
                if(stanTankowania == ZADOKOWANY)
                {
                    zdenerwowanie -=random.nextInt(500);
                    System.out.println("Kapitan statku "+index+" sie niecierpliwi");
                    if(zdenerwowanie <=0) stanTankowania = NIEPOWODZENIE;
                }
            }
            else if(stanTankowania == NIEPOWODZENIE)
            {
                System.out.println("Statek nr "+index+" opuszcza dok");
                s.wydokowanie();
            }
        }
    }
}