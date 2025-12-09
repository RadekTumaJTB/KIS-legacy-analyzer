package cz.jtbank.kis.bff.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "KP_KTG_APPUSER", schema = "DB_JT")
public class AppUserEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "S_LOGIN", unique = true, nullable = false, length = 50)
    private String login;

    @Column(name = "S_JMENO", nullable = false, length = 100)
    private String jmeno;

    @Column(name = "S_EMAIL", length = 100)
    private String email;

    @Column(name = "S_POZICE", length = 100)
    private String pozice;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getJmeno() { return jmeno; }
    public void setJmeno(String jmeno) { this.jmeno = jmeno; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPozice() { return pozice; }
    public void setPozice(String pozice) { this.pozice = pozice; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
