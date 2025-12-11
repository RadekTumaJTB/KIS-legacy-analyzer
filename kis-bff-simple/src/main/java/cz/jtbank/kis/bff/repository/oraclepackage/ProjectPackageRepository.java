package cz.jtbank.kis.bff.repository.oraclepackage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

/**
 * Repository for calling Oracle Package KAP_PROJEKT procedures.
 *
 * This repository mirrors the original Oracle ADF/BC4J implementation
 * from ProjektModuleImpl.java, calling Oracle stored procedures
 * instead of using JPA entities.
 *
 * Original procedure signature:
 * db_jt.kap_projekt.p_KpProjekt(
 *   aAkce, aIdProjekt, aNazev, aCisloOld, aIdStatus,
 *   aIdNavrhuje, aIdPManager, aIdMngSeg, aMenaNaklady,
 *   aStartOceneni, aIdFrekvence, aPopis, aIdNavrh,
 *   aDtMDalsi, aMMesicu, aUzivatel, aIdTypBilance,
 *   aSledujeBudget, aIdTypBudgetu, aKategorie
 * )
 */
@Repository
public class ProjectPackageRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProjectPackageRepository.class);

    private final DataSource dataSource;

    public ProjectPackageRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Call db_jt.kap_projekt.p_KpProjekt procedure.
     *
     * @param akce Action: 'I' = Insert, 'U' = Update, 'D' = Delete
     * @param idProjekt Project ID
     * @param nazev Project name
     * @param cisloOld Old project number
     * @param idStatus Status ID
     * @param idNavrhuje Proposer user ID
     * @param idPManager Project manager ID
     * @param idMngSeg Management segment ID (nullable)
     * @param menaNaklady Currency code
     * @param startOceneni Start evaluation date
     * @param idFrekvence Frequency ID
     * @param popis Description
     * @param idNavrh Proposal ID (nullable)
     * @param dtMDalsi Next memorandum date
     * @param mMesicu Months count
     * @param uzivatel Current username
     * @param idTypBilance Balance type ID (nullable)
     * @param sledujeBudget Track budget flag: 'A' = Yes, 'N' = No
     * @param idTypBudgetu Budget type ID (nullable)
     * @param kategorie Category ID (nullable)
     * @throws SQLException if procedure call fails
     */
    public void callKpProjekt(
            String akce,
            Integer idProjekt,
            String nazev,
            String cisloOld,
            Integer idStatus,
            Integer idNavrhuje,
            Integer idPManager,
            Integer idMngSeg,
            String menaNaklady,
            java.sql.Date startOceneni,
            Integer idFrekvence,
            String popis,
            Integer idNavrh,
            java.sql.Date dtMDalsi,
            Integer mMesicu,
            String uzivatel,
            Integer idTypBilance,
            String sledujeBudget,
            Integer idTypBudgetu,
            Integer kategorie) throws SQLException {

        Connection conn = null;
        CallableStatement st = null;

        try {
            conn = dataSource.getConnection();

            // Prepare callable statement matching original code:
            // "begin db_jt.kap_projekt.p_KpProjekt(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;"
            String sql = "begin db_jt.kap_projekt.p_KpProjekt(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
            st = conn.prepareCall(sql);

            // Set parameters (1-indexed)
            st.setString(1, akce);
            st.setInt(2, idProjekt);
            st.setString(3, nazev);
            st.setString(4, cisloOld);
            st.setInt(5, idStatus);
            st.setInt(6, idNavrhuje);
            st.setInt(7, idPManager);

            // Nullable parameter handling
            if (idMngSeg != null && idMngSeg > 0) {
                st.setInt(8, idMngSeg);
            } else {
                st.setNull(8, Types.INTEGER);
            }

            st.setString(9, menaNaklady);
            st.setDate(10, startOceneni);
            st.setInt(11, idFrekvence);
            st.setString(12, popis);

            if (idNavrh != null && idNavrh > 0) {
                st.setInt(13, idNavrh);
            } else {
                st.setNull(13, Types.INTEGER);
            }

            st.setDate(14, dtMDalsi);
            st.setInt(15, mMesicu);
            st.setString(16, uzivatel);

            if (idTypBilance != null && idTypBilance > 0) {
                st.setInt(17, idTypBilance);
            } else {
                st.setNull(17, Types.INTEGER);
            }

            st.setString(18, sledujeBudget);

            if (idTypBudgetu != null && idTypBudgetu > 0) {
                st.setInt(19, idTypBudgetu);
            } else {
                st.setNull(19, Types.INTEGER);
            }

            if (kategorie != null && kategorie > 0) {
                st.setInt(20, kategorie);
            } else {
                st.setNull(20, Types.INTEGER);
            }

            // Execute procedure
            logger.info("Executing db_jt.kap_projekt.p_KpProjekt with action: {}, idProjekt: {}", akce, idProjekt);
            st.execute();
            logger.info("Successfully executed db_jt.kap_projekt.p_KpProjekt");

        } catch (SQLException e) {
            logger.error("Failed to execute db_jt.kap_projekt.p_KpProjekt", e);
            throw new SQLException("Failed to call db_jt.kap_projekt.p_KpProjekt: " + e.getMessage(), e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {
                    logger.warn("Failed to close CallableStatement", ignored);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {
                    logger.warn("Failed to close Connection", ignored);
                }
            }
        }
    }

    /**
     * Call db_jt.kap_projekt.p_KpProjektCashFlow procedure.
     *
     * Original signature:
     * p_KpProjektCashFlow(
     *   aAkce, aId, aIdProjekt, aIdTyp, aDtDatum, aCastka,
     *   aMena, aInOut, aIdIOTyp, aPoznamka, aUzivatel
     * )
     */
    public void callKpProjektCashFlow(
            String akce,
            Integer id,
            Integer idProjekt,
            Integer idTyp,
            java.sql.Date dtDatum,
            Double castka,
            String mena,
            String inOut,
            Integer idIOTyp,
            String poznamka,
            String uzivatel) throws SQLException {

        Connection conn = null;
        CallableStatement st = null;

        try {
            conn = dataSource.getConnection();

            String sql = "begin db_jt.kap_projekt.p_KpProjektCashFlow(?,?,?,?,?,?,?,?,?,?,?); end;";
            st = conn.prepareCall(sql);

            st.setString(1, akce);
            st.setInt(2, id);
            st.setInt(3, idProjekt);
            st.setInt(4, idTyp);
            st.setDate(5, dtDatum);
            st.setDouble(6, castka);
            st.setString(7, mena);
            st.setString(8, inOut);

            if (idIOTyp != null) {
                st.setInt(9, idIOTyp);
            } else {
                st.setNull(9, Types.INTEGER);
            }

            st.setString(10, poznamka);
            st.setString(11, uzivatel);

            logger.info("Executing db_jt.kap_projekt.p_KpProjektCashFlow with action: {}, id: {}", akce, id);
            st.execute();
            logger.info("Successfully executed db_jt.kap_projekt.p_KpProjektCashFlow");

        } catch (SQLException e) {
            logger.error("Failed to execute db_jt.kap_projekt.p_KpProjektCashFlow", e);
            throw new SQLException("Failed to call db_jt.kap_projekt.p_KpProjektCashFlow: " + e.getMessage(), e);
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {}
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }
}
