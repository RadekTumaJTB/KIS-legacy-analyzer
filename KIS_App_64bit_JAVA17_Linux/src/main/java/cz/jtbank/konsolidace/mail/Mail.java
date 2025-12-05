package cz.jtbank.konsolidace.mail;

import cz.jtbank.konsolidace.common.*;
import cz.jtbank.konsolidace.excel.*;
import java.text.*;
import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;
import oracle.jbo.ApplicationModule;
import oracle.jbo.ViewObject;
import oracle.jbo.Row;
import oracle.jbo.domain.Number;
import oracle.jbo.client.Configuration;
import java.util.*;

import org.apache.log4j.*;
import cz.jtbank.konsolidace.common.Logging;

/*package cz.jtbank.konsolidace.mail;

import cz.jtbank.konsolidace.common.Constants;
import cz.jtbank.konsolidace.common.Logging;
import cz.jtbank.konsolidace.common.Utils;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import oracle.jbo.ApplicationModule;
import oracle.jbo.Row;
import oracle.jbo.ViewObject;
import oracle.jbo.client.Configuration;
import oracle.jbo.domain.Number;

import org.apache.log4j.Logger;
*/

public class Mail 
{
  static Logger logger = Logger.getLogger(Mail.class);
  static { logger.addAppender(Logging.getAppender(Logging.LOG_EMAIL)); }

  /* private static final String HOST ="smtp.pa.jtfg.com" ; "czpae00023.pa.jtfg.com" "192.168.2.9" cartman.jtbank.cz";*/
  private static final String HOST ="smtp-pa.jtfg.com" ;   /* 11/2022*/
  private static final String ENCODING = "Windows-1250";
  private static String kis_sender = "kis@jtbank.cz";
  //private static final String kis_sender = "kis32@jtbank.cz";
  
  private static Properties props = System.getProperties();
  static 
  {
    props.put("mail.smtp.host", HOST);
    if ( !"tweek".equalsIgnoreCase( Constants.getHostName() )){
        kis_sender = "kis"+Constants.getHostName()+"@jtbank.cz";
    }
  }
  
  private static final String VICE_INFO = "\n\nVďż˝ce informacďż˝ najdete v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                          "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp\n";
  private static final String VICE_INFO_DOC = "\n\nSchvďż˝lit je mďż˝ďż˝ete v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                          "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocPrijate.jsp\n";

  private static final String VICE_INFO_TYPIZ = "\nU jednotlivďż˝ch SL jsou zobrazenďż˝ jenom ďż˝ďż˝dky pro kterďż˝ jste byl vybrďż˝n jako schvalovatel."+
                                            "\n\nVďż˝ce informacďż˝ najdete v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                            "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocPrijate.jsp?filterSchval=21\n";

  private static final String VICE_INFO_DOC_ZAD = "\n\nEditovat je mďż˝ďż˝ete v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                          "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocOdeslane.jsp\n";
  private static final String VICE_INFO_DOCKONTR = "\n\nKompletnďż˝ seznam schvalovacďż˝ch listďż˝ najdete v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                          "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocKontroling.jsp\n";
  private static final String VICE_INFO_BUDESK = "\n\nZmďż˝nu mďż˝ďż˝ete provďż˝st v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu na:\n"+
                                          "\t\t\t\thttps://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=BudgetStd.jsp\n";
  private static final String CSS = Constants.ROOT_FILES_PATH+"\\bc4j.css";

  private boolean getPovoleno(ApplicationModule am, int id) 
  {
    boolean ret = false;
  
    ViewObject vo = am.findViewObject("KpEmailParamView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+id);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      ret = "1".equals(row.getAttribute("CPovoleno"));
    }
    vo.closeRowSet();
    
    return ret;
  }

  public int getPocetDni(ApplicationModule am, int id) 
  {
    int ret = 0;
  
    ViewObject vo = am.findViewObject("KpEmailParamView1");
    vo.clearCache();
    vo.setWhereClause("ID = "+id);
    if(vo.hasNext()) 
    {
      Row row = vo.next();
      Number tmp = (Number)row.getAttribute("NlPocetdni");
      if(tmp!=null) ret=tmp.intValue();
    }
    vo.closeRowSet();
    
    return ret;
  }

  public boolean sendMail(String od, String[] komu, String subjekt, String text, String[] prilohy)
  {
    String komuList = "";

logger.debug("sendMail CO:"+subjekt);
	
    try 
    {
      Session session = Session.getDefaultInstance(props, null);
      
      MimeMessage message = new MimeMessage(session);
      message.setFrom(new InternetAddress(od));
      for(int i=0; i<komu.length; i++) {
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(komu[i]));
      }
      message.setSubject(subjekt,ENCODING);
      message.setSentDate(new java.util.Date());
  
      MimeMultipart mm = new MimeMultipart();
  
      MimeBodyPart bpText = new MimeBodyPart();
      bpText.setText(text,ENCODING);
      mm.addBodyPart(bpText);
  
      for(int i=0; i<prilohy.length; i++) {
        MimeBodyPart bpAtt = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(prilohy[i]);
        DataHandler dh = new DataHandler(fds);
        bpAtt.setDataHandler(dh);
        bpAtt.setDisposition("attachment");
        String nazev = prilohy[i].substring(prilohy[i].lastIndexOf('\\')+1);
        bpAtt.setFileName(nazev);
        mm.addBodyPart(bpAtt);
      }
  
      message.setContent(mm);
      Transport.send(message);
      
      for(int i=0; i<komu.length; i++) 
      {
        komuList += komu[i];
        if(i<komu.length-1) komuList+=",";
      }
      /*esc 18.04.08*/
	  logger.debug("KOMU:"+komuList+"-CO:"+subjekt);
    }
    catch(Throwable e) 
    {
      e.printStackTrace();
      logger.error("Neďż˝spďż˝ch pďż˝i posďż˝lďż˝nďż˝ mailu!!! KOMU:"+komuList+"-CO:"+subjekt,e);
      return false;
    }
    return true;
  }

  private Set setFowrardEmail(String view, ApplicationModule am, Set users, String whereSpecial) 
  {
    ViewObject voZastup = am.findViewObject(view);
    voZastup.clearCache();
    
    Set newUsers = new HashSet();
    newUsers.addAll(users);

    Iterator iter = users.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      voZastup.setWhereClause("ID_KOHO = "+userId + " AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO AND C_FWDEMAIL = '1' "+whereSpecial);
      while(voZastup.hasNext()) 
      {
        Row row = voZastup.next();
        Number zastup = (Number) row.getAttribute("IdKdo");
        newUsers.add(zastup);
        newUsers.remove(userId);
      }
      voZastup.closeRowSet();
    }
    
    users.addAll(newUsers);
    
    return newUsers;
  }
  
  private Set setFowrardEmail(String view, ApplicationModule am, Set users)
  {
    return setFowrardEmail(view, am, users, "");
  }

  private Map setFowrardEmail(String view, ApplicationModule am, Map usersMap) 
  {
    if(usersMap.isEmpty()) return usersMap;

    ViewObject voZastup = am.findViewObject(view);
    voZastup.clearCache();
    
    Map newUsers = new HashMap();
    newUsers.putAll(usersMap);
    
    Iterator iter = usersMap.keySet().iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      Map vinici = (Map)usersMap.get(userId);
      voZastup.setWhereClause("ID_KOHO = "+userId + " AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO AND C_FWDEMAIL = '1'");
      while(voZastup.hasNext()) 
      {
        Row row = voZastup.next();
        Number zastup = (Number) row.getAttribute("IdKdo");
        newUsers.put(zastup, vinici);
        newUsers.remove(userId);
      }
      voZastup.closeRowSet();
    }
    
    usersMap.putAll(newUsers);
    
    return newUsers;
  }

  private String getEmailAddress(ApplicationModule am, Number userId) 
  {
    String mailAdd = null;
    ViewObject voUser = am.findViewObject("KtgAppuserView1");
    voUser.clearCache();
    voUser.setWhereClause("ID = "+userId);
    if(voUser.hasNext()) 
    {
      Row row = voUser.next();
      mailAdd = (String) row.getAttribute("SEmail");
    }
    voUser.closeRowSet();
    return mailAdd;
  }

  private String getFullName(ApplicationModule am, int userId) 
  {
    String name = "";
    ViewObject voUser = am.findViewObject("KtgAppuserView1");
    voUser.clearCache();
    voUser.setWhereClause("ID = "+userId);
    if(voUser.hasNext()) 
    {
      Row row = voUser.next();
      name += row.getAttribute("SKrestni")!=null ? (String) row.getAttribute("SKrestni")+" " : "";
      name += row.getAttribute("SPrijmeni")!=null ? (String) row.getAttribute("SPrijmeni") : "";
    }
    voUser.closeRowSet();
    return name;
  }
  
  private List getOsobySegment(ApplicationModule am, int idSegment, boolean projekt) 
  {
    List list = new ArrayList();
    ViewObject vo = am.findViewObject("KpDatMngsegmentbossView1");
    vo.clearCache();
    vo.setWhereClause("ID_MNGSEGMENT = "+idSegment+" AND SYSDATE BETWEEN DT_PLATNOSTOD AND DT_PLATNOSTDO");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number idBoss = (Number) row.getAttribute("IdBoss");
      Number idTop = (Number) row.getAttribute("IdTopmng");
      Number idSponzor = (Number) row.getAttribute("IdSponzor");
//      list.add(new Object[] {"ďż˝ďż˝f/ka segmentu", idBoss});
      list.add(new Object[] {"OO holdingu", idBoss});
      list.add(new Object[] {"Top manager/ka", idTop});
      if(projekt) list.add(new Object[] {"Sponzor/ka", idSponzor});
    }
    vo.closeRowSet();
    return list;
  }

  public void sendNewProject(ApplicationModule am, int pm, String projekt, int idSegment) 
  {
    String mailAdd = null;

    if(getPovoleno(am,7)) {
      mailAdd = getEmailAddress(am, new Number(pm));
      if(mailAdd != null && mailAdd.length()>0) {
        sendMail(kis_sender,
                 new String[] {mailAdd},
                 "Pďż˝idďż˝n novďż˝ projekt",
                 "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byl pďż˝idďż˝n novďż˝ projekt \""+projekt+"\", kde jste byl/a vybrďż˝n/a jako zodpovďż˝dnďż˝/ďż˝ project manager/ka."+VICE_INFO,
                 new String[] {});
      }

      List list = getOsobySegment(am, idSegment, true);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        String kdo = (String) oos[0];
        Number osoba = (Number) oos[1];
        if(osoba!=null) {
          mailAdd = getEmailAddress(am, osoba);
          if(mailAdd != null && mailAdd.length()>0) {
            sendMail(kis_sender,
                     new String[] {mailAdd},
                     "Pďż˝idďż˝n novďż˝ projekt",
                     "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byl pďż˝idďż˝n novďż˝ projekt \""+projekt+"\", kde jste byl/a vybrďż˝n/a jako "+kdo+"."+VICE_INFO,
                     new String[] {});
          }
        }
      }
    }
    
    if(getPovoleno(am,5)) {    
      Set set = new HashSet();
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 4");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();

      if(set.size()>0) {
        sendMail(kis_sender,
                 (String[]) set.toArray(new String[] {}),
                 "Pďż˝idďż˝n novďż˝ projekt",
                 "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byl pďż˝idďż˝n novďż˝ projekt \""+projekt+"\"."+VICE_INFO,
                 new String[] {});
      }
    }
  }

  public void sendChngProject(ApplicationModule am, int pm, int origPm, String projekt, int idSegment) 
  {
    String mailAdd = null;    
    Set set = new HashSet();
    StringBuffer lidiSegment = new StringBuffer();

    if(getPovoleno(am,6)) {
      mailAdd = getEmailAddress(am, new Number(pm));
      if(mailAdd != null && mailAdd.length()>0) {
        set.add(mailAdd);
      }

      mailAdd = getEmailAddress(am, new Number(origPm));
      if(mailAdd != null && mailAdd.length()>0) {
        set.add(mailAdd);
      }

      List list = getOsobySegment(am, idSegment, true);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        String kdo = (String) oos[0];
        Number osoba = (Number) oos[1];
        if(osoba!=null) {
          mailAdd = getEmailAddress(am, osoba);
          if(mailAdd != null && mailAdd.length()>0) {
            set.add(mailAdd);
          }
          lidiSegment.append(kdo+": "+getFullName(am, osoba.intValue())+"\n");
        }
      }
    }
    
    if(getPovoleno(am,8)) {    
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 4");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
    }

    if(set.size()>0) {
      String kdoNaKoho = " zmďż˝nďż˝n/a projekt manager/ka z "+getFullName(am,origPm)+
                                               " na "+getFullName(am,pm)+". Dalďż˝ďż˝ odpovďż˝dnďż˝ osoby jsou:\n"+lidiSegment.toString();
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝nďż˝n projekt manager",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl/a v projektu \""+projekt+"\" "+kdoNaKoho+VICE_INFO,
               new String[] {});
    }
  }

  public void sendNewProjectNavrh(ApplicationModule am, int idProjektNavrh) 
  {
    Set set = new HashSet();
    String mailAdd = null;
    
    Number idNavrh = null;
    String nazev = "", navrhuje = "", sponzor = "", pman = "", popis = "", menaN = "", menaV = "", zacatek = "", konec = "";
    Number vynos = null, naklad = null;

    if(getPovoleno(am,14)) {
      SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
      ViewObject voNavrh = am.findViewObject("VwKtgProjektnavrhView1");
      voNavrh.clearCache();
      voNavrh.setWhereClause("ID = "+idProjektNavrh);
      if(voNavrh.hasNext()) 
      {
        Row rowNavrh = voNavrh.next();
        nazev = (String)rowNavrh.getAttribute("SNazev");
        idNavrh = (Number)rowNavrh.getAttribute("IdNavrhuje");
        navrhuje = (String)rowNavrh.getAttribute("Navrhuje");
        sponzor = (String)rowNavrh.getAttribute("Sponzor");
        pman = (String)rowNavrh.getAttribute("Pmanager");
        popis = (String)rowNavrh.getAttribute("SPopis");
        vynos = (Number)rowNavrh.getAttribute("NdVynos");
        menaV = (String)rowNavrh.getAttribute("SMenavynos");
        naklad = (Number)rowNavrh.getAttribute("NdNaklad");
        menaN = (String)rowNavrh.getAttribute("SMenanaklad");
        oracle.jbo.domain.Date hlpDt = (oracle.jbo.domain.Date) rowNavrh.getAttribute("DtZacatek");
        if(hlpDt != null) zacatek = sdf.format(hlpDt.dateValue());
        hlpDt = (oracle.jbo.domain.Date) rowNavrh.getAttribute("DtKonec");
        if(hlpDt != null) konec = sdf.format(hlpDt.dateValue());
      }
      voNavrh.closeRowSet();
      
      if(idNavrh!=null && idNavrh.intValue()>0) 
      {
        mailAdd = getEmailAddress(am, idNavrh);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      else 
      {
        if(navrhuje != null && navrhuje.length()>0) 
          set.add(navrhuje);
      }

      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 6");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
    }
    
    if(set.size()>0) {
      String info = "\nNďż˝zev projektu: " + nazev +
                    "\nNavrhuje: " + navrhuje +
                    "\nSponzor: " + sponzor +
                    "\nProjekt manager/ka: " + pman +
                    "\nPopis projektu: " + popis +
                    "\nPďż˝edpoklďż˝danďż˝ vďż˝nos: " + vynos + " " + menaV +
                    "\nPďż˝edpoklďż˝danďż˝ nďż˝klad: " + naklad + " " + menaN +
                    "\nZaďż˝ďż˝tek realizace: " + zacatek +
                    "\nUkonďż˝enďż˝ realizace: " + konec + VICE_INFO;
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Novďż˝ nďż˝vrh na projekt "+nazev,
               "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byl pďż˝idďż˝n novďż˝ nďż˝vrh na projekt \""+nazev+"\".\n"+info,
               new String[] {});
    }
  }

  public void sendNewSpolecnost(ApplicationModule am, int spolId, String spolNazev, int[] osoby, int idSegment) 
  {
    Set set = new HashSet();
    String mailAdd = null;

    if(getPovoleno(am,25)) {    
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 7");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
    }
    
    if(set.size()>0) {
      String ostatniOsoby = "\nSloďż˝enďż˝ teamu zodpovďż˝dnďż˝ch osob novďż˝ zaďż˝azenďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nosti je:\n"+
                            ((osoby[0]>0)?"\tZodpovďż˝dnďż˝/ďż˝ ďż˝ďż˝etnďż˝: "+getFullName(am,osoby[0])+"\n":"")+
                            ((osoby[1]>0)?"\tOdpovďż˝dnďż˝ osoba: "+getFullName(am,osoby[1])+"\n":"")+
                            ((osoby[2]>0)?"\tAdministrďż˝tor/ka: "+getFullName(am,osoby[2])+"\n":"");

      StringBuffer lidiSegment = new StringBuffer();
      List list = getOsobySegment(am, idSegment, false);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        String kdo = (String) oos[0];
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          lidiSegment.append("\t"+kdo+": "+getFullName(am, osobaSeg.intValue())+"\n");
        }
      }

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Pďż˝idďż˝na novďż˝ spoleďż˝nost",
//               "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byla zaďż˝azena novďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nost \""+spolNazev+"\" pod ID="+spolId+"."+ostatniOsoby+lidiSegment+VICE_INFO,
               "Do aplikace KIS byla zaďż˝azena novďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nost \""+spolNazev+"\" pod ID="+spolId+"."+ostatniOsoby+lidiSegment+VICE_INFO,
               
               new String[] {});
    }
  }

  public void sendNewSpolecnost(ApplicationModule am, int spolId, String spolNazev, int idKomu, String kdo, int[] osoby, int idSegment) 
  {
    if(!getPovoleno(am,26)) return;

    String mailAdd = getEmailAddress(am, new Number(idKomu));
    
    if(mailAdd != null && mailAdd.length()>0) {
      String ostatniOsoby = "\nSloďż˝enďż˝ teamu zodpovďż˝dnďż˝ch osob novďż˝ zaďż˝azenďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nosti je:\n"+
                            ((osoby[0]>0)?"\tZodpovďż˝dnďż˝/ďż˝ ďż˝ďż˝etnďż˝: "+getFullName(am,osoby[0])+"\n":"")+
                            ((osoby[1]>0)?"\tOdpovďż˝dnďż˝ osoba: "+getFullName(am,osoby[1])+"\n":"")+
                            ((osoby[2]>0)?"\tAdministrďż˝tor/ka: "+getFullName(am,osoby[2])+"\n":"");

      StringBuffer lidiSegment = new StringBuffer();
      List list = getOsobySegment(am, idSegment, false);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        String kdoSeg = (String) oos[0];
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          lidiSegment.append("\t"+kdoSeg+": "+getFullName(am, osobaSeg.intValue())+"\n");
        }
      }

      sendMail(kis_sender,
               new String[] {mailAdd},
               "Pďż˝idďż˝na novďż˝ spoleďż˝nost",
//               "Do Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu byla zaďż˝azena novďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nost \""+spolNazev+"\" pod ID="+spolId+", u nďż˝ jste byl/a vybrďż˝n/a jako "+kdo+"."+ostatniOsoby+lidiSegment+VICE_INFO,
               "Do aplikace KIS byla zaďż˝azena novďż˝ ďż˝ďż˝etnďż˝ spoleďż˝nost \""+spolNazev+"\" pod ID="+spolId+", u nďż˝ jste byl/a vybrďż˝n/a jako "+kdo+"."+ostatniOsoby+lidiSegment+VICE_INFO,
               new String[] {});
    }
  }

  public void sendChngSpolecnost(ApplicationModule am, int spolId, String spolNazev, int osoba, int origOsoba, String kdo, int[] osoby, int idSegment) 
  {
    String mailAdd = null;    
    Set set = new HashSet();

    List list = getOsobySegment(am, idSegment, false);

    if(getPovoleno(am,27)) {
      mailAdd = getEmailAddress(am, new Number(origOsoba));
      if(mailAdd != null && mailAdd.length()>0) {
        set.add(mailAdd);
      }

      for(int i=0; i<osoby.length; i++) {
        mailAdd = getEmailAddress(am, new Number(osoby[i]));
        if(mailAdd != null && mailAdd.length()>0) {
          set.add(mailAdd);
        }
      }

      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          mailAdd = getEmailAddress(am, osobaSeg);
          if(mailAdd != null && mailAdd.length()>0) {
            set.add(mailAdd);
          }
        }
      }
    }
    
    if(getPovoleno(am,28)) {    
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 7");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
    }

    if(set.size()>0) {
      String kdoNaKoho = " zmďż˝nďż˝n/a "+kdo+" z "+getFullName(am,origOsoba)+
                                               " na "+getFullName(am,osoba)+".";
      String ostatniOsoby = "\nNovďż˝ sloďż˝enďż˝ teamu zodpovďż˝dnďż˝ch osob spoleďż˝nosti je tedy:\n"+
                            ((osoby[0]>0)?"\tZodpovďż˝dnďż˝/ďż˝ ďż˝ďż˝etnďż˝: "+getFullName(am,osoby[0])+"\n":"")+
                            ((osoby[1]>0)?"\tOdpovďż˝dnďż˝ osoba: "+getFullName(am,osoby[1])+"\n":"")+
                            ((osoby[2]>0)?"\tAdministrďż˝tor/ka: "+getFullName(am,osoby[2])+"\n":"");

      StringBuffer lidiSegment = new StringBuffer();
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        String kdoSeg = (String) oos[0];
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          lidiSegment.append("\t"+kdoSeg+": "+getFullName(am, osobaSeg.intValue())+"\n");
        }
      }
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝nďż˝n zodpovďż˝dnďż˝ spoleďż˝nosti",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl/a u spoleďż˝nosti \""+spolNazev+"\" (ID="+spolId+") "+kdoNaKoho+ostatniOsoby+lidiSegment+VICE_INFO,
               new String[] {});
    }
  }

  public void sendChngSpolecnostKat(ApplicationModule am, int spolId, String spolNazev, int[] osoby, boolean jeNeni, int idSegment) 
  {
    String mailAdd = null;    
    Set set = new HashSet();

    if(getPovoleno(am,27)) {
      for(int i=0; i<osoby.length; i++) {
        mailAdd = getEmailAddress(am, new Number(osoby[i]));
        if(mailAdd != null && mailAdd.length()>0) {
          set.add(mailAdd);
        }
      }

      List list = getOsobySegment(am, idSegment, false);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          mailAdd = getEmailAddress(am, osobaSeg);
          if(mailAdd != null && mailAdd.length()>0) {
            set.add(mailAdd);
          }
        }
      }
    }
    
    if(getPovoleno(am,28)) {    
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 7");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
    }

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝nďż˝na kategorie spoleďż˝nosti",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu je spoleďż˝nost \""+spolNazev+"\" (ID="+spolId+") v kategorii "+(jeNeni?"":"NE")+"podlďż˝hajďż˝cďż˝ pďż˝iďż˝azenďż˝ MIS identifikace projektu a odboru ke kaďż˝dďż˝ transakci vďż˝etnďż˝ obďż˝hu pďż˝es SL."+VICE_INFO,
               new String[] {});
    }
  }

  public void sendChngSpolecnostMngSeg(ApplicationModule am, int spolId, String spolNazev, int[] osoby, String zSeg, String naSeg, int idSegment, int idSegmentOrig) 
  {
    String mailAdd = null;    
    Set set = new HashSet();

    if(getPovoleno(am,49)) {
/*esc 18.04.08*/
//logger.debug("MAIL 49 POVOLEN");
      for(int i=0; i<osoby.length; i++) {
        mailAdd = getEmailAddress(am, new Number(osoby[i]));
        if(mailAdd != null && mailAdd.length()>0) {
          set.add(mailAdd);
        }

        List list = getOsobySegment(am, idSegment, false);
        Iterator iter = list.iterator();
        while(iter.hasNext()) 
        {
          Object[] oos = (Object[]) iter.next();
          Number osobaSeg = (Number) oos[1];
          if(osobaSeg!=null) {
            mailAdd = getEmailAddress(am, osobaSeg);
            if(mailAdd != null && mailAdd.length()>0) {
              set.add(mailAdd);
            }
          }
        }
  
        list = getOsobySegment(am, idSegmentOrig, false);
        iter = list.iterator();
        while(iter.hasNext()) 
        {
          Object[] oos = (Object[]) iter.next();
          Number osobaSeg = (Number) oos[1];
          if(osobaSeg!=null) {
            mailAdd = getEmailAddress(am, osobaSeg);
            if(mailAdd != null && mailAdd.length()>0) {
              set.add(mailAdd);
            }
          }
        }
      }

/*esc 18.04.08*/
logger.debug("MAIL PRO: "+mailAdd);

      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 26");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
//esc holdingy    }

/*esc 18.04.08*/
//logger.debug("POSIELAM EMAIL o zmene HOLDINGU na PROJEKTE!!! ");

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//               "Zmďż˝na mng. segmentu spoleďż˝nosti",
               "Zmďż˝na holdingu spoleďż˝nosti",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl u spoleďż˝nosti \""+spolNazev+"\" (ID="+spolId+") zmďż˝nďż˝n holding z "+zSeg+" na "+naSeg+"."+VICE_INFO,
               new String[] {});
    }
  } // povolene posielanie mailu o zmenach MNG ..
  }

  public void sendChngProjektMngSeg(ApplicationModule am, int projId, String projNazev, int[] osoby, String zSeg, String naSeg, int idSegment, int idSegmentOrig) 
  {
/*esc 18.04.08*/
logger.debug("projNazev:"+projNazev+"-zSeg:"+zSeg+"-naSeg:"+naSeg);
    String mailAdd = null;    
    Set set = new HashSet();

    if(getPovoleno(am,49)) {
/*esc 18.04.08*/
//logger.debug("MAIL 49 POVOLEN");      
	  for(int i=0; i<osoby.length; i++) {
        mailAdd = getEmailAddress(am, new Number(osoby[i]));
        if(mailAdd != null && mailAdd.length()>0) {
          set.add(mailAdd);
        }
      }

      List list = getOsobySegment(am, idSegment, false);
      Iterator iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          mailAdd = getEmailAddress(am, osobaSeg);
          if(mailAdd != null && mailAdd.length()>0) {
            set.add(mailAdd);
          }
        }
      }

      list = getOsobySegment(am, idSegmentOrig, false);
      iter = list.iterator();
      while(iter.hasNext()) 
      {
        Object[] oos = (Object[]) iter.next();
        Number osobaSeg = (Number) oos[1];
        if(osobaSeg!=null) {
          mailAdd = getEmailAddress(am, osobaSeg);
          if(mailAdd != null && mailAdd.length()>0) {
            set.add(mailAdd);
          }
        }
      }

/*esc 18.04.08*/
logger.debug("MAIL PRO: "+mailAdd);

      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 26");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
      vo.closeRowSet();
//esc holding    }
/*esc 18.04.08*/
//logger.debug("POSIELAM EMAIL o zmene segmentu na PROJEKTE!!! ");

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               //"Zmďż˝na mng. segmentu projektu",
               "Zmďż˝na holdingu",
//               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl u projektu \""+projNazev+"\" (ID="+projId+") zmďż˝nďż˝n managerskďż˝ segment z "+zSeg+" na "+naSeg+"."+VICE_INFO,
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl u projektu \""+projNazev+"\" (ID="+projId+") zmďż˝nďż˝n holding  z "+zSeg+" na "+naSeg+"."+VICE_INFO,
               new String[] {});
    }
    }  //povoleno posielat mail
  }

  public void sendProtokol(ApplicationModule am, Number ucId, String[] fileNames, int typeUser) 
  {
    int typPovoleno = 0;
    String kdo = null;
    if(typeUser==Constants.UCETNI) {
      kdo = "zodpovďż˝dnďż˝/ďż˝ ďż˝ďż˝etnďż˝";
      typPovoleno = 2;
    }
    else if(typeUser==Constants.OO) {
      kdo = "odpovďż˝dnďż˝ osoba";
      typPovoleno = 3;
    }
    else if(typeUser==Constants.SEF_SEGMENTU) {
//      kdo = "ďż˝ďż˝f/ka segmentu";
      kdo = "OO holdingu";
      typPovoleno = 4;
    }
    else if(typeUser==Constants.TOP) {
      kdo = "odpovďż˝dnďż˝/ďż˝ ďż˝len/ka Top managementu";
      typPovoleno = 4;
    }
    else if(typeUser==Constants.NADRIZENA_UCETNI) {
      kdo = "nadďż˝ďż˝zenďż˝/ďż˝ ďż˝ďż˝etnďż˝";
      typPovoleno = 2;
    }
    
    if(!getPovoleno(am,typPovoleno)) return;
  
    String mailAdd = getEmailAddress(am, ucId);
    if(mailAdd != null && mailAdd.length()>0) {
      sendMail(kis_sender,
               new String[] {mailAdd},
               "Protokol automatickďż˝ho generovďż˝nďż˝",
               "V pďż˝ďż˝loze tohoto mailu je protokol z automatickďż˝ho generovďż˝nďż˝ Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu se spoleďż˝nostmi, u kterďż˝ch figurujete jako "+kdo+"."+VICE_INFO,
               fileNames);
    }
  }

  public void sendProtokolSkupiny(ApplicationModule am, Number ucId, String fileName, String skupina) 
  {
    if(!getPovoleno(am,2)) return;
  
    String mailAdd = getEmailAddress(am, ucId);
    if(mailAdd != null && mailAdd.length()>0) {
      sendMail(kis_sender,
               new String[] {mailAdd},
               "Protokol automatickďż˝ho generovďż˝nďż˝",
               "V pďż˝ďż˝loze tohoto mailu je protokol z automatickďż˝ho generovďż˝nďż˝ Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu se spoleďż˝nostmi konsolidaďż˝nďż˝ skupiny "+skupina+"."+VICE_INFO,
               new String[] {fileName/*, CSS*/});
    }
  }

  public void sendProtokol(ApplicationModule am, Set userIds, String[] fileNames, int typeUser) 
  {
    if(typeUser==Constants.UNIF_UCET && !getPovoleno(am,47)) return;
    if(typeUser!=Constants.UNIF_UCET && !getPovoleno(am,54)) return;
  
    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      set.add(mailAdd);
    }
    
    if(set.size()>0) {
      StringBuffer buf = new StringBuffer("V pďż˝ďż˝loze tohoto mailu je protokol z automatickďż˝ho generovďż˝nďż˝ Konsolidaďż˝nďż˝ho Informaďż˝nďż˝ho Systďż˝mu. ");
     
      String subject = "Protokol automatickďż˝ho generovďż˝nďż˝";
      if(typeUser==Constants.MUSTEK) {
        subject = "Chyby mďż˝stkďż˝ z generovďż˝nďż˝";
        buf.append("Tento protokol obsahuje pouze chyby mďż˝stkďż˝, zjiďż˝tďż˝nďż˝ bďż˝hem generovďż˝nďż˝. ");
      }
      else if(typeUser==Constants.UNIF_UCET) {
        subject = "Chyby unifikovanďż˝ch ďż˝ďż˝tďż˝";
        buf.append("Tento protokol obsahuje pouze chyby unifikovanďż˝ch ďż˝ďż˝tďż˝, zjiďż˝tďż˝nďż˝ bďż˝hem generovďż˝nďż˝. ");
      }
     
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               subject,
               buf.toString()+VICE_INFO,
               fileNames);
    }
  }

  public void sendDocZadavatel(ApplicationModule am, Set userIds) 
  {
    if(!getPovoleno(am,13)) return;

    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
//esc 03.10      
      logger.debug("Schvalovacďż˝ listy - zadavatel");
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
               "Schvalovacďż˝ listy - zadavatel",
               "Byl/a jste vybrďż˝n/a jako zadavatel Schvalovacďż˝ho listu v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu."+VICE_INFO_DOC_ZAD,
               new String[] {});
    }
  }

/// ==========================  esc 01/09 TYPIZOVANE ======================= end
 /// v3, posle ludom info s XLS vytvorenych  podla typizovanych
/*  public void sendDocTypiz (ApplicationModule am, Set setNamesT,String cestaT) {
  logger.debug("PROTOKOL - SEND email v3 Typiz START"); 
      {
        if(!getPovoleno(am,9)) return;

        setFowrardEmail("KpDatDoczastupView1", am, setNamesT);

        Set set = new HashSet();
        
        Iterator iter = setNamesT.iterator();
        while(iter.hasNext()) 
        {
          Number userId = (Number) iter.next();
          String mailAdd = getEmailAddress(am, userId);
          if(mailAdd != null)
            set.add(mailAdd);
        }

        if(set.size()>0) {
          sendMail(kis_sender,
                   (String[]) set.toArray(new String[] {}),
                    "Typizovane Schvalovacďż˝ listy",
            "Vas schvaleny Typizovany schvalovacďż˝ list byl pouzit k vytvoreni SL v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu."+VICE_INFO_TYPIZ+"\n <a href=//https://tweek.pa.jtfg.com/konsolidace/index.jsp?ref=DocPrijate.jsp?filterSchval=21> LINK </a>",
             //https://tweek.pa.jtfg.com/konsolidace/index.jsp?ref=DocPrijate.jsp?filterSchval=21                    
                    new String[] {cestaT});
        }
      }                    
  }
*/  
/// v4, posle ludom info so zoznamom SL vytvorenych  podla typizovanych cez objekty
      public void sendDocTypizUsed(ApplicationModule am, Map mapKomuCo) 
      {
//       logger.debug("PROTOKOL - SEND email v4 Typiz START"); 
            if(!getPovoleno(am,9)) return;
        
       
        String mailAdd = null;
        String Schvalovaky = ""; // text o Schvalovakoch
        
        Iterator iter = mapKomuCo.keySet().iterator();

        while(iter.hasNext()) {
          Schvalovaky = ""; // text o Schvalovakoch
          Number komu = (Number) iter.next();              //userId
          logger.debug("PROTOKOL - SEND email Typiz MAIL :"+komu); 
          
          mailAdd = getEmailAddress(am, new Number(komu));
     // logger.debug("PROTOKOL - SEND email Typiz MAIL :"+mailAdd); 
          HashMap mapSeznamSl;
          mapSeznamSl = (HashMap)mapKomuCo.get(komu); 
            
             //v4TreeMap mapSeznamSl;
            //v4 mapSeznamSl = (TreeMap)mapKomuCo.get(komu); //Set co = (Set) komuCo.get(komu);
                       
          //v4Iterator iterSezSl = mapSeznamSl.keySet().iterator(); //Iterator ip = co.iterator();
           //v4Iterator iterSezSl = mapSeznamSl.values().iterator();
           
           
           SchvalovakData [] schvalovakyP = new SchvalovakData[mapSeznamSl.size()];
           int i = 0;
           Iterator iterSezSl = mapSeznamSl.keySet().iterator();
           while (iterSezSl.hasNext()){
               Integer idDoc = (Integer)iterSezSl.next();
               schvalovakyP[i++] = (SchvalovakData)mapSeznamSl.get(idDoc);
               Schvalovaky += "\n SL "+i+" - "+idDoc;
           }
    
            Arrays.sort(schvalovakyP);
           
            for (i = 0; i < schvalovakyP.length; i++)
          //while(iterSezSl.hasNext()) 
          {            
            //Number idDoc = (Number) iterSezSl.next();
            //v4int idDoc = ((Integer)iterSezSl.next()).intValue();
            
            //v4SchvalovakData schvalovakData = (SchvalovakData)mapSeznamSl.get(idDoc);
            //SchvalovakData schvalovakData = (SchvalovakData)iterSezSl.next();                  
             SchvalovakData schvalovakData = (SchvalovakData)schvalovakyP[i];
             
            Integer  idDoc  = schvalovakData.getIdDoc();
            Schvalovaky += "\n\n SCHVALOVACďż˝ LIST ID: "+idDoc+", ";           

            Number idTypizlink = schvalovakData.getIdTypizlink();                       
            Schvalovaky += "\t (Pďż˝vodnďż˝ Typizovanďż˝ SL:"+idTypizlink + ")";      
            


            String nazevSpolocnosti = schvalovakData.getNazevSpolocnost();
            Number idSpolocnosti =  schvalovakData.getIdSpolocnost();
            String mena    = schvalovakData.getMena();
            String protistrana    = schvalovakData.getProtistrana();
			String typTransakce   = schvalovakData.getTypTransakce();
			
            Schvalovaky += "\n\t "+nazevSpolocnosti + "\t ("+idSpolocnosti+")";

            Schvalovaky += "\n\t Protistrana: "+ protistrana;
            Schvalovaky += "\n\t Typ transakce: "+ typTransakce; 
            HashMap radkySl = schvalovakData.getRadky();

             Iterator iterSchvalovakRadekData = radkySl.keySet().iterator(); //Iterator ip = co.iterator();                    
              while(iterSchvalovakRadekData.hasNext()) 
              {
                Number idRadek = (Number) iterSchvalovakRadekData.next();                
                 
                SchvalovakRadekData schvalovakRadekData = (SchvalovakRadekData) radkySl.get(idRadek);

                
                String odbor =  schvalovakRadekData.getOdbor();
                String projekt = schvalovakRadekData.getProjekt();
                String komentar = schvalovakRadekData.getKomentar();
                Number suma = schvalovakRadekData.getCastka();
                //dbg  Schvalovaky +="\n\t"+idRadek;
                  Schvalovaky +="\n\n\t\t Odbor: "+ odbor +", Projekt: "+projekt+"\n\t\t Castka: "+suma+" "+mena+"\n\t\t Komentar: "+komentar;
                  
              }
             
              }       
//        logger.debug("PROTOKOL - SEND email Typiz MAIL ::"+Schvalovaky);                                  
		//logger.debug("PROTOKOL - SEND email Typiz MAIL ::"+mailAdd);                                  
          if(mailAdd!=null) {
            sendMail(kis_sender,
                     new String[] {mailAdd},
                                    "Typizovane Schvalovacďż˝ listy",
                                    "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byli automaticky schvalenďż˝ druhotnďż˝ Typizovanďż˝ schvalovacďż˝ listy podle vďż˝mi schvďż˝lenďż˝ch Typizovanďż˝ch SL."+Schvalovaky+"\n"+VICE_INFO_TYPIZ,              
                     new String[] {});
          } //mail
        } //mapKomuCo
      }
/// ==========================  esc 01/09 TYPIZOVANE ======================= end  v4
/// v2, posle ludom info so zoznamom SL vytvorenych  podla typizovanych
/*  public void sendDocTypizUsed(ApplicationModule am, Map komuCo) 
  {
   logger.debug("PROTOKOL - SEND email Typiz START"); 
	if(!getPovoleno(am,9)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    Iterator iter = komuCo.keySet().iterator();
    while(iter.hasNext()) {
   
      Number komu = (Number) iter.next();
	  
	  logger.debug("PROTOKOL - SEND email Typiz MAIL :"+komu); 
      
	  mailAdd = getEmailAddress(am, new Number(komu));
      Set co = (Set) komuCo.get(komu);
      String Schvalovaky = "";
      Iterator ip = co.iterator();
      while(ip.hasNext()) 
      {

        Number p = (Number) ip.next();
        Schvalovaky += "\n\t"+p+", ";
	
		logger.debug("PROTOKOL - SEND email Typiz MAIL ::"+Schvalovaky); 
    
	  }	  
	 
      if(mailAdd!=null) {
        sendMail(kis_sender,
                 new String[] {mailAdd},
				"Typizovane Schvalovacďż˝ listy",
				"V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byli vami schvalenďż˝ Typizovanďż˝ schvalovacďż˝ listy pouďż˝ity k vytvoďż˝eni nďż˝sledujďż˝cďż˝ch druhotnďż˝ch SL."+Schvalovaky+"\n"+VICE_INFO_DOC,              
                 new String[] {});
      }
    }
  }
*/
  /// ==========================  esc 01/09 TYPIZOVANE ======================= end
/// v1, posle iba info o tom ze nejaky SL bol pouzity resp vytvoreny
/*public void sendDocTypiz(ApplicationModule am, Set userIds) 
  {
    if(!getPovoleno(am,9)) return;

    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Typizovane Schvalovacďż˝ listy",
               "Vas schvaleny Typizovany schvalovacďż˝ list byl pouzit k vytvoreni SL v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu."+VICE_INFO_TYPIZ+"\n <a href=//https://tweek.pa.jtfg.com/konsolidace/index.jsp?ref=DocPrijate.jsp?filterSchval=21> LINK </a>",
                //https://tweek.pa.jtfg.com/konsolidace/index.jsp?ref=DocPrijate.jsp?filterSchval=21
               new String[] {});
    }
  }
*/  
  /// ==========================  esc 01/09 TYPIZOVANE ======================= end
  public void sendDocToDo(ApplicationModule am, Set userIds) 
  {
    if(!getPovoleno(am,9)) return;
    logger.debug("mail - sendDocToDo");
    
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      logger.debug("Schvalovacďż˝ listy - schvaleni");
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
               "Schvalovacďż˝ listy - schvaleni",
               "Mďż˝te schvalovacďż˝ listy ke schvďż˝lenďż˝ v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu."+VICE_INFO_DOC,
               new String[] {});
    }
  }

  //public void sendDocKontr(ApplicationModule am, Set userIds, Set vinici, Map mapSL, Set viniciBud, Map mapSLBud, String soubor) 
  //public void sendDocKontr(ApplicationModule am, Set userIds, Set vinici, Map mapSL, Set viniciBud, Map mapSLBud, String soubor,String soubor2) 
    public void sendDocKontr(ApplicationModule am, Set userIds, Set vinici, Map mapSL, Set viniciBud, Map mapSLBud, String soubor, String soubor2, String soubor3)  
  {
    if(!getPovoleno(am,10)) return;

    //setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer();
      if(vinici.size()>0) {
        msg.append("Kontroling: Tyto odpovďż˝dnďż˝ osoby ve stanovenďż˝ lhďż˝tďż˝ neschvďż˝lily svďż˝ schvalovacďż˝ listy:");
        Iterator iterVin = vinici.iterator();
        while(iterVin.hasNext()) 
        {
          String name = (String) iterVin.next();
          msg.append("\n\t"+name);
          StringBuffer sb = (StringBuffer) mapSL.get(name);
          msg.append(" - listy ke schvďż˝lenďż˝: "+sb.toString());
        }
        msg.append("\n\n");
      }
      if(viniciBud.size()>0) {
        msg.append("Kontroling: Tyto odpovďż˝dnďż˝ osoby doposud neschvďż˝lily navďż˝ďż˝enďż˝ budgetďż˝ v SL:");
        Iterator iterVin = viniciBud.iterator();
        while(iterVin.hasNext()) 
        {
          String name = (String) iterVin.next();
          msg.append("\n\t"+name);
          StringBuffer sb = (StringBuffer) mapSLBud.get(name);
          msg.append(" - listy ke schvďż˝lenďż˝: "+sb.toString());
        }
      }
      
      msg.append("\n\nPďż˝ďż˝lohou tohoto mailu je XLS se seznamem schvalovacďż˝ch listďż˝ za poslednďż˝ 3 mďż˝sďż˝ce, s vyznaďż˝enďż˝m pďż˝ekroďż˝enďż˝m datumu splatnosti jednotlivďż˝mďż˝ ďż˝rovnďż˝mi zpracovďż˝nďż˝ SL.");
    /*
     String[] prilohy;
     int  i=1;
     prilohy = new String[] {soubor};
     
     if (soubor2 != null ) 
      {
             prilohy[i++] = soubor2;
      }
       
     if (soubor3 != null ) 
      {
             prilohy[i++] = soubor3;
      }
     */
     try{
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016               "Schvalovacďż˝ listy",
//logger.debug("Schvalovacďż˝ listy"+" - kontroling 3M");
               "Schvalovacďż˝ listy",
               msg.toString()+VICE_INFO_DOCKONTR,
               //esc 12/2009
           //    (String[]) prilohy                );
               //new String[] {soubor});
               //new String[] {soubor,soubor2});
               new String[] {soubor,soubor2, soubor3});
             //new String[] {soubor,soubor2!=null ? soubor2:"", soubor3!=null ? soubor3:""});
    }
      catch(Throwable e) 
    {
      e.printStackTrace();
      logger.error("ERROR Neďż˝spďż˝ch pďż˝i posďż˝lďż˝nďż˝ mailu Schvalovacďż˝ listy - Kontroling!!! ",e);      
    }
    }
  }

  public void sendDocKontrNedokoncene(ApplicationModule am, Set userIds, Map mapSL, Map mapNeschval, Map mapSLMis, Map mapNeschvalMis, Map mapNeschvalGes, Map mapSLBud, Map mapNeschvalBud, Map mapUctoNeschval) 
  {
    if(!getPovoleno(am,15) && !getPovoleno(am,21)) return;

    //setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    StringBuffer msg = new StringBuffer();
    Comparator c = new Comparator() {
                             public int compare(Object obj1, Object obj2) 
                             {
                               if(obj1==null && obj2==null) return 0;
                               else if(obj2==null) return 1;
                               else if(obj1==null) return -1;
                               
                               String o1 = (String) obj1;
                               String o2 = (String) obj2;
                               String t1 = o1.substring(o1.indexOf('(')+1);
                               String t2 = o2.substring(o2.indexOf('(')+1);
                               int vysl = t1.compareTo(t2);
                               if(vysl!=0) return vysl;
                               else return o1.compareTo(o2);
                             }
                           };
    
    if(set.size()>0 && mapSL.size()>0 && getPovoleno(am,15)) {
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali zpracovďż˝vat schvalovacďż˝ listy:");
      Set ts = new TreeSet(c);
      ts.addAll(mapSL.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapSL.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapNeschval.size()>0 && getPovoleno(am,21)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali ďż˝eďż˝it zamďż˝tnutďż˝ svďż˝ch schvalovacďż˝ch listďż˝:");
      Set ts = new TreeSet(c);
      ts.addAll(mapNeschval.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapNeschval.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapSLMis.size()>0 && getPovoleno(am,15)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali zpracovďż˝vat MIS/ďż˝ďż˝TO transakce:");
      Set ts = new TreeSet(c);
      ts.addAll(mapSLMis.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapSLMis.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapNeschvalMis.size()>0 && getPovoleno(am,21)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali ďż˝eďż˝it zamďż˝tnutďż˝ svďż˝ch MIS/ďż˝ďż˝TO transakcďż˝:");
      Set ts = new TreeSet(c);
      ts.addAll(mapNeschvalMis.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapNeschvalMis.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapNeschvalGes.size()>0 && getPovoleno(am,21)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito gestoďż˝i ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali ďż˝eďż˝it zamďż˝tnutďż˝ svďż˝ch schvalovacďż˝ch listďż˝:");
      Set ts = new TreeSet(c);
      ts.addAll(mapNeschvalGes.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapNeschvalGes.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapUctoNeschval.size()>0 && getPovoleno(am,21)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali ďż˝eďż˝it zamďż˝tnutďż˝ svďż˝ch schvalovacďż˝ch listďż˝ zodpovďż˝dnou ďż˝ďż˝etnďż˝:");
      Set ts = new TreeSet(c);
      ts.addAll(mapUctoNeschval.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapUctoNeschval.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapSLBud.size()>0 && getPovoleno(am,15)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali zpracovďż˝vat navďż˝ďż˝enďż˝ budgetu:");
      Set ts = new TreeSet(c);
      ts.addAll(mapSLBud.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapSLBud.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }

    if(set.size()>0 && mapNeschvalBud.size()>0 && getPovoleno(am,21)) {
      if(msg.length()>0) msg.append("\n\n");
      msg.append("Kontroling: Tito zadavatelďż˝ ve stanovenďż˝ lhďż˝tďż˝ nezaďż˝ali ďż˝eďż˝it zamďż˝tnutďż˝ svďż˝ch navďż˝ďż˝enďż˝ budgetu:");
      Set ts = new TreeSet(c);
      ts.addAll(mapNeschvalBud.keySet());
      Iterator iterVin = ts.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapNeschvalBud.get(name);
        msg.append(" - listy ke zpracovďż˝nďż˝: "+sb.toString());
      }
    }
    
    if(msg.length()>0)
    {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10               "Schvalovacďż˝ listy",
//logger.debug("Schvalovacďż˝ listy"+"  - kontroling/zamitnuti ");
               "Schvalovacďż˝ listy",
               msg.toString()+VICE_INFO_DOCKONTR,
               new String[] {});
    }
  }

  public void sendDocTop(ApplicationModule am, Map mapTop) 
  {
    if(!getPovoleno(am,11)) return;

    if(mapTop.isEmpty()) return;
    
    //setFowrardEmail("KpDatDoczastupView1", am, mapTop);

    Iterator iterTop = mapTop.keySet().iterator();
    while(iterTop.hasNext()) 
    {
      Number top = (Number) iterTop.next();
      String mailAdd = getEmailAddress(am, top);
      Map vinici = (Map)mapTop.get(top);
        
      if(mailAdd!=null && vinici.size()>0) {
        StringBuffer msg = new StringBuffer("OOH: Tyto odpovďż˝dnďż˝ osoby ve stanovenďż˝ lhďż˝tďż˝ neschvďż˝lily svďż˝ schvalovacďż˝ listy:");
        Iterator iterVin = vinici.keySet().iterator();
        while(iterVin.hasNext()) 
        {
          String name = (String) iterVin.next();
          msg.append("\n\t"+name);
          StringBuffer sb = (StringBuffer) vinici.get(name);
          msg.append(" - listy ke schvďż˝lenďż˝: "+sb.toString());
        }
        sendMail(kis_sender,
                 new String[] {mailAdd},
//esc 03.10.2016                 "Schvalovacďż˝ listy",
//logger.debug("Schvalovacďż˝ listy"+" - kontroling/neschvaleni OOH");
                 "Schvalovacďż˝ listy",
                 msg.toString()+VICE_INFO_DOCKONTR,
                 new String[] {});
      }
    }
  }

  public void sendDocTopAll(ApplicationModule am, Set userIds, Set vinici, Map mapSL) 
  {
    if(!getPovoleno(am,12)) return;

    //setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0 && vinici.size()>0) {
      StringBuffer msg = new StringBuffer("OOH-All: Tyto odpovďż˝dnďż˝ osoby ve stanovenďż˝ lhďż˝tďż˝ neschvďż˝lily svďż˝ schvalovacďż˝ listy:");
      Iterator iterVin = vinici.iterator();
      while(iterVin.hasNext()) 
      {
        String name = (String) iterVin.next();
        msg.append("\n\t"+name);
        StringBuffer sb = (StringBuffer) mapSL.get(name);
        msg.append(" - listy ke schvďż˝lenďż˝: "+sb.toString());
      }
    
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
//logger.debug("Schvalovacďż˝ listy"+" - kontroling/neschvaleni all");
               "Schvalovacďż˝ listy",
               msg.toString()+VICE_INFO_DOCKONTR,
               new String[] {});
    }
  }
  
  public void sendDocZamitRadek(ApplicationModule am, Set userIds, Number idDoc, String duvod) 
  {
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("Ve schvalovacďż˝m listu "+idDoc+" byl zamďż˝tnut ďż˝ďż˝dek. ");
      msg.append("\nDďż˝vod je: "+duvod);
//esc 08 2011
      msg.append("\n SL: https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocSchval.jsp?idDoc="+idDoc);    
      logger.debug("Schvalovacďż˝ listy - zamitnuti");      
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
               "Schvalovacďż˝ listy - zamitnuti",
               msg.toString()+VICE_INFO_DOC,
               new String[] {});
    }
  }

  public void sendDocZamitGestor(ApplicationModule am, Set userIds, Number idDoc, String duvod) 
  {
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("Schvalovacďż˝ list "+idDoc+" byl zamďż˝tnut gestorem. ");
      msg.append("\nDďż˝vod je: "+duvod);
//esc 08 2011
      msg.append("\n SL: https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocSchval.jsp?idDoc="+idDoc);    
      
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Schvalovacďż˝ listy",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendDocZamitUcetni(ApplicationModule am, Set userIds, Number idDoc, String duvod) 
  {
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("Schvalovacďż˝ list "+idDoc+" byl zamďż˝tnut ďż˝ďż˝tujďż˝cďż˝m. ");
      msg.append("\nDďż˝vod je: "+duvod);

//esc 08 2011
      msg.append("\n SL: https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocSchval.jsp?idDoc="+idDoc);    
      
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Schvalovacďż˝ listy",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendDocZamitZadavatel(ApplicationModule am, Set userIds, Number idDoc, String duvod) 
  {
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      logger.debug("Schvalovacďż˝ listy - odmitnuti zadavatelem");               
      
      StringBuffer msg = new StringBuffer("Schvalovacďż˝ list "+idDoc+" byl odmďż˝tnut zadavatelem. ");
      msg.append("\nDďż˝vod je: "+duvod);      
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
               "Schvalovacďż˝ listy - odmitnuti zadavatelem",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendDocZmenaZruseni(ApplicationModule am, int idDoc, Set userIds) 
  {
    setFowrardEmail("KpDatDoczastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
//      StringBuffer msg = new StringBuffer("Ve schvalovacďż˝m listu "+idDoc+" doďż˝lo ke zmďż˝nďż˝m. Proto byla zruďż˝ena Vaďż˝e schvďż˝lenďż˝. ");
//      msg.append("Zďż˝roveďż˝ byla zruďż˝ena i zamďż˝tnutďż˝ v tomto SL, takďż˝e je moďż˝nďż˝ pokraďż˝ovat ve schvalovďż˝nďż˝.\n");

      logger.debug("Schvalovacďż˝ listy - zmďż˝na");

      StringBuffer msg = new StringBuffer("Ve schvalovacďż˝m listu "+idDoc+" doďż˝lo ke zmďż˝nďż˝m. Proto byla zruďż˝ena Vaďż˝e schvďż˝lenďż˝. ");
      msg.append("Zďż˝roveďż˝ byla zruďż˝ena i zamďż˝tnutďż˝ v tomto SL, takďż˝e je moďż˝nďż˝ pokraďż˝ovat ve schvalovďż˝nďż˝.\n");

//esc 08/2011
      msg.append("\n SL: https://"+Utils.getInetAddress()+"/"+Constants.PROJECT_PATH+"/index.jsp?ref=DocSchval.jsp?idDoc="+idDoc);    
      
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
//esc 03.10.2016  "Schvalovacďż˝ listy",
               "Schvalovacďż˝ listy - zmďż˝na",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }
  
  public void sendBudgetZmenaCastky(ApplicationModule am, double origCastka, double castka, String mena, Number[] oos, String budget, String typTran, String zmenil) 
  {
    if(!getPovoleno(am,42)) return;

    Set set = new HashSet();
    set.add(Constants.KONTROLING_SK);
    
    for(int i=0; i<oos.length; i++)
    {
      Number userId = oos[i];
      if(userId!=null) {
        String mailAdd = getEmailAddress(am, userId);
        if(mailAdd != null)
          set.add(mailAdd);
      }
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("V budgetu '"+budget+"' byla u typu transakce '"+typTran+"' zmďż˝nďż˝na ďż˝ďż˝stka z "+origCastka+" na "+castka+" (v "+mena+").");
      msg.append(" Zmďż˝nu ďż˝ďż˝stky provedl(a) "+zmenil+".");

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Budgeting - zmďż˝na ďż˝ďż˝stky",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendBudgetEskaling(ApplicationModule am, String zadatel, Set userIds, String budget, String tran) 
  {
    if(!getPovoleno(am,16)) return;

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("Uďż˝ivatel "+zadatel+" prosďż˝ o navďż˝ďż˝enďż˝ pro kategorii "+tran+" pro budget "+budget+".");

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Budgeting",
               msg.toString()+VICE_INFO_BUDESK,
               new String[] {});
    }
  }

  public void sendBudgetDenni(ApplicationModule am, Number user, String cesta) 
  {
    if(!getPovoleno(am,17)) return;
    
    Set userIds = new HashSet();
    userIds.add(user);
    //setFowrardEmail("KpDatBudgetzastupView1", am, userIds, " AND ID_BUDGET IS NULL");

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("S politovďż˝nďż˝m musďż˝me oznďż˝mit, ďż˝e byl pďż˝ekroďż˝en plďż˝novanďż˝ budget pro nďż˝kterďż˝ typy transakcďż˝, u nichďż˝ figurujete jako odpovďż˝dnďż˝ osoba. Pďż˝esnďż˝ seznam je v pďż˝iloďż˝enďż˝m XLS");

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Budgeting - pďż˝ekroďż˝enďż˝",
               msg.toString()+VICE_INFO_BUDESK,
               new String[] {cesta});
    }
  }

  public void sendBudgetDenni(ApplicationModule am, Set userIds, String budget, String tran) 
  {
    if(!getPovoleno(am,17)) return;
    
    setFowrardEmail("KpDatBudgetzastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("S politovďż˝nďż˝m musďż˝me oznďż˝mit, ďż˝e byl pďż˝ekroďż˝en budget "+budget+" pro kategorii "+tran+".");

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Budgeting",
               msg.toString()+VICE_INFO_BUDESK,
               new String[] {});
    }
  }

  public void sendBudgetMesicni(ApplicationModule am, Number userId, String fileName, int povoleno) 
  {
    if(!getPovoleno(am,povoleno)) return;
    
    Set userIds = new HashSet();
    userIds.add(userId);

    //setFowrardEmail("KpDatBudgetzastupView1", am, userIds);

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userIdNew = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userIdNew);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("Jako pďż˝ďż˝lohu tohoto mailu posďż˝lďż˝me budget-report (ve formďż˝tu excel) pro prďż˝vďż˝ uplynulďż˝ mďż˝sďż˝c.");

      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Budgeting",
               msg.toString()+VICE_INFO_BUDESK,
               new String[] {fileName});
    }
  }

  public void sendBudgetSchvalovani(ApplicationModule am, Map map, String fileName) 
  {
    if(!getPovoleno(am,45)) return;
    
    //setFowrardEmail("KpDatBudgetzastupView1", am, userIds);
    Set userIds = new HashSet();
    Set set = new HashSet();

    if(map==null || map.isEmpty()) return;

    String[] osoby = new String[] {"odpovďż˝dnďż˝ osoba odboru",
                                   "gestor",
                                   "odpovďż˝dnďż˝ osoba spoleďż˝nosti",
                                   //"ďż˝ďż˝f segmentu"};
                                   "odpovďż˝dnďż˝ osoba holdingu"};
    String[] zpr = new String[] {"V tďż˝chto budgetech mďż˝ďż˝ete (ale nemusďż˝te) schvalovat, ne vďż˝echny pďż˝edchďż˝zejďż˝cďż˝ ďż˝rovnďż˝ schvalovďż˝nďż˝ jsou schvďż˝leny:\n",
                                 "V tďż˝chto budgetech byste mďż˝l/a schvalovat, vďż˝echny pďż˝edchďż˝zejďż˝cďż˝ ďż˝rovnďż˝ schvalovďż˝nďż˝ jsou jiďż˝ schvďż˝leny:\n" };
    
    StringBuffer[] msgKontr = new StringBuffer[] { new StringBuffer(), new StringBuffer() };

    Iterator urovne = map.keySet().iterator();
    while(urovne.hasNext()) 
    {
      String uroven = (String) urovne.next();
      int uInt = Integer.parseInt(uroven)-1;
      Map mapOsoby = (Map) map.get(uroven);
      Iterator os = mapOsoby.keySet().iterator();
      while(os.hasNext()) 
      {
        StringBuffer msg = new StringBuffer("Tento mail obsahuje seznam Vaďż˝ich budgetďż˝, ve kterďż˝ch jeďż˝tďż˝ chybďż˝ Vaďż˝e schvďż˝lenďż˝ nďż˝kterďż˝ch nďż˝kladďż˝ v roli "+osoby[uInt]+".\n");
        msg.append("V zďż˝vorce za budgetem je vďż˝dy poďż˝et neschvďż˝lenďż˝ch nďż˝kladďż˝ v danďż˝m budgetu.");
        Number idOsoby = (Number) os.next();
//        String mailAdr = getEmailAddress(am, idOsoby);
        String jmeno = getFullName(am, idOsoby.intValue());
        Set[] radky = (Set[]) mapOsoby.get(idOsoby);
        for(int i=1; i>=0; i--) 
        {
          Iterator iter = radky[i].iterator();
          if(iter.hasNext()) msg.append("\n\n"+zpr[i]+"\n");
          while(iter.hasNext()) 
          {
            String txt = (String) iter.next();
            txt = txt.substring(txt.indexOf('@')+1);
            msg.append("\t"+txt+"\n\n");
            msgKontr[i].append("\t"+jmeno+" ("+osoby[uInt]+") - "+txt+"\n\n");
          }          
        }
        userIds.clear();
        userIds.add(idOsoby);
//        setFowrardEmail("KpDatBudgetzastupView1", am, userIds);
        set.clear();
        if(!userIds.isEmpty()) {
          Iterator iu = userIds.iterator();
          while(iu.hasNext()) 
          {
            Number user = (Number) iu.next();
            String mailAdd = getEmailAddress(am, user);
            if(mailAdd!=null) set.add(mailAdd);
          }
          if(!set.isEmpty()) {
            sendMail(kis_sender,
                     (String[]) set.toArray(new String[] {}),
                     "Neschvďż˝lenďż˝ budgetu",
                     msg.toString()+VICE_INFO_BUDESK,
                     new String[] {});
          }
        }
      }
    }

    set.clear();
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 5");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      String mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Neschvďż˝lenďż˝ budgetu - kontroling",
               "Tento mail obsahuje seznam budgetďż˝, ve kterďż˝ch jeďż˝tďż˝ chybďż˝ schvďż˝lenďż˝ nďż˝kterďż˝ch nďż˝kladďż˝.\n"+
               "V zďż˝vorce za budgetem je vďż˝dy poďż˝et neschvďż˝lenďż˝ch nďż˝kladďż˝ v danďż˝m budgetu."+
               "\n\nBudgety, v nichďż˝ by uvedenďż˝ osoba mďż˝la schvalovat, vďż˝echny pďż˝edchďż˝zejďż˝cďż˝ ďż˝rovnďż˝ schvalovďż˝nďż˝ jsou jiďż˝ schvďż˝leny:\n\n"+
               msgKontr[1]+
               "\n\nBudgety, v nichďż˝ by uvedenďż˝ osoba mďż˝ďż˝e schvalovat, ne vďż˝echny pďż˝edchďż˝zejďż˝cďż˝ ďż˝rovnďż˝ schvalovďż˝nďż˝ jsou ale jiďż˝ schvďż˝leny:\n\n"+
               msgKontr[0]+
               VICE_INFO_BUDESK,
               new String[] {fileName});
    }
  }

  public void sendEviSpolecnost(ApplicationModule am, Set userIds, String text) 
  {
    if(!getPovoleno(am,30)) return;

    Set set = new HashSet();
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      String mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer(text);
    
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Admin. spoleďż˝nosti",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendEviSpolecnostNavrh(ApplicationModule am, Set userIds, String text, int senderId) 
  {
    if(!getPovoleno(am,33)) return;
    
    String sender = getEmailAddress(am, new Number(senderId));
    String mailAdd = null;

    Set set = new HashSet();
    set.add(sender);
    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) 
    {
      Number userId = (Number) iter.next();
      mailAdd = getEmailAddress(am, userId);
      if(mailAdd != null)
        set.add(mailAdd);
    }

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 8");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      StringBuffer msg = new StringBuffer(text);
    
      sendMail(sender,
               (String[]) set.toArray(new String[] {}),
               "Admin. spoleďż˝nosti",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendEviSpolecnostOR3M(ApplicationModule am, Set kompl, Set akc) 
  {
    if(!getPovoleno(am,36)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    StringBuffer buf = new StringBuffer("Seznam evidovanďż˝ch spoleďż˝nostďż˝ (plus zemďż˝ pďż˝vodu a datum poslednďż˝ho vďż˝pisu), kterďż˝m za mďż˝nďż˝ neďż˝ mďż˝sďż˝c vyprďż˝ďż˝ platnost vďż˝pisu z obchodnďż˝ho rejsďż˝ďż˝ku:\n");

    Iterator iter = kompl.iterator();
    if(!kompl.isEmpty()) buf.append("Kompletnďż˝ vďż˝pis:");
    while(iter.hasNext()) 
    {
      String txt = (String) iter.next();
      buf.append("\n\t"+txt);
    }
    if(!kompl.isEmpty()) buf.append("\n\n");

    iter = akc.iterator();
    if(!akc.isEmpty()) buf.append("Akcionďż˝ďż˝i:");
    while(iter.hasNext()) 
    {
      String txt = (String) iter.next();
      buf.append("\n\t"+txt);
    }
    if(!akc.isEmpty()) buf.append("\n");

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 10");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Admin. spoleďż˝nosti - OR",
               buf.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendEviSpolecnostOR1M(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,36)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 10");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Admin. spoleďż˝nosti - OR",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s evidovanďż˝mi administrovanďż˝mi spoleďż˝nostmi, jejichďż˝ kompletnďż˝ vďż˝pis z obchodnďż˝ho rejstďż˝ďż˝ku (v evidenci KIS) vyprďż˝ďż˝ pďż˝esnďż˝ za 1 mďż˝sďż˝c, a s tďż˝mi, kterďż˝ kompletnďż˝ vďż˝pis v KIS nemajďż˝ vďż˝bec.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendOnlineSpolecnost(ApplicationModule am, int spolId, String spolNazev, int[] osoby, String online, int idSegment) 
  {
    if(!getPovoleno(am,31)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    for(int i=0; i<osoby.length; i++) {
      mailAdd = getEmailAddress(am, new Number(osoby[i]));
      if(mailAdd != null && mailAdd.length()>0) {
        set.add(mailAdd);
      }
    }

    List list = getOsobySegment(am, idSegment, false);
    Iterator iter = list.iterator();
    while(iter.hasNext()) 
    {
      Object[] oos = (Object[]) iter.next();
      Number osobaSeg = (Number) oos[1];
      if(osobaSeg!=null) {
        mailAdd = getEmailAddress(am, osobaSeg);
        if(mailAdd != null && mailAdd.length()>0) {
          set.add(mailAdd);
        }
      }
    }
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 7");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Spoleďż˝nost je on/off-line",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byl u spoleďż˝nosti \""+spolNazev+"\" (ID="+spolId+") zmďż˝nďż˝n stav na "+("1".equals(online)?"on":"off")+"-line, a "+("1".equals(online)?"zaďż˝ďż˝najďż˝":"pďż˝estďż˝vajďż˝")+" se tedy aplikovat standardnďż˝ pravidla na schvalovďż˝nďż˝ pro ďż˝ďż˝etnďż˝ a OO."+VICE_INFO,
               new String[] {});
    }
  }

  public void sendProjektSpolecnost(ApplicationModule am, String projekt, int projId, String pmanJmeno, String spolNazev, Set komu, String zap, String segProj, String segSpol) 
  {
    if(!getPovoleno(am,32)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    Iterator iter = komu.iterator();
    while(iter.hasNext()) {
      Number num = (Number) iter.next();
      mailAdd = getEmailAddress(am, new Number(num));
      if(mailAdd != null && mailAdd.length()>0) {
        set.add(mailAdd);
      }
    }
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 4");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      String proces = null;
      if("I".equals(zap)) proces = "pďż˝idďż˝na";
      else if("D".equals(zap)) proces = "odebrďż˝na";
      else proces = "provedena zmďż˝na pro";
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Spoleďż˝nost u projektu",
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu byla u projektu \""+projekt+"\" ("+projId+", seg. "+segProj+") "+proces+" spoleďż˝nost "+spolNazev+" (seg. "+segSpol+"). Projekt manager je "+pmanJmeno+"."+VICE_INFO,
               new String[] {});
    }
  }

  public void sendProjektPreceneni(ApplicationModule am, String projekt, int projId, Number projManager) 
  {
    if(!getPovoleno(am,41)) return;
    
    String mailAdd = getEmailAddress(am, projManager);
    if(mailAdd != null && mailAdd.length()>0) {
      sendMail(kis_sender,
               new String[] {mailAdd},
               "Pďż˝ecenďż˝nďż˝ projektu "+projId,
               "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu je tďż˝eba provďż˝st pďż˝ecenďż˝nďż˝ u projektu \""+projekt+"\" (ID="+projId+")."+VICE_INFO,
               new String[] {});
    }
  }

  public void sendProjektPreceneniKontroling(ApplicationModule am, List projekty) 
  {
    if(!getPovoleno(am,41)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 3");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      StringBuffer msg = new StringBuffer("V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu je tďż˝eba provďż˝st pďż˝ecenďż˝nďż˝ u nďż˝sledujďż˝cďż˝ch projektďż˝:\n\n");
      Iterator iter = projekty.iterator();
      while(iter.hasNext()) 
      {
        msg.append(iter.next()+"\n\n");
      }
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Pďż˝ecenďż˝nďż˝ projektďż˝",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendProjektMemorandum(ApplicationModule am, Map komuCo) 
  {
    if(!getPovoleno(am,35)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    Iterator iter = komuCo.keySet().iterator();
    while(iter.hasNext()) {
      Number komu = (Number) iter.next();
      mailAdd = getEmailAddress(am, new Number(komu));
      Set co = (Set) komuCo.get(komu);
      String projekty = "";
      Iterator ip = co.iterator();
      while(ip.hasNext()) 
      {
        String p = (String) ip.next();
        projekty += "\n\t"+p+", ";
      }

      if(mailAdd!=null) {
        sendMail(kis_sender,
                 new String[] {mailAdd},
                 "Karta projektu",
                 "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu nebyla dodďż˝na informaďż˝nďż˝ zprďż˝va do karty projektu nďż˝sledujďż˝cďż˝ch projektďż˝: "+projekty+"\n u kterďż˝ch figurujete jako projekt manager."+VICE_INFO,
                 new String[] {});
      }
    }
  }

  public void sendProjektMemorandumKontroling(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,34)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 9");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Karta projektu - kontroling",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s projekty, jimďż˝ nebyla dodďż˝na karta projektu a majďż˝ uďż˝ po termďż˝nu dodďż˝nďż˝, pďż˝ďż˝padnďż˝ tento termďż˝n vyprďż˝ďż˝ v nejbliďż˝ďż˝ďż˝ch dnech.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendProjektMemorandumSS(ApplicationModule am, Number idBoss, String cesta) 
  {
    if(!getPovoleno(am,34)) return;//TBC
    
    Set userIds = new HashSet();
    userIds.add(idBoss);
    setFowrardEmail("KpDatSegmentzastupView1", am, userIds, "AND NL_TYP in (1,3)");
    
    Set set = new HashSet();
    String mailAdd = null;    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) {
      Number komu = (Number) iter.next();
      mailAdd = getEmailAddress(am, komu);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }

    if(mailAdd!=null && mailAdd.length()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Karta projektu - OOH",
//               "Mail obsahuje jako pďż˝ďż˝lohu XLS s projekty, kterďż˝ spadajďż˝ do Vaďż˝ich segmentďż˝, jimďż˝ nebyla dodďż˝na karta projektu a majďż˝ uďż˝ po termďż˝nu dodďż˝nďż˝, pďż˝ďż˝padnďż˝ tento termďż˝n vyprďż˝ďż˝ v nejbliďż˝ďż˝ďż˝ch dnech.\n"+VICE_INFO,
                  "Mail obsahuje jako pďż˝ďż˝lohu XLS s projekty, kterďż˝ spadajďż˝ do Vaďż˝ich holdingďż˝, jimďż˝ nebyla dodďż˝na karta projektu a majďż˝ uďż˝ po termďż˝nu dodďż˝nďż˝, pďż˝ďż˝padnďż˝ tento termďż˝n vyprďż˝ďż˝ v nejbliďż˝ďż˝ďż˝ch dnech.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendSpolecnostZprava(ApplicationModule am, Map komuCo) 
  {
    if(!getPovoleno(am,53)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    Iterator iter = komuCo.keySet().iterator();
    while(iter.hasNext()) {
      Number komu = (Number) iter.next();
      mailAdd = getEmailAddress(am, new Number(komu));
      Set co = (Set) komuCo.get(komu);
      String spolKarty = "";
      Iterator ip = co.iterator();
      while(ip.hasNext()) 
      {
        String p = (String) ip.next();
        spolKarty += "\n\t"+p+", ";
      }

      if(mailAdd!=null) {
        sendMail(kis_sender,
                 new String[] {mailAdd},
                 "Karta SPV",
                 "V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu nebyla dodďż˝na informaďż˝nďż˝ zprďż˝va do karet SPV: "+spolKarty+"\n u kterďż˝ch figurujete jako odpovďż˝dnďż˝ osoba."+VICE_INFO,
                 new String[] {});
      }
    }
  }

  public void sendSpolecnostZpravaKontroling(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,52)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 9");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Karta SPV - kontroling",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s SPV, jimďż˝ nebyla dodďż˝na informaďż˝nďż˝ zprďż˝va a majďż˝ uďż˝ po termďż˝nu dodďż˝nďż˝, pďż˝ďż˝padnďż˝ tento termďż˝n vyprďż˝ďż˝ v nejbliďż˝ďż˝ďż˝ch dnech.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendSpolecnostZpravaSS(ApplicationModule am, Number idBoss, String cesta) 
  {
    if(!getPovoleno(am,52)) return;//TBC
    
    Set userIds = new HashSet();
    userIds.add(idBoss);
    setFowrardEmail("KpDatSegmentzastupView1", am, userIds, "AND NL_TYP in (2,4)");
    
    Set set = new HashSet();
    String mailAdd = null;    
    Iterator iter = userIds.iterator();
    while(iter.hasNext()) {
      Number komu = (Number) iter.next();
      mailAdd = getEmailAddress(am, komu);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    
    if(mailAdd!=null && mailAdd.length()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Karta SPV - OOH",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s SPV, kterďż˝ spadajďż˝ do Vaďż˝ich holdingďż˝, jimďż˝ nebyla dodďż˝na informďż˝nďż˝ zprďż˝va a majďż˝ uďż˝ po termďż˝nu dodďż˝nďż˝, pďż˝ďż˝padnďż˝ tento termďż˝n vyprďż˝ďż˝ v nejbliďż˝ďż˝ďż˝ch dnech.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }
  
  public void sendDokladZamitnuti(ApplicationModule am, String idZamek, String spol, String datum, String duvod, Number ucetni, Number ucetniBoss, Number oo, Number ss, Number admin) 
  {
    if(!getPovoleno(am,37)) return;
    
    String nazevSpol = null;
    
    ViewObject voSpol = am.findViewObject("KpKtgUcetnispolecnostView1");
    voSpol.clearCache();
    voSpol.setWhereClause("ID = "+spol);
    if(voSpol.hasNext()) 
    {
      Row row = voSpol.next();
      nazevSpol = (String)row.getAttribute("SNazev");
    }
    voSpol.closeRowSet();

    Set set = new HashSet();
    String mailAdd = null;
    StringBuffer msg = new StringBuffer("V Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu doďż˝lo k zamďż˝tnutďż˝ dokladu "+nazevSpol+" (ID="+spol+") z "+datum);
    
    String kym = null;
    ViewObject voZam = am.findViewObject("KpCisZamekView1");
    voZam.clearCache();
    voZam.setWhereClause("ID = "+idZamek);
    if(voZam.hasNext()) 
    {
      Row row = voZam.next();
      kym = (String)row.getAttribute("SPopis");
    }
    voZam.closeRowSet();

    msg.append(" "+kym);
    
    if("1".equals(idZamek) || "101".equals(idZamek)) 
    {
      mailAdd = getEmailAddress(am, ucetni);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, ucetniBoss);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, oo);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
    }
    else if("2".equals(idZamek) || "102".equals(idZamek)) 
    {
      mailAdd = getEmailAddress(am, ucetni);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, oo);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, ss);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, admin);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 11");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      }
      vo.closeRowSet();
    }
    else if("3".equals(idZamek) || "103".equals(idZamek)) 
    {
      mailAdd = getEmailAddress(am, oo);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      mailAdd = getEmailAddress(am, ss);
      if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      
      ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
      vo.clearCache();
      vo.setWhereClause("ID_MSGTYPE = 11");
      while(vo.hasNext()) 
      {
        Row row = vo.next();
        Number id = (Number)row.getAttribute("IdKtgappuser");
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) set.add(mailAdd);
      }
      vo.closeRowSet();
    }
    msg.append(". Dďż˝vodem zamďż˝tnutďż˝ je: "+duvod+".\n");


    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zamďż˝tnutďż˝ dokladu",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendSpolecnostZamekGen(ApplicationModule am, String cesta, boolean specific) 
  {
    if(!getPovoleno(am,38)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    if(!specific) vo.setWhereClause("ID_MSGTYPE = 11");
    else vo.setWhereClause("ID_MSGTYPE = 21");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Doklady - schvďż˝lenďż˝/zamďż˝tnutďż˝",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS se schvďż˝lenďż˝mi a zamďż˝tnutďż˝mi dokladďż˝ spoleďż˝nostďż˝ v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendSpolecnostZamekGenSS(ApplicationModule am, Number idSS, String cesta) 
  {
    if(!getPovoleno(am,40)) return;
    
    String mailAdd = getEmailAddress(am, idSS);
    
    if(mailAdd!=null) {
      sendMail(kis_sender,
               new String[] {mailAdd},
               "Doklady - schvďż˝lenďż˝/zamďż˝tnutďż˝",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS se seznamem dokladďż˝ spoleďż˝nostďż˝, kterďż˝ majďż˝ bďż˝t Vďż˝mi schvďż˝leny. XLS obsahuje jak seznam dokladďż˝, kterďż˝ uďż˝ mďż˝ly bďż˝t schvďż˝leny a mohou bďż˝t schvďż˝leny, tak i seznam tďż˝ch, kterďż˝ jeďż˝tďż˝ ďż˝ekajďż˝ na schvďż˝lenďż˝ od ďż˝ďż˝etnďż˝ nebo OO. Soubor takďż˝ obsahuje seznam spoleďż˝nostďż˝, jejichďż˝ doklady majďż˝ bďż˝t schvďż˝leny v nejbliďż˝ďż˝ďż˝ch dnech.\nDalďż˝ďż˝ informace v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }
/*
  public void sendSpolecnostZamekGenTop(ApplicationModule am, Set top, Map map) 
  {
    if(!getPovoleno(am,40)) return;

    String mailAdd = null;

    Iterator iter = top.iterator();
    while(iter.hasNext()) 
    {
      Number idTop = (Number) iter.next();
      mailAdd = getEmailAddress(am, idTop);
      StringBuffer msg = new StringBuffer("V nejbliďż˝ďż˝ďż˝ch dnech vyprďż˝ďż˝ datum pro schvďż˝lenďż˝ dokladďż˝ nďż˝sledujďż˝cďż˝ch spoleďż˝nostďż˝, u kterďż˝ch figurujete jako odpovďż˝dnďż˝ ďż˝len Top managementu:\n");
      
      String key = null;
      String text = null;
      for(int i=0; i<4; i++) {
        switch(i) 
        {
          case 0: 
            text = "Doklady tďż˝chto spoleďż˝nostďż˝ jsou schvďż˝leny zodpovďż˝dnou ďż˝ďż˝etnďż˝ i OO a jsou on-line, takďż˝e je mďż˝ďż˝ete schvďż˝lit:\n\n";
            key = idTop.toString();
            break;
          case 1: 
            text = "Doklady tďż˝chto spoleďż˝nostďż˝ nejsou schvďż˝leny OO, takďż˝e je nemďż˝ďż˝ete schvďż˝lit (ďż˝eďż˝ďż˝ controlling):\n\n";
            key = idTop+"-oo";
            break;
          case 2: 
            text = "Doklady tďż˝chto spoleďż˝nostďż˝ nejsou schvďż˝leny zodpovďż˝dnou ďż˝ďż˝etnďż˝, takďż˝e je nemďż˝ďż˝ete schvďż˝lit (ďż˝eďż˝ďż˝ nadďż˝ďż˝zenďż˝ ďż˝ďż˝etnďż˝):\n\n";
            key = idTop+"-ucetni";
            break;
          case 3: 
            text = "Doklady tďż˝chto spoleďż˝nostďż˝ nejsou on-line, takďż˝e je nemusďż˝te schvalovat:\n\n";
            key = idTop+"-off";
            break;
        }
      
        List list = (List) map.get(key);
        if(list!=null) {
          msg.append("\n\n"+text);
          Iterator iterSpol = list.iterator();
          while(iterSpol.hasNext()) 
          {
            String spol = (String) iterSpol.next();
            msg.append("\t\t"+spol);
            if(iterSpol.hasNext()) msg.append(",\n\n");
          }
        }
      }
      
      sendMail(kis_sender,
               (String[]) new String[] {mailAdd},
               "Doklady - schvďż˝lenďż˝/zamďż˝tnutďż˝",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }
*/
  public void sendSpolecnostZamekExterni(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,55)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 28");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Doklady ext. spol.",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS se seznamem externďż˝ch spoleďż˝nostďż˝, u kterďż˝ch doposud neprobďż˝ho ďż˝ďż˝dnďż˝ schvďż˝lenďż˝ dokladu a doklad tedy nenďż˝ uzavďż˝en v Konsolidaďż˝nďż˝m Informaďż˝nďż˝m Systďż˝mu.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendSpolecnostSkupina4(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,43)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 23");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Spoleďż˝nosti vs. skupina 4",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s internďż˝ ďż˝ďż˝tovanďż˝mi nearchivnďż˝ spoleďż˝nostmi, kterďż˝ momentďż˝lnďż˝ nejsou nadefinovďż˝ny jako ďż˝lenovďż˝ konsolidaďż˝nďż˝ ďż˝ďż˝etnďż˝ skupiny 4.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendSpolecnostiSoulad(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,44)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 24");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Adm. vs. Evi. spoleďż˝nosti",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s administrovanďż˝mi spoleďż˝nostmi, kterďż˝ momentďż˝lnďż˝ nejsou propojeny s totoďż˝nďż˝mi evidovanďż˝mi spoleďż˝nostmi (s administrďż˝torem).\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendEviSpolecnostZmena(ApplicationModule am, String nazev, String ico, String origNazev, String origIco, Set dbLinks) 
  {
    if(!getPovoleno(am,39)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 12");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    StringBuffer msg = new StringBuffer("U administrovanďż˝ spoleďż˝nosti "+nazev+" (Iďż˝: "+ico+") doďż˝lo k nďż˝sledujďż˝cďż˝m zmďż˝nďż˝m:");
    if(!origIco.equals(ico)) msg.append("\nIďż˝ bylo zmďż˝nďż˝no z "+origIco+" na "+ico);
    if(!origNazev.equals(nazev)) msg.append("\nNďż˝zev byl zmďż˝nďż˝n z "+origNazev+" na "+nazev);
    
    msg.append("\n\nSpoleďż˝nost je aktuďż˝lnďż˝ zaloďż˝ena pod pďż˝vodnďż˝m Iďż˝ v IS: ");
    Iterator iter = dbLinks.iterator();
    boolean first = true;
    while(iter.hasNext()) 
    {
      if(first) first=false; else msg.append(", ");
      msg.append(iter.next());
    }
    msg.append(".");
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝na admin. spoleďż˝nosti",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendEviSpolecnostNova(ApplicationModule am, String nazev, String ico, Set dbLinks) 
  {
    if(!getPovoleno(am,39)) return;
    
    Set set = new HashSet();
    String mailAdd = null;

    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 27");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    StringBuffer msg = new StringBuffer("Byla zaloďż˝ena novďż˝ administrovanďż˝ spoleďż˝nost "+nazev+" (Iďż˝: "+ico+").");
    
    msg.append("\n\nSpoleďż˝nost je aktuďż˝lnďż˝ zaloďż˝ena pod tďż˝mto Iďż˝ v IS: ");
    Iterator iter = dbLinks.iterator();
    boolean first = true;
    while(iter.hasNext()) 
    {
      if(first) first=false; else msg.append(", ");
      msg.append(iter.next());
    }
    msg.append(".");
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zaloďż˝enďż˝ admin. spoleďż˝nosti",
               msg.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendProtistranyZmeny(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,46)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 5");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝ny protistran",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS se zmďż˝nami v protistranďż˝ch z primďż˝rnďż˝ch systďż˝mďż˝, kterďż˝ byly KISem zaznamenďż˝ny za poslednďż˝ch 24 hodin.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }

  public void sendMajetekZmeny(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,56)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 29");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zmďż˝ny majetkovďż˝ch ďż˝ďż˝astďż˝",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s poslednďż˝mi zmďż˝nami v definici majetkovďż˝ch ďż˝ďż˝astďż˝.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }


  public void sendBudgetMustekNaklad(ApplicationModule am, String cesta) 
  {
    if(!getPovoleno(am,48)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 5");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();

    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Chybďż˝jďż˝cďż˝ ďż˝ďż˝ty v b. mďż˝stcďż˝ch",
               "Mail obsahuje jako pďż˝ďż˝lohu XLS s nďż˝kladovďż˝mi ďż˝ďż˝ty chybďż˝jďż˝cďż˝mi v pďż˝evodovďż˝m mďż˝stku typďż˝ transakcďż˝ (nďż˝kladďż˝) budgetďż˝.\n"+VICE_INFO,
               new String[] {cesta});
    }
  }
  
  public void sendZamekParovaniVazeb(ApplicationModule am, String akce, Number idUcetniProti, String nazevSpol, String nazevProti, String datum, int idSkup, String zamkl) 
  {
    if(!getPovoleno(am,50)) return;
    
    String mailAdd = getEmailAddress(am, idUcetniProti);
    
    StringBuffer buf = new StringBuffer("Byly "+("I".equals(akce)?"za":"ode")+"mďż˝eny vzďż˝jemnďż˝ vazby skupiny "+idSkup+" mezi spoleďż˝nostďż˝ ");
    buf.append(nazevSpol+" a spoleďż˝nostďż˝ "+nazevProti+" pro datum "+datum+". Informace Vďż˝m byla zaslďż˝na, protoďż˝e u druhďż˝ jmenovanďż˝ spoleďż˝nosti ");
    buf.append("figurujete jako zodpovďż˝dnďż˝/ďż˝ ďż˝ďż˝etnďż˝. Vazby byly zamďż˝eny uďż˝ivatelem "+zamkl+", kterďż˝ho mďż˝ďż˝ete kontaktovat v pďż˝ďż˝padďż˝ problďż˝mďż˝ nebo nejasnostďż˝.\n");
    
    if(mailAdd!=null) {
      sendMail(kis_sender,
               new String[] {mailAdd},
               "Zďż˝mek VV",
               buf.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public void sendZamekPoCloseDate(ApplicationModule am, Set spols, Set oos) 
  {
    if(!getPovoleno(am,51)) return;
    
    Set set = new HashSet();
    String mailAdd = null;
    
    ViewObject vo = am.findViewObject("KpKtgEmailzpravyView1");
    vo.clearCache();
    vo.setWhereClause("ID_MSGTYPE = 11");
    while(vo.hasNext()) 
    {
      Row row = vo.next();
      Number id = (Number)row.getAttribute("IdKtgappuser");
      mailAdd = getEmailAddress(am, id);
      if(mailAdd != null && mailAdd.length()>0) 
        set.add(mailAdd);
    }
    vo.closeRowSet();
    
    if(!oos.isEmpty()) 
    {
      Iterator iter = oos.iterator();
      while(iter.hasNext()) 
      {
        Number id = (Number) iter.next();
        mailAdd = getEmailAddress(am, id);
        if(mailAdd != null && mailAdd.length()>0) 
          set.add(mailAdd);
      }
    }
    
    StringBuffer buf = new StringBuffer("U nďż˝sledujďż˝cďż˝ch spoleďż˝nostďż˝ bylo odemďż˝eno ďż˝ďż˝tnictvďż˝, aďż˝koliv uďż˝ v KISu existujďż˝ zamďż˝enďż˝ doklady s pozdďż˝jďż˝ďż˝m datem!!!\n\n");
    Iterator iter = spols.iterator();
    while(iter.hasNext()) 
    {
      buf.append(iter.next()+"\n\n");
    }
    
    if(set.size()>0) {
      sendMail(kis_sender,
               (String[]) set.toArray(new String[] {}),
               "Zďż˝mek vs. zavďż˝rďż˝nďż˝",
               buf.toString()+VICE_INFO,
               new String[] {});
    }
  }

  public static void main(String[] args) throws Throwable
  {
//    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.dokument.DokumentModule","DokumentModuleLocal");
//    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.projekt.ProjektModule","ProjektModuleLocal");
//    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.ucskup.UcSkupModule","UcSkupModuleLocal");
    ApplicationModule dm = Configuration.createRootApplicationModule("cz.jtbank.konsolidace.doklady.DokladyModule","DokladyModuleLocal");
    Mail mail = new Mail();
/*
    mail.sendMail(kis_sender,
                  new String[]{"martinek@jtbank.cz"},
                  "Testovacďż˝ mail z javy",
                  "Testovacďż˝ mail z javy s pďż˝ďż˝lohou",
                  new String[]{});
*/    
/*
    Set set = new HashSet();
    set.add(new Number(1));
    set.add(new Number(81));
    mail.sendDocZamitRadek(dm, set , new Number(1234), "jen tak");
*/
//    mail.sendChngProject(dm,81,1,1,"Test");
//    mail.sendChngSpolecnost(dm,1234,"Test",1,2,"OO",new int[] {1,2,3,4}, 1);
    mail.sendProjektMemorandumSS(dm,new Number(46),"C:\\Konsolidace_JT\\bc4j.css");
    mail.sendSpolecnostZpravaSS(dm,new Number(46),"C:\\Konsolidace_JT\\bc4j.css");
  }
}