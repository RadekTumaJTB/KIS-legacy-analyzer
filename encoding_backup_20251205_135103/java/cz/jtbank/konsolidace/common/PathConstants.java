package cz.jtbank.konsolidace.common;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Platform-independent path constants for KIS Banking application.
 *
 * <p>This class replaces hardcoded Windows paths with configurable,
 * platform-independent paths suitable for Linux deployment.</p>
 *
 * <p><b>Migration Information:</b></p>
 * <ul>
 *   <li>Migration Date: 2025-12-05</li>
 *   <li>Replaces: 430+ hardcoded Windows paths across 84 files</li>
 *   <li>Primary target: Constants.java (260+ references)</li>
 * </ul>
 *
 * <p><b>Usage:</b></p>
 * <pre>
 * // Get export path
 * String exportPath = PathConstants.getExportPath();
 *
 * // Build file path
 * String filePath = PathConstants.buildPath(PathConstants.getExportPath(), "data.xlsx");
 *
 * // Get specific directory
 * String projektyDir = PathConstants.getDataProjekty();
 * </pre>
 *
 * <p><b>Configuration:</b></p>
 * Paths are loaded from application-paths.properties and can be overridden
 * via system properties (e.g., -Dkis.paths.base=/custom/path)
 *
 * @author KIS Migration Team
 * @version 2.0 (Linux-compatible)
 * @see Constants
 */
public class PathConstants {

    private static final Properties pathProperties = new Properties();
    private static final String PROPERTIES_FILE = "application-paths.properties";

    // Load properties on class initialization
    static {
        loadProperties();
    }

    /**
     * Loads path configuration from properties file.
     * System properties take precedence over file properties.
     */
    private static void loadProperties() {
        try (InputStream input = PathConstants.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("WARNING: Cannot find " + PROPERTIES_FILE +
                                 " - using default paths");
                loadDefaultProperties();
                return;
            }
            pathProperties.load(input);
            System.out.println("INFO: Loaded path configuration from " + PROPERTIES_FILE);
        } catch (IOException e) {
            System.err.println("ERROR: Failed to load " + PROPERTIES_FILE +
                             " - using defaults: " + e.getMessage());
            loadDefaultProperties();
        }
    }

    /**
     * Loads default Linux paths as fallback.
     */
    private static void loadDefaultProperties() {
        pathProperties.setProperty("kis.paths.base", "/opt/kis-banking");
        pathProperties.setProperty("kis.paths.root", "${kis.paths.base}/Konsolidace_JT");
        // Add other essential defaults as needed
    }

    /**
     * Gets a property value, resolving ${...} placeholders and checking system properties.
     *
     * @param key property key
     * @param defaultValue default value if property not found
     * @return resolved property value
     */
    private static String getProperty(String key, String defaultValue) {
        // System properties take precedence
        String value = System.getProperty(key);
        if (value == null) {
            value = pathProperties.getProperty(key, defaultValue);
        }
        return resolvePlaceholders(value);
    }

    /**
     * Resolves ${...} placeholders in property values.
     *
     * @param value value potentially containing placeholders
     * @return resolved value
     */
    private static String resolvePlaceholders(String value) {
        if (value == null) return null;

        int start = value.indexOf("${");
        if (start == -1) return value;

        int end = value.indexOf("}", start);
        if (end == -1) return value;

        String placeholder = value.substring(start + 2, end);
        String replacement = getProperty(placeholder, "");

        String result = value.substring(0, start) + replacement + value.substring(end + 1);
        return resolvePlaceholders(result); // Recursive resolution
    }

    /**
     * Builds a platform-independent file path from components.
     *
     * @param first the first path component
     * @param more additional path components
     * @return platform-independent path string
     */
    public static String buildPath(String first, String... more) {
        return Paths.get(first, more).toString();
    }

    /**
     * Converts a path to a Path object.
     *
     * @param path the path string
     * @return Path object
     */
    public static Path toPath(String path) {
        return Paths.get(path);
    }

    // ==========================================================================
    // Base Paths
    // ==========================================================================

    public static String getBasePath() {
        return getProperty("kis.paths.base", "/opt/kis-banking");
    }

    public static String getRootPath() {
        return getProperty("kis.paths.root", buildPath(getBasePath(), "Konsolidace_JT"));
    }

    // ==========================================================================
    // Main Data Directories
    // ==========================================================================

    public static String getDataPath() {
        return getProperty("kis.paths.data", buildPath(getRootPath(), "data"));
    }

    public static String getXlsPath() {
        return getProperty("kis.paths.xls", buildPath(getRootPath(), "data"));
    }

    public static String getCsvPath() {
        return getProperty("kis.paths.csv", buildPath(getRootPath(), "csv"));
    }

    public static String getExportPath() {
        return getProperty("kis.paths.export", buildPath(getRootPath(), "export"));
    }

    public static String getProtokolPath() {
        return getProperty("kis.paths.protokol", buildPath(getRootPath(), "protokoly"));
    }

    public static String getSablonyPath() {
        return getProperty("kis.paths.sablony", buildPath(getRootPath(), "sablony"));
    }

    public static String getSablonyCartesisPath() {
        return getProperty("kis.paths.sablony.cartesis", buildPath(getSablonyPath(), "cartesis"));
    }

    public static String getDocFilesPath() {
        return getProperty("kis.paths.docfiles", buildPath(getRootPath(), "docfiles"));
    }

    public static String getEviFilesPath() {
        return getProperty("kis.paths.evifiles", buildPath(getRootPath(), "evifiles"));
    }

    public static String getArchivPath() {
        return getProperty("kis.paths.archiv", buildPath(getRootPath(), "archiv"));
    }

    // ==========================================================================
    // Temporary Directories (.TEMP)
    // ==========================================================================

    public static String getTempBasePath() {
        return getProperty("kis.paths.temp.base", buildPath(getRootPath(), ".TEMP"));
    }

    public static String getDirTemp() {
        return ".TEMP" + getFileSeparator();
    }

    public static String getDirAdminNaklady() {
        return getProperty("kis.paths.temp.admin.naklady",
                          buildPath(getTempBasePath(), "adminNaklady"));
    }

    public static String getDirBilanceDetail() {
        return getProperty("kis.paths.temp.bilance.detail",
                          buildPath(getTempBasePath(), "bilanceDetail"));
    }

    public static String getDirBudgetPrekroceni() {
        return getProperty("kis.paths.temp.budget.prekroceni",
                          buildPath(getTempBasePath(), "budPres"));
    }

    public static String getDirBudgetSchvalovani() {
        return getProperty("kis.paths.temp.budget.schvalovani",
                          buildPath(getTempBasePath(), "budSchval"));
    }

    public static String getDirDokladyDetail() {
        return getProperty("kis.paths.temp.doklady.detail",
                          buildPath(getTempBasePath(), "dokladyDetail"));
    }

    public static String getDirEviOr() {
        return getProperty("kis.paths.temp.evi.or",
                          buildPath(getTempBasePath(), "eviOr"));
    }

    public static String getDirChybyMustku() {
        return getProperty("kis.paths.temp.chyby.mustku",
                          buildPath(getTempBasePath(), "chybyMustku"));
    }

    public static String getDirKapOr() {
        return getProperty("kis.paths.temp.kap.or",
                          buildPath(getTempBasePath(), "kap"));
    }

    public static String getDirKonsSpravce() {
        return getProperty("kis.paths.temp.kons.spravce",
                          buildPath(getTempBasePath(), "konsSpravce"));
    }

    public static String getDirMpop() {
        return getProperty("kis.paths.temp.mpop",
                          buildPath(getTempBasePath(), "mpop"));
    }

    public static String getDirMustky() {
        return getProperty("kis.paths.temp.mustky",
                          buildPath(getTempBasePath(), "mustky"));
    }

    public static String getDirPbKlienti() {
        return getProperty("kis.paths.temp.pb.klienti",
                          buildPath(getTempBasePath(), "pbKlientiA"));
    }

    public static String getDirProjektyKarta() {
        return getProperty("kis.paths.temp.projekty.karta",
                          buildPath(getTempBasePath(), "projektKarta"));
    }

    public static String getDirProjektySldev() {
        return getProperty("kis.paths.temp.projekty.sldev",
                          buildPath(getTempBasePath(), "projektySLDev"));
    }

    public static String getDirProjektyTransakce() {
        return getProperty("kis.paths.temp.projekty.transakce",
                          buildPath(getTempBasePath(), "projektyTran"));
    }

    public static String getDirProtiGroup() {
        return getProperty("kis.paths.temp.proti.group",
                          buildPath(getTempBasePath(), "protiGroup"));
    }

    public static String getDirSl() {
        return getProperty("kis.paths.temp.sl",
                          buildPath(getTempBasePath(), "slOutput"));
    }

    public static String getDirSlPostup() {
        return getProperty("kis.paths.temp.sl.postup",
                          buildPath(getTempBasePath(), "slPostup"));
    }

    public static String getDirSpolPredav() {
        return getProperty("kis.paths.temp.spol.predav",
                          buildPath(getTempBasePath(), "spolPredav"));
    }

    public static String getDirUnifUcty() {
        return getProperty("kis.paths.temp.unif.ucty",
                          buildPath(getTempBasePath(), "unifUcty"));
    }

    public static String getDirZamekGenerovani() {
        return getProperty("kis.paths.temp.zamek.generovani",
                          buildPath(getTempBasePath(), "zamekGen"));
    }

    public static String getDirZmenyProtistran() {
        return getProperty("kis.paths.temp.zmeny.protistran",
                          buildPath(getTempBasePath(), "zmenyProtistran"));
    }

    public static String getDirPodnikatelUcty() {
        return getProperty("kis.paths.temp.podnikatel.ucty",
                          buildPath(getTempBasePath(), "podnikatelUcty"));
    }

    public static String getDirZamekProtokol() {
        return getProperty("kis.paths.temp.zamek.protokol",
                          buildPath(getTempBasePath(), "zamekProtokol"));
    }

    public static String getDirZmenaMajUcast() {
        return getProperty("kis.paths.temp.zmena.maj.ucast",
                          buildPath(getTempBasePath(), "zmenaMU"));
    }

    // ==========================================================================
    // Data Directories (.DATA)
    // ==========================================================================

    public static String getDataBasePath() {
        return getProperty("kis.paths.data.base", buildPath(getRootPath(), ".DATA"));
    }

    public static String getDirData() {
        return ".DATA" + getFileSeparator();
    }

    public static String getDataBudgetStd() {
        return getProperty("kis.paths.data.budget.std",
                          buildPath(getDataBasePath(), "budget"));
    }

    public static String getDataBudgetNaklad() {
        return getProperty("kis.paths.data.budget.naklad",
                          buildPath(getDataBasePath(), "budgetMustekNaklad"));
    }

    public static String getDataBudgetProjekt() {
        return getProperty("kis.paths.data.budget.projekt",
                          buildPath(getDataBasePath(), "budgetProjekt"));
    }

    public static String getDataEmise() {
        return getProperty("kis.paths.data.emise",
                          buildPath(getDataBasePath(), "emise"));
    }

    public static String getDataIfrsZmeny() {
        return getProperty("kis.paths.data.ifrs.zmeny",
                          buildPath(getDataBasePath(), "ifrszmeny"));
    }

    public static String getDataDokladKamil() {
        return getProperty("kis.paths.data.doklad.kamil",
                          buildPath(getDataBasePath(), "kamil"));
    }

    public static String getDataSubkonsolidace() {
        return getProperty("kis.paths.data.subkonsolidace",
                          buildPath(getDataBasePath(), "konsolidace"));
    }

    public static String getDataKonsZmeny() {
        return getProperty("kis.paths.data.kons.zmeny",
                          buildPath(getDataBasePath(), "konszmeny"));
    }

    public static String getDataMajetek() {
        return getProperty("kis.paths.data.majetek",
                          buildPath(getDataBasePath(), "majetek"));
    }

    public static String getDataOdbory() {
        return getProperty("kis.paths.data.odbory",
                          buildPath(getDataBasePath(), "odbory"));
    }

    public static String getDataPoziceMu() {
        return getProperty("kis.paths.data.pozice.mu",
                          buildPath(getDataBasePath(), "pozice_"));
    }

    public static String getDataProjektyDoklady() {
        return getProperty("kis.paths.data.projekty.doklady",
                          buildPath(getDataBasePath(), "projektDoklad"));
    }

    public static String getDataProjekty() {
        return getProperty("kis.paths.data.projekty",
                          buildPath(getDataBasePath(), "projekty"));
    }

    public static String getDataProjektyCf() {
        return getProperty("kis.paths.data.projekty.cf",
                          buildPath(getDataBasePath(), "projektyCF"));
    }

    public static String getDataUvery() {
        return getProperty("kis.paths.data.uvery",
                          buildPath(getDataBasePath(), "projektyUvery"));
    }

    public static String getDataProtiOsoby() {
        return getProperty("kis.paths.data.proti.osoby",
                          buildPath(getDataBasePath(), "protiOsoby"));
    }

    public static String getDataSpolecnosti() {
        return getProperty("kis.paths.data.spolecnosti",
                          buildPath(getDataBasePath(), "spolecnosti"));
    }

    public static String getDataCartesis() {
        return getProperty("kis.paths.data.cartesis",
                          buildPath(getDataBasePath(), "CARTESIS"));
    }

    public static String getDataPoziceMuLogs() {
        return getProperty("kis.paths.data.pozice.mu.logs",
                          buildPath(getDataBasePath(), "muProtokol", "pozice_"));
    }

    // ==========================================================================
    // System Configuration
    // ==========================================================================

    public static String getJaznXmlPath() {
        return getProperty("kis.paths.jazn.xml",
                          "/opt/oracle/j2ee10/j2ee/OC4J_app/application-deployments/kis/jazn-data.xml");
    }

    // ==========================================================================
    // Utility Methods
    // ==========================================================================

    /**
     * Gets the platform-specific file separator.
     *
     * @return file separator (/ on Unix, \ on Windows)
     */
    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    /**
     * Gets the platform-specific path separator.
     *
     * @return path separator (: on Unix, ; on Windows)
     */
    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    /**
     * Checks if running on Windows platform.
     *
     * @return true if Windows, false otherwise
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Checks if running on Linux platform.
     *
     * @return true if Linux, false otherwise
     */
    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("nux");
    }

    /**
     * Gets diagnostic information about current path configuration.
     * Useful for troubleshooting deployment issues.
     *
     * @return diagnostic string with key paths and platform info
     */
    public static String getDiagnostics() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== KIS Path Configuration Diagnostics ===\n");
        sb.append("OS: ").append(System.getProperty("os.name")).append("\n");
        sb.append("File Separator: ").append(getFileSeparator()).append("\n");
        sb.append("Base Path: ").append(getBasePath()).append("\n");
        sb.append("Root Path: ").append(getRootPath()).append("\n");
        sb.append("Export Path: ").append(getExportPath()).append("\n");
        sb.append("Templates Path: ").append(getSablonyPath()).append("\n");
        sb.append("JAZN XML: ").append(getJaznXmlPath()).append("\n");
        sb.append("Properties loaded: ").append(pathProperties.size()).append(" entries\n");
        sb.append("==========================================");
        return sb.toString();
    }
}
