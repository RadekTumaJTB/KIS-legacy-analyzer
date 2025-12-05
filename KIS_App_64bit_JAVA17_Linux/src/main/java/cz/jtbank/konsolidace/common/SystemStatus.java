package cz.jtbank.konsolidace.common;

import java.util.*;
import java.text.SimpleDateFormat;

public class SystemStatus
{
  private SystemStatus()
  {
  }

  private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy");
  private static SystemStatus instance;
  private List<String> status = new ArrayList<>();
  
  public static SystemStatus getInstance() 
  {
    if(instance == null) instance = new SystemStatus();
    return instance;
  }
  
  public void reset() 
  {
    status.clear();
  }
  
  public void setStatus(String statusText) 
  {
    status.add(sdf.format(new Date())+" "+statusText);
  }
  
  public List<String> getStatus()
  {
    return status;
  }
  
  public String getStatusHtml()
  {
    StringBuffer buf = new StringBuffer();
    Iterator<String> iter = status.iterator();
    boolean odd = true;
    if(!status.isEmpty())
    {
      buf.append("<table class='clsTable' width='100%'>\n");
    }
    while(iter.hasNext())
    {
      String statusText = iter.next();
      odd = !odd;
      buf.append("<tr class='clsTableRow'>\n");
      buf.append("<td class='clsCell"+(odd?"":"Light")+"Orange'>\n");
      if(iter.hasNext()) buf.append(statusText);
      else buf.append("<b>"+statusText+"</b>");
      buf.append("\n</td>\n</tr>\n");
    }
    if(!status.isEmpty()) 
    {
      buf.append("</table>\n");
    }
    return buf.toString();
  }
}