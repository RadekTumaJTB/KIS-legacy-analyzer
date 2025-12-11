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
 * Repository for calling Oracle Package KAP_DOKUMENT procedures.
 *
 * This repository mirrors the original Oracle ADF/BC4J implementation
 * from DokumentModuleImpl.java, calling Oracle stored procedures
 * instead of using JPA entities.
 *
 * Original procedure signature:
 * db_jt.kap_dokument.p_dokument(
 *   aAkce, aId, aIdCisDok, aIdSpol, aPopis, aIdTypTran, aTypizovana,
 *   aTypizovanaOd, aTypizovanaDo, aMena, aPisemne, aIdProti, aProti,
 *   aCislo, aDtSplat, aIdZadavatel, aIdCisStatus, aPokladniTransakce,
 *   aDtPozadDatumUhrady, aIdGestor, aUserId, aTypizLink, aDuvodZruseni,
 *   retCheckChange, aIdTypPreceneni, aUcet, aUcet2, aDPH, aIdBudget,
 *   aTTDeveloper, aIdCisUhrada, aIdSpolB, aUcetB, aUcetB2, cerpatRezervu,
 *   DT_NAVYSENI_SCHVALENO, DT_NAVYSENI_ZAMITNUTO, S_NAVYSENI_ZAMITNUTO,
 *   ID_NAVYSENI_MB, aCISLOFA, aDATPRIJ, aCASTKA, aFEIS_PRENOS, aDATDPH,
 *   aDATODES, aTYP_FKT, aCapex_Opex, aDATVYSTAVENI
 * )
 */
@Repository
public class DocumentPackageRepository {

    private static final Logger logger = LoggerFactory.getLogger(DocumentPackageRepository.class);

    private final DataSource dataSource;

    public DocumentPackageRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Call db_jt.kap_dokument.p_dokument procedure.
     *
     * @param akce Action: 'I' = Insert, 'U' = Update, 'D' = Delete
     * @param id Document ID (IN OUT parameter)
     * @param idCisDok Document type ID
     * @param idSpol Company ID
     * @param popis Description
     * @param idTypTran Transaction type ID
     * @param typizovanaTran Standardized transaction flag
     * @param typizovanaOd Standardized from date
     * @param typizovanaDo Standardized to date
     * @param mena Currency
     * @param pisemne Written amount
     * @param idProti Counter party ID
     * @param protistrana Counter party name
     * @param cislo Document number
     * @param dtSplatnosti Due date
     * @param idZadavatel Requester ID
     * @param idCisStatus Status ID
     * @param pokladniTransakce Cash transaction flag
     * @param dtPozadUhrada Requested payment date
     * @param idGestor Manager ID
     * @param userId Current user ID
     * @param typLink Link type
     * @param duvodZruseni Cancellation reason
     * @param idTypPreceneni Revaluation type ID
     * @param ucet Account
     * @param ucet2 Account 2
     * @param dph VAT flag
     * @param idBudget Budget ID
     * @param idTranDev Transaction developer ID
     * @param idUhrada Payment ID
     * @param idSpolB Company B ID
     * @param ucetB Account B
     * @param ucetB2 Account B2
     * @param cerpatRezervu Draw reserve
     * @param dtNavyseniSchvaleno Budget increase approved date
     * @param dtNavyseniZamitnuto Budget increase rejected date
     * @param sNavyseniZamitnuto Budget increase rejection reason
     * @param idNavyseniMb Budget increase MB ID
     * @param cisloFa Invoice number
     * @param dtPrijato Received date
     * @param castka Amount
     * @param feisPrenos FEIS transfer flag
     * @param dtDph VAT date
     * @param dtDatOdes Dispatch date
     * @param idTypFkt Function type ID
     * @param capexOpex CAPEX/OPEX flag
     * @param datVystaveni Issue date
     * @return Document ID (from OUT parameter) and sendMail flag
     * @throws SQLException if procedure call fails
     */
    public DokumentResult callDokument(
            String akce,
            Integer id,
            Integer idCisDok,
            Integer idSpol,
            String popis,
            Integer idTypTran,
            String typizovanaTran,
            java.sql.Date typizovanaOd,
            java.sql.Date typizovanaDo,
            String mena,
            String pisemne,
            Integer idProti,
            String protistrana,
            String cislo,
            java.sql.Date dtSplatnosti,
            Integer idZadavatel,
            Integer idCisStatus,
            String pokladniTransakce,
            java.sql.Date dtPozadUhrada,
            Integer idGestor,
            Integer userId,
            Integer typLink,
            String duvodZruseni,
            Integer idTypPreceneni,
            String ucet,
            String ucet2,
            String dph,
            Integer idBudget,
            Integer idTranDev,
            Integer idUhrada,
            Integer idSpolB,
            String ucetB,
            String ucetB2,
            String cerpatRezervu,
            java.sql.Date dtNavyseniSchvaleno,
            java.sql.Date dtNavyseniZamitnuto,
            String sNavyseniZamitnuto,
            Integer idNavyseniMb,
            String cisloFa,
            java.sql.Date dtPrijato,
            Double castka,
            String feisPrenos,
            java.sql.Date dtDph,
            java.sql.Date dtDatOdes,
            Integer idTypFkt,
            String capexOpex,
            java.sql.Date datVystaveni) throws SQLException {

        Connection conn = null;
        CallableStatement st = null;

        try {
            conn = dataSource.getConnection();

            // Prepare callable statement matching original code:
            // 48 parameters total
            String sql = "begin db_jt.kap_dokument.p_dokument(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,? ,?,?,?,?,?,?,? ,?,?); end;";
            st = conn.prepareCall(sql);

            // Set parameters (1-indexed)
            st.setString(1, akce);
            st.registerOutParameter(2, Types.INTEGER);
            st.setInt(2, id);
            st.setInt(3, idCisDok);

            if (idSpol != null && idSpol > 0) {
                st.setInt(4, idSpol);
            } else {
                st.setNull(4, Types.INTEGER);
            }

            st.setString(5, popis);

            if (idTypTran != null && idTypTran > 0) {
                st.setInt(6, idTypTran);
            } else {
                st.setNull(6, Types.INTEGER);
            }

            st.setString(7, typizovanaTran);
            st.setDate(8, typizovanaOd);
            st.setDate(9, typizovanaDo);
            st.setString(10, mena);
            st.setString(11, pisemne);

            if (idProti != null && idProti > 0) {
                st.setInt(12, idProti);
            } else {
                st.setNull(12, Types.INTEGER);
            }

            st.setString(13, protistrana);
            st.setString(14, cislo);
            st.setDate(15, dtSplatnosti);
            st.setInt(16, idZadavatel);
            st.setInt(17, idCisStatus);
            st.setString(18, pokladniTransakce);
            st.setDate(19, dtPozadUhrada);

            if (idGestor != null && idGestor > 0) {
                st.setInt(20, idGestor);
            } else {
                st.setNull(20, Types.INTEGER);
            }

            st.setInt(21, userId);

            if (typLink != null && typLink > 0) {
                st.setInt(22, typLink);
            } else {
                st.setNull(22, Types.INTEGER);
            }

            st.setString(23, duvodZruseni);
            st.registerOutParameter(24, Types.INTEGER);

            if (idTypPreceneni != null && idTypPreceneni > 0) {
                st.setInt(25, idTypPreceneni);
            } else {
                st.setNull(25, Types.INTEGER);
            }

            st.setString(26, ucet == null ? null : ucet.trim());
            st.setString(27, ucet2 == null ? null : ucet2.trim());
            st.setString(28, dph);

            if (idBudget != null && idBudget > 0) {
                st.setInt(29, idBudget);
            } else {
                st.setNull(29, Types.INTEGER);
            }

            if (idTranDev != null && idTranDev > 0) {
                st.setInt(30, idTranDev);
            } else {
                st.setNull(30, Types.INTEGER);
            }

            if (idUhrada != null && idUhrada > 0) {
                st.setInt(31, idUhrada);
            } else {
                st.setNull(31, Types.INTEGER);
            }

            if (idSpolB != null && idSpolB > 0) {
                st.setInt(32, idSpolB);
            } else {
                st.setNull(32, Types.INTEGER);
            }

            st.setString(33, ucetB == null ? null : ucetB.trim());
            st.setString(34, ucetB2 == null ? null : ucetB2.trim());
            st.setString(35, cerpatRezervu);
            st.setDate(36, dtNavyseniSchvaleno);
            st.setDate(37, dtNavyseniZamitnuto);
            st.setString(38, sNavyseniZamitnuto);

            if (idNavyseniMb != null && idNavyseniMb > 0) {
                st.setInt(39, idNavyseniMb);
            } else {
                st.setNull(39, Types.INTEGER);
            }

            st.setString(40, cisloFa);
            st.setDate(41, dtPrijato);
            st.setDouble(42, castka);
            st.setString(43, feisPrenos);
            st.setDate(44, dtDph);
            st.setDate(45, dtDatOdes);

            if (idTypFkt != null && idTypFkt > 0) {
                st.setInt(46, idTypFkt);
            } else {
                st.setNull(46, Types.INTEGER);
            }

            st.setString(47, capexOpex);
            st.setDate(48, datVystaveni);

            // Execute procedure
            logger.info("Executing db_jt.kap_dokument.p_dokument with action: {}, idDokument: {}", akce, id);
            st.execute();
            logger.info("Successfully executed db_jt.kap_dokument.p_dokument");

            // Get OUT parameters
            int idRet = st.getInt(2);
            int sendMail = st.getInt(24);

            return new DokumentResult(idRet, sendMail);

        } catch (SQLException e) {
            logger.error("Failed to execute db_jt.kap_dokument.p_dokument", e);
            throw new SQLException("Failed to call db_jt.kap_dokument.p_dokument: " + e.getMessage(), e);
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
     * Call db_jt.kap_dokument.p_setUhrada procedure.
     *
     * @param id Document ID
     * @param idUhrada Payment ID
     * @throws SQLException if procedure call fails
     */
    public void callSetUhrada(Integer id, Integer idUhrada) throws SQLException {
        Connection conn = null;
        CallableStatement st = null;

        try {
            conn = dataSource.getConnection();

            String sql = "begin db_jt.kap_dokument.p_setUhrada(?,?); end;";
            st = conn.prepareCall(sql);

            st.setInt(1, id);
            if (idUhrada != null && idUhrada > 0) {
                st.setInt(2, idUhrada);
            } else {
                st.setNull(2, Types.INTEGER);
            }

            logger.info("Executing db_jt.kap_dokument.p_setUhrada with id: {}, idUhrada: {}", id, idUhrada);
            st.execute();
            logger.info("Successfully executed db_jt.kap_dokument.p_setUhrada");

        } catch (SQLException e) {
            logger.error("Failed to execute db_jt.kap_dokument.p_setUhrada", e);
            throw new SQLException("Failed to call db_jt.kap_dokument.p_setUhrada: " + e.getMessage(), e);
        } finally {
            if (st != null) try { st.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Call db_jt.kap_dokument.p_statusChange procedure.
     *
     * @param id Document ID
     * @param idCisStatus New status ID
     * @throws SQLException if procedure call fails
     */
    public void callStatusChange(Integer id, Integer idCisStatus) throws SQLException {
        Connection conn = null;
        CallableStatement st = null;

        try {
            conn = dataSource.getConnection();

            String sql = "begin db_jt.kap_dokument.p_statusChange(?,?); end;";
            st = conn.prepareCall(sql);

            st.setInt(1, id);
            st.setInt(2, idCisStatus);

            logger.info("Executing db_jt.kap_dokument.p_statusChange with id: {}, idCisStatus: {}", id, idCisStatus);
            st.execute();
            logger.info("Successfully executed db_jt.kap_dokument.p_statusChange");

        } catch (SQLException e) {
            logger.error("Failed to execute db_jt.kap_dokument.p_statusChange", e);
            throw new SQLException("Failed to call db_jt.kap_dokument.p_statusChange: " + e.getMessage(), e);
        } finally {
            if (st != null) try { st.close(); } catch (SQLException ignored) {}
            if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        }
    }

    /**
     * Result class to hold return values from callDokument procedure.
     */
    public static class DokumentResult {
        private final int idRet;
        private final int sendMail;

        public DokumentResult(int idRet, int sendMail) {
            this.idRet = idRet;
            this.sendMail = sendMail;
        }

        public int getIdRet() {
            return idRet;
        }

        public int getSendMail() {
            return sendMail;
        }
    }
}
