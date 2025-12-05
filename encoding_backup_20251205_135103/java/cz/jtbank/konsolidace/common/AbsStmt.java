package cz.jtbank.konsolidace.common;

import oracle.jbo.server.*;
import java.sql.*;

public abstract class AbsStmt {
  private static final int[] waitMin = {1,5,10,15,30};

  ApplicationModuleImpl applicationModuleImpl;
  protected DBTransaction dbTran;  
  protected CallableStatement st = null;
  
  public AbsStmt ( ApplicationModuleImpl aApplicationModuleImpl ) 
  {
    applicationModuleImpl = aApplicationModuleImpl;
    dbTran = applicationModuleImpl.getDBTransaction();  
  }
  
  public void execute () throws SQLException
  {
    for ( int i = 0; i<waitMin.length; i++ ) {
      System.out.println("Pokus c. "+(i+1));
      try {
        createStmt();
        st.execute();
        break;
      } catch ( SQLException e ) {
        System.out.println("e.getErrorCode()="+e.getErrorCode());
        System.out.println("e.getSQLState()="+e.getSQLState());
        if ( e.getErrorCode() == 2068 ) 
        {
          dbTran.isConnected(true);
          dbTran.reconnect();  
          try {
            synchronized(this) 
            {
              System.out.println("Pokus c. "+(i+1)+" selhal.");
              System.out.println("Ceka se "+waitMin[i]+" minut*.");
              this.wait(waitMin[i]*60000);
            }
          }
          catch (InterruptedException ie) {
            ie.printStackTrace();
          }
        }
      }
    } 
  }
  
  protected abstract void createStmt () throws SQLException; 
  
  public CallableStatement getStmt() 
  {
    return st;
  }
}
