package cz.jtbank.konsolidace.excel;

import java.util.*;

public class BeanListSkupina 
{
  public ArrayList list;

  public BeanListSkupina()
  {
    list = new ArrayList();
  }
  
  public void sortList() 
  {
    Collections.sort(list);
    Iterator iter = list.iterator();
    while(iter.hasNext()) 
    {
      Skupina skup = (Skupina) iter.next();
      skup.sortList();
    }
  }
}

class Skupina implements Comparable
{
  public ArrayList protistrany;
  public double suma;
  public double sumaNeBanka;
  public double sumaCelkom;
  public String nazev;
  public double cerpat;
  public double cerpatNeBanka;
  public boolean notSkupina = false;
  public String banka;
  
  public Skupina() 
  {
    protistrany =  new ArrayList();
  }
  
  /*
  public int compareTo(Object obj) 
  {
    if(((Skupina)obj).suma == suma) return 0;
    else if(((Skupina)obj).suma > suma) return 1;
    else return -1;
  }
*/
public int compareTo(Object obj) 
  {
  Skupina skupina = (Skupina)obj;
  
    if(skupina.suma + skupina.sumaNeBanka == this.suma + this.sumaNeBanka) return 0;
    else if(skupina.suma + skupina.sumaNeBanka > this.suma + this.sumaNeBanka) return 1;
    else return -1;
  }
  public void sortList() 
  {
    Collections.sort(protistrany);
  }
}

class Protistrana implements Comparable 
{
  public ArrayList spolecnosti;
  public double suma;
  public String nazev;
  public String ico;
  public double cerpat;
  public boolean notProti = false;
  public String banka;

  public Protistrana() 
  {
    spolecnosti = new ArrayList();
  }
  
  public int compareTo(Object obj) 
  {
    if(((Protistrana)obj).suma == suma) return 0;
    else if(((Protistrana)obj).suma > suma) return 1;
    else return -1;
  }
}

class Spolecnost 
{
  public ArrayList radky;
  public String nazev;

  public Spolecnost() 
  {
    radky = new ArrayList();
  }
}

class Radek 
{
  public String popis;
  public double castkaMena;
  public String mena;
  public double castkaCzk;
  public double cistaAng;

  public Radek() 
  {
  }
}