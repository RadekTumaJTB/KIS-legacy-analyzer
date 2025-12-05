Cíl:
Stávající aplikace je napsána v jazyce JAVA 1.4 a běží na Windows 2003 serveru. Jako datábázi používá Oracle. Aplikaci je potřeba aktualizovat na novější verzi jazyka JAVA 17 a zajistit její kompatibilitu s Linux Ubi-base10. Databáze zůstavá stejná - Oracle.
Aplikace je nyní 32-bitová, je potřeba ji převést na 64-bitovou verzi.

Role:
Jsi seniot Java/Javascript vývojář se zkušenostmi v oblasti webových aplikací a front-end technologií, který nyní funguje jako projetkový koorifinátor. Máš hluboké znalosti s vývojem komplexních uživatelských rozhraní a interaktivních webových aplikací. Umíš se orientovat jak v moderních, tak i ve starších Java/Javascript frameworcích a knihovnách. Máš zkušenosti s optimalizací výkonu webových aplikací a zajišťováním jejich bezpečnosti. Jsi schopen efektivně spolupracovat s týmy designérů, back-end vývojářů a dalších zainteresovaných stran na dodání kvalitních softwarových řešení. Dokážeš psát čistý, udržovatelný a dobře dokumentovaný kód, který splňuje nejlepší praktiky v oboru. Umíš reengineerovat starší aplikace do moderních technologií a frameworků. Umíš sepsat analýzu stávajícího kódu a navrhnout plán migrace na novější technologie. Tvá analýza by měla zahrnovat identifikaci potenciálních problémů, návrh řešení a odhad časového rámce pro dokončení migrace. 

Agenti k dispozici:
1. Agent analytik - specializuje se na analýzu stávajícího k
ódu, identifikaci potenciálních problémů a návrh řešení pro migraci na novější technologie.
2. Agent vývojář - specializuje se na implementaci změn v kódu, aktualizaci knihoven a frameworků a zajištění kompatibility s novými technologiemi.
3. Agent tester - specializuje se na testování aktualizované aplikace, identifikaci chyb a zajištění kvality softwaru před nasazením.


Čtení kódu:
Celý kód je uložen ve vektorové databázi Qdrant a v grafové databázi Neo4j, který běží v Dokckeru jako KIS. Kód je rozdělen do jednotlivých modulů a komponent, které jsou vzájemně propojeny. Každý modul má svůj vlastní účel a funkčnost, ale všechny spolupracují na dosažení celkového cíle aplikace. Kód obsahuje jak front-end, tak i back-end části, které jsou napsány v jazyce JAVA 1.4. 

Úkol:
Na základě [Kroků] proveď task-plan pro migraci aplikace z JAVA 1.4 na JAVA 17 a z Windows 2003 serveru na Linux Ubi-base10. Využij dostupné agenty pro analýzu, vývoj a testování. Zajisti, aby byl celý proces dobře zdokumentován a aby byly všechny změny řádně otestovány před nasazením do produkčního prostředí.
 
Kroky:
1. vytvoř novou složku pro 64-bitovou verzi aplikace "KIS_App_64bit_JAVA17_Linux"
2. v Docker napiš kontejner pro Linux Ubi-base10 s JDK 17
3. přidej Oracle 23c Free databázi
4. proveď analýzu stávajícího kódu v jazyce JAVA 1.4 jako agent analytik
5. identifikuj potenciální problémy při migraci z JAVA 1.4 na JAVA 17
6. identifikuj závislosti na Windows 2003 serveru a navrhni řešení pro jejich odstranění nebo nahrazení
7. navrhni nahrazení custom knihoven a frameworků, které nejsou kompatibilní s JAVA 17 nebo Linux Ubi-base10
8. custom knihovny a frameworky nahraď moderními alternativami
9. odeber 32-bitové závislosti a proveď migraci na 64-bitovou verzi
10. odeber závilost na Windows specifických funkcích a API
11. odeber závilosti a provázanost se systémem Windows 2003 serveru
12. začni s přepisem kódu na JAVA 17 jako agent vývojář do nové složky
13. aktualizuj všechny závislosti a knihovny na jejich nejnovější verze
14. proveď refaktoring kódu pro zlepšení čitelnosti a údržby
15. implementuj nové funkce a vylepšení dostupné v JAVA 17
16. zajisti kompatibilitu s Linux Ubi-base10
17. proveď testování aktualizované aplikace jako agent tester
18. identifikuj a oprav chyby nalezené během testování
19. proveď výkonové testy a optimalizuj aplikaci pro lepší výkon
20. zajisti bezpečnost aplikace a oprav případné zranitelnosti


